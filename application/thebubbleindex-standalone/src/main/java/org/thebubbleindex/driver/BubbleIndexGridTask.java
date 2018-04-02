package org.thebubbleindex.driver;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.thebubbleindex.exception.FailedToRunIndex;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.runnable.RunIndex;
import org.thebubbleindex.swing.BubbleIndexWorker;
import org.thebubbleindex.util.Utilities;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceProperty;
import com.gigaspaces.annotation.pojo.SpaceRouting;

/**
 * BubbleIndexGridTask class is the central logic component of the application.
 * Provides variable initialization, reads input files and stores results
 * obtained in the Run for a single time window.
 *
 * @author thebubbleindex
 */
@SpaceClass
public class BubbleIndexGridTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1741595830806069916L;
	private List<String> outputMessageList = new ArrayList<String>(200);
	private String categoryName;
	private String selectionName;

	private String previousFilePath;

	private byte[] previousFileBytes;
	private String filePath;
	private String savePath;
	private String openCLSrc;

	private double omega;
	private double mCoeff;
	private double tCrit;
	private int window;
	private int dataSize;

	private int[] dailyPriceDateInt;
	private double[] results;

	private double[] dailyPriceDoubleValues;
	private RunContext runContext;

	private String id;

	/**
	 * BubbleIndexGridTask constructor
	 * 
	 * @param omega
	 * @param mCoeff
	 * @param tCrit
	 * @param window
	 * @param categoryName
	 * @param selectionName
	 */
	public BubbleIndexGridTask(final double omega, final double mCoeff, final double tCrit, final int window,
			final String categoryName, final String selectionName, final DailyDataCache dailyDataCache,
			final Indices indices, final String openCLSrc, final RunContext runContext) {
		if (runContext.isComputeGrid())
			outputMessageList.add("Initializing The Bubble Index. Category Name = " + categoryName
					+ ", Selection Name = " + selectionName + ", Omega = " + omega + ", M = " + mCoeff + ", TCrit = "
					+ tCrit + ", Window = " + window);

		this.omega = omega;
		this.mCoeff = mCoeff;
		this.tCrit = tCrit;
		this.window = window;
		this.categoryName = categoryName;
		this.selectionName = selectionName;
		this.openCLSrc = openCLSrc;
		this.runContext = runContext;

		if (!this.runContext.isStop())
			setFilePaths(indices);

		if (dailyDataCache.getSelectionName().equals(this.selectionName)) {
			final List<String> dailyPriceDate = dailyDataCache.getDailyPriceDate();
			dailyPriceDateInt = new int[dailyPriceDate.size()];
			Utilities.convertDatesToIntArray(dailyPriceDate, dailyPriceDateInt);

			dailyPriceDoubleValues = dailyDataCache.getDailyPriceDoubleValues();
			dataSize = new Integer(dailyPriceDoubleValues.length);
		} else {
			final List<String> dailyPriceData = new ArrayList<String>(10000);
			final List<String> dailyPriceDate = new ArrayList<String>(10000);

			if (!this.runContext.isStop())
				Utilities.ReadValues(filePath, dailyPriceDate, dailyPriceData, false, false);

			dailyPriceDateInt = new int[dailyPriceDate.size()];
			Utilities.convertDatesToIntArray(dailyPriceDate, dailyPriceDateInt);

			dailyDataCache.setSelectionName(this.selectionName);
			dailyDataCache.setDailyPriceData(new ArrayList<String>(dailyPriceData));
			dailyDataCache.setDailyPriceDate(new ArrayList<String>(dailyPriceDate));

			dataSize = new Integer(dailyPriceData.size());

			dailyPriceDoubleValues = new double[dataSize];

			if (!this.runContext.isStop())
				convertPrices(dailyPriceData);

			dailyDataCache.setDailyPriceDoubleValues(dailyPriceDoubleValues);
		}
	}

	/**
	 * BubbleIndexGridTask constructor, typically used only in the case where
	 * plotting proceeds this call.
	 * 
	 * @param categoryName
	 * @param selectionName
	 */
	public BubbleIndexGridTask(final String categoryName, final String selectionName,
			final DailyDataCache dailyDataCache, final Indices indices, final String openCLSrc,
			final RunContext runContext) {

		omega = 0.0;
		mCoeff = 0.0;
		window = 0;
		tCrit = 0.0;

		this.categoryName = categoryName;
		this.selectionName = selectionName;
		this.openCLSrc = openCLSrc;
		this.runContext = runContext;

		if (!this.runContext.isStop())
			setFilePaths(indices);

		final List<String> dailyPriceData = new ArrayList<String>(10000);
		final List<String> dailyPriceDate = new ArrayList<String>();

		if (!this.runContext.isStop())
			Utilities.ReadValues(filePath, dailyPriceDate, dailyPriceData, false, false);

		dailyPriceDateInt = new int[dailyPriceDate.size()];
		Utilities.convertDatesToIntArray(dailyPriceDate, dailyPriceDateInt);
		dataSize = dailyPriceData.size();

		dailyPriceDoubleValues = new double[dataSize];

		if (!this.runContext.isStop())
			convertPrices(dailyPriceData);
	}

	public BubbleIndexGridTask() {
		omega = -1.0;
		mCoeff = -10000.0;
		window = -1;
		tCrit = -1.0;
		dataSize = -1;
		dailyPriceDateInt = null;
		dailyPriceDoubleValues = null;

		this.categoryName = null;
		this.selectionName = null;
		this.openCLSrc = null;
		this.runContext = null;
	}

	/**
	 * runBubbleIndex Core run method. Provides a GPU and CPU version. Catches any
	 * errors which the Run methods may throw.
	 * 
	 * @param bubbleIndexWorker
	 */
	public void runBubbleIndex(final BubbleIndexWorker bubbleIndexWorker) {
		if (dataSize > window) {

			// unzip previous file bytes
			try {
				previousFileBytes = Utilities.unZipBytes(previousFileBytes);
			} catch (final Exception ex) {
				previousFileBytes = null;
			}

			final List<Double> resultsList = new ArrayList<Double>();
			final List<String> dailyPriceDate = new ArrayList<String>(dailyPriceDateInt.length);
			for (final int dailyPriceDateIntValue : dailyPriceDateInt) {
				dailyPriceDate.add(Utilities.getDateStringFromInt(dailyPriceDateIntValue));
			}

			final RunIndex runIndex = new RunIndex(bubbleIndexWorker, dailyPriceDoubleValues, dataSize, window,
					resultsList, dailyPriceDate, previousFileBytes, selectionName, omega, mCoeff, tCrit, null,
					openCLSrc, runContext);

			if (!runContext.isForceCPU()) {
				try {
					if (runContext.isComputeGrid())
						outputMessageList.add("Executing GPU Run. Category Name = " + categoryName
								+ ", Selection Name = " + selectionName);

					Logs.myLogger.info("Executing GPU Run. Category Name = {}, Selection Name = {}", categoryName,
							selectionName);
					runIndex.execIndexWithGPU();

					results = new double[resultsList.size()];
					int index = 0;
					for (final Double result : resultsList) {
						results[index++] = result.doubleValue();
					}
				} catch (final FailedToRunIndex er) {
					Logs.myLogger.info("Category Name = {}, Selection Name = {}, Window = {}. {}", categoryName,
							selectionName, window, er);
					if (runContext.isGUI() && !runContext.isComputeGrid()) {
						bubbleIndexWorker.publishText(er.getMessage());
					} else {
						if (runContext.isComputeGrid())
							outputMessageList.add(er.getMessage());

						System.out.println(er.getMessage());
					}
					results = null;
				}
			} else {
				try {
					Logs.myLogger.info("Executing CPU Run. Category Name = {}, Selection Name = {}", categoryName,
							selectionName);
					runIndex.execIndexWithCPU();

					results = new double[resultsList.size()];
					int index = 0;
					for (final Double result : resultsList) {
						results[index++] = result.doubleValue();
					}
				} catch (final FailedToRunIndex er) {
					Logs.myLogger.error("Category Name = {}, Selection Name = {}, Window = {}. {}", categoryName,
							selectionName, window, er);
					if (runContext.isGUI() && !runContext.isComputeGrid()) {
						bubbleIndexWorker.publishText("Error: " + er);
					} else {
						System.out.println("Error: " + er);
					}
					results = null;
				}
			}

			if (!runContext.isStop())
				outputMessageList.add("Completed processing for category: " + categoryName + ", selection: "
						+ selectionName + ", window: " + window);
		}

		// make sure, if the stop button is pressed that there are no results
		if (runContext.isStop())
			results = null;

		previousFileBytes = null;
	}

	/**
	 * outputResults saves the results of the run to the savePath
	 * 
	 * @param bubbleIndexWorker
	 */
	public void outputResults(final BubbleIndexWorker bubbleIndexWorker) {
		if (dataSize > window) {
			if (results != null) {
				outputMessageList.add("Completed with " + (results != null ? results.length : 0) + " results.");

				final String Name = selectionName + window + "days.csv";

				if (runContext.isGUI() && !runContext.isComputeGrid()) {
					bubbleIndexWorker.publishText("Writing output file: " + Name);
				} else {
					outputMessageList.add("Writing output file: " + previousFilePath);
					System.out.println("Writing output file: " + Name);
				}

				try {
					Logs.myLogger.info("Writing output file: {}", previousFilePath);

					final List<String> dailyPriceDate = new ArrayList<String>(dailyPriceDateInt.length);
					final List<Double> resultsList = new ArrayList<Double>();

					for (final int dailyPriceDateIntValue : dailyPriceDateInt) {
						dailyPriceDate.add(Utilities.getDateStringFromInt(dailyPriceDateIntValue));
					}

					for (final double result : results) {
						resultsList.add(new Double(result));
					}

					Utilities.WriteCSV(savePath, resultsList, dataSize - window, Name, dailyPriceDate,
							new File(previousFilePath).exists());
				} catch (final IOException ex) {
					Logs.myLogger.error("Failed to write csv output. Save path = {}. {}", savePath, ex);

					if (runContext.isComputeGrid())
						outputMessageList.add("Failed to write csv output." + ex.getMessage());
				}
			}
		}
	}

	/**
	 * setFilePaths helper method to create the file paths which contain the daily
	 * data and any previously existing runs.
	 * 
	 */
	private void setFilePaths(final Indices indices) {

		filePath = indices.getUserDir() + indices.getProgramDataFolder() + indices.getFilePathSymbol() + categoryName
				+ indices.getFilePathSymbol() + selectionName + indices.getFilePathSymbol() + selectionName
				+ "dailydata.csv";

		savePath = indices.getUserDir() + indices.getProgramDataFolder() + indices.getFilePathSymbol() + categoryName
				+ indices.getFilePathSymbol() + selectionName + indices.getFilePathSymbol();

		previousFilePath = indices.getUserDir() + indices.getProgramDataFolder() + indices.getFilePathSymbol()
				+ categoryName + indices.getFilePathSymbol() + selectionName + indices.getFilePathSymbol()
				+ selectionName + Integer.toString(window) + "days.csv";

		byte[] tempFileBytes = null;

		try {
			tempFileBytes = Files.readAllBytes(new File(previousFilePath).toPath());
		} catch (final IOException e) {
			// ignore error, if prev file does not exist then previousFileBytes
			// will be null
		} finally {
			if (tempFileBytes != null && tempFileBytes.length > 0) {
				try {
					previousFileBytes = Utilities.zipBytes("zip" + selectionName, tempFileBytes);
				} catch (final IOException ex) {
					previousFileBytes = null;
				}
			}
		}

		if (!runContext.isComputeGrid()) {
			Utilities.displayOutput(runContext, "Output File Path: " + previousFilePath, false);
		} else {
			outputMessageList.add("Setting output File Path: " + previousFilePath);
		}
	}

	/**
	 * convertPrices helper method to convert the daily price data into doubles
	 * 
	 */
	private void convertPrices(final List<String> dailyPriceData) {
		for (int i = 0; i < dataSize; i++) {
			try {
				dailyPriceDoubleValues[i] = Double.parseDouble(dailyPriceData.get(i));
			} catch (final NumberFormatException ex) {
				Logs.myLogger.error("Number Format Exception. Code 030. " + ex);
			}
		}
	}

	public double[] getResults() {
		return results;
	}

	public List<String> getGUITextOutputsFromComputeGrid() {
		return outputMessageList;
	}

	public RunContext getRunContext() {
		return runContext;
	}

	@SpaceId(autoGenerate = true)
	@SpaceRouting
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public List<String> getOutputMessageList() {
		return outputMessageList;
	}

	public void setOutputMessageList(final List<String> outputMessageList) {
		this.outputMessageList = outputMessageList;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(final String categoryName) {
		this.categoryName = categoryName;
	}

	public String getSelectionName() {
		return selectionName;
	}

	public void setSelectionName(final String selectionName) {
		this.selectionName = selectionName;
	}

	public String getPreviousFilePath() {
		return previousFilePath;
	}

	public void setPreviousFilePath(final String previousFilePath) {
		this.previousFilePath = previousFilePath;
	}

	public byte[] getPreviousFileBytes() {
		return previousFileBytes;
	}

	public void setPreviousFileBytes(final byte[] previousFileBytes) {
		this.previousFileBytes = previousFileBytes;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(final String filePath) {
		this.filePath = filePath;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(final String savePath) {
		this.savePath = savePath;
	}

	public String getOpenCLSrc() {
		return openCLSrc;
	}

	public void setOpenCLSrc(final String openCLSrc) {
		this.openCLSrc = openCLSrc;
	}

	@SpaceProperty(nullValue = "-1.0")
	public double getOmega() {
		return omega;
	}

	public void setOmega(final double omega) {
		this.omega = omega;
	}

	@SpaceProperty(nullValue = "-10000.0")
	public double getmCoeff() {
		return mCoeff;
	}

	public void setmCoeff(final double mCoeff) {
		this.mCoeff = mCoeff;
	}

	@SpaceProperty(nullValue = "-1.0")
	public double gettCrit() {
		return tCrit;
	}

	public void settCrit(final double tCrit) {
		this.tCrit = tCrit;
	}

	@SpaceProperty(nullValue = "-1")
	public int getDataSize() {
		return dataSize;
	}

	public void setDataSize(final int dataSize) {
		this.dataSize = dataSize;
	}

	public int[] getDailyPriceDateInt() {
		return dailyPriceDateInt;
	}

	public void setDailyPriceDateInt(final int[] dailyPriceDateInt) {
		this.dailyPriceDateInt = dailyPriceDateInt;
	}

	public double[] getDailyPriceDoubleValues() {
		return dailyPriceDoubleValues;
	}

	public void setDailyPriceDoubleValues(final double[] dailyPriceDoubleValues) {
		this.dailyPriceDoubleValues = dailyPriceDoubleValues;
	}

	@SpaceProperty(nullValue = "-1")
	public int getWindow() {
		return window;
	}

	public void setWindow(final int window) {
		this.window = window;
	}

	public void setResults(final double[] results) {
		this.results = results;
	}

	public void setRunContext(final RunContext runContext) {
		this.runContext = runContext;
	}

	@Override
	public String toString() {
		return "BubbleIndexGridTask [outputMessageList=" + outputMessageList + ", categoryName=" + categoryName
				+ ", selectionName=" + selectionName + ", previousFilePath=" + previousFilePath + ", previousFileBytes="
				+ Arrays.toString(previousFileBytes) + ", filePath=" + filePath + ", savePath=" + savePath
				+ ", openCLSrc=" + openCLSrc + ", omega=" + omega + ", mCoeff=" + mCoeff + ", tCrit=" + tCrit
				+ ", window=" + window + ", dataSize=" + dataSize + ", dailyPriceDateInt="
				+ Arrays.toString(dailyPriceDateInt) + ", results=" + Arrays.toString(results)
				+ ", dailyPriceDoubleValues=" + Arrays.toString(dailyPriceDoubleValues) + ", runContext=" + runContext
				+ ", id=" + id + "]";
	}
}
