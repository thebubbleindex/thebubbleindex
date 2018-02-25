package org.thebubbleindex.driver;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.thebubbleindex.exception.FailedToRunIndex;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.plot.BubbleIndexPlot;
import org.thebubbleindex.plot.DerivativePlot;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.runnable.RunIndex;
import org.thebubbleindex.swing.BubbleIndexWorker;
import org.thebubbleindex.util.Utilities;

/**
 * BubbleIndex class is the central logic component of the application. Provides
 * variable initialization, reads input files and stores results obtained in the
 * Run for a single time window.
 * 
 * @author thebubbleindex
 */
public class BubbleIndex implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6695607232313746483L;
	private final String categoryName;
	private final String selectionName;

	private String previousFilePath;
	private String filePath;
	private String savePath;
	private final String openCLSrc;

	private final double omega;
	private final double mCoeff;
	private final double tCrit;
	private final int window;
	private final int dataSize;

	private final List<String> dailyPriceData;
	private final List<String> dailyPriceDate;
	private final List<Double> results;

	private final double[] dailyPriceDoubleValues;
	private final Indices indices;
	private final RunContext runContext;

	/**
	 * BubbleIndex constructor
	 * 
	 * @param omega
	 * @param mCoeff
	 * @param tCrit
	 * @param window
	 * @param categoryName
	 * @param selectionName
	 */
	public BubbleIndex(final double omega, final double mCoeff, final double tCrit, final int window,
			final String categoryName, final String selectionName, final DailyDataCache dailyDataCache,
			final Indices indices, final String openCLSrc, final RunContext runContext) {

		Logs.myLogger
				.info("Initializing The Bubble Index. Category Name = {}, Selection Name = {}, Omega = {}, M = {}, TCrit = {}, "
						+ "Window = {}", categoryName, selectionName, omega, mCoeff, tCrit, window);

		this.omega = omega;
		this.mCoeff = mCoeff;
		this.tCrit = tCrit;
		this.window = window;
		this.categoryName = categoryName;
		this.selectionName = selectionName;
		this.indices = indices;
		this.openCLSrc = openCLSrc;
		this.runContext = runContext;

		if (!runContext.isStop())
			setFilePaths();

		if (dailyDataCache.getSelectionName().equals(this.selectionName)) {
			dailyPriceData = dailyDataCache.getDailyPriceData();
			dailyPriceDate = dailyDataCache.getDailyPriceDate();

			dataSize = dailyPriceData.size();

			dailyPriceDoubleValues = dailyDataCache.getDailyPriceDoubleValues();

			results = new ArrayList<Double>(dataSize);

		} else {
			dailyPriceData = new ArrayList<String>(10000);
			dailyPriceDate = new ArrayList<String>(10000);

			if (!runContext.isStop())
				Utilities.ReadValues(filePath, dailyPriceDate, dailyPriceData, false, false);

			dailyDataCache.setSelectionName(this.selectionName);
			dailyDataCache.setDailyPriceData(new ArrayList<String>(dailyPriceData));
			dailyDataCache.setDailyPriceDate(new ArrayList<String>(dailyPriceDate));

			dataSize = dailyPriceData.size();

			dailyPriceDoubleValues = new double[dataSize];

			if (!runContext.isStop())
				convertPrices();

			dailyDataCache.setDailyPriceDoubleValues(dailyPriceDoubleValues);

			results = new ArrayList<Double>(dataSize);
		}
	}

	/**
	 * BubbleIndex constructor, typically used only in the case where plotting
	 * proceeds this call.
	 * 
	 * @param categoryName
	 * @param selectionName
	 */
	public BubbleIndex(final String categoryName, final String selectionName, final DailyDataCache dailyDataCache,
			final Indices indices, final String openCLSrc, final RunContext runContext) {
		Logs.myLogger.info("Initializing The Bubble Index. Category Name = {}, Selection Name = {}", categoryName,
				selectionName);

		omega = 0.0;
		mCoeff = 0.0;
		window = 0;
		tCrit = 0.0;

		this.categoryName = categoryName;
		this.selectionName = selectionName;
		this.indices = indices;
		this.openCLSrc = openCLSrc;
		this.runContext = runContext;

		if (!runContext.isStop())
			setFilePaths();

		dailyPriceData = new ArrayList<String>();
		dailyPriceDate = new ArrayList<String>();
		results = new ArrayList<Double>();

		if (!runContext.isStop())
			Utilities.ReadValues(filePath, dailyPriceDate, dailyPriceData, false, false);
		dataSize = dailyPriceData.size();

		dailyPriceDoubleValues = new double[dataSize];

		if (!runContext.isStop())
			convertPrices();

	}

	/**
	 * runBubbleIndex Core run method. Provides a GPU and CPU version. Catches
	 * any errors which the Run methods may throw.
	 * 
	 * @param bubbleIndexWorker
	 */
	public void runBubbleIndex(final BubbleIndexWorker bubbleIndexWorker) {
		if (dataSize > window) {
			final RunIndex runIndex = new RunIndex(bubbleIndexWorker, dailyPriceDoubleValues, dataSize, window, results,
					dailyPriceDate, previousFilePath, selectionName, omega, mCoeff, tCrit, indices, openCLSrc,
					runContext);

			if (!runContext.isForceCPU()) {
				try {
					Logs.myLogger.info("Executing GPU Run. Category Name = {}, Selection Name = {}", categoryName,
							selectionName);
					runIndex.execIndexWithGPU();
				} catch (final FailedToRunIndex er) {
					Logs.myLogger.info("Category Name = {}, Selection Name = {}, Window = {}. {}", categoryName,
							selectionName, window, er);
					if (runContext.isGUI()) {
						bubbleIndexWorker.publishText(er.getMessage());
					} else {
						System.out.println(er.getMessage());
					}
					results.clear();
				}
			} else {
				try {
					Logs.myLogger.info("Executing CPU Run. Category Name = {}, Selection Name = {}", categoryName,
							selectionName);
					runIndex.execIndexWithCPU();
				} catch (final FailedToRunIndex er) {
					Logs.myLogger.error("Category Name = {}, Selection Name = {}, Window = {}. {}", categoryName,
							selectionName, window, er);
					if (runContext.isGUI()) {
						bubbleIndexWorker.publishText("Error: " + er);
					} else {
						System.out.println("Error: " + er);
					}
					results.clear();
				}
			}
		}
	}

	/**
	 * outputResults saves the results of the run to the savePath
	 * 
	 * @param bubbleIndexWorker
	 */
	public void outputResults(final BubbleIndexWorker bubbleIndexWorker) {
		if (dataSize > window) {
			if (!results.isEmpty()) {

				final String Name = selectionName + window + "days.csv";

				if (runContext.isGUI()) {
					bubbleIndexWorker.publishText("Writing output file.");
				} else {
					System.out.println("Writing output file.");
				}
				try {
					Logs.myLogger.info("Writing output file: {}", previousFilePath);

					Utilities.WriteCSV(savePath, results, dataSize - window, Name, dailyPriceDate,
							new File(previousFilePath).exists());
				} catch (final IOException ex) {
					Logs.myLogger.error("Failed to write csv output. Save path = {}. {}", savePath, ex);
				}
			}
		}
	}

	/**
	 * plot creates and displays the results of The Bubble Index for the first
	 * four windows of the windowString.
	 * <p>
	 * There is the option to provide custom beginning and end dates from the
	 * GUI.
	 * 
	 * @param bubbleIndexWorker
	 * 
	 * @param windowsString
	 * @param begDate
	 * @param endDate
	 * @param isCustomRange
	 */
	public void plot(final BubbleIndexWorker bubbleIndexWorker, final String windowsString, final Date begDate,
			final Date endDate, final boolean isCustomRange) {
		Logs.myLogger.info(
				"Plotting. Category Name = {}, Selection Name = {}, Windows = {}," + " BegDate = {}, EndDate = {}",
				categoryName, selectionName, windowsString, begDate.toString(), endDate.toString());
		new BubbleIndexPlot(bubbleIndexWorker, categoryName, selectionName, windowsString, begDate, endDate,
				isCustomRange, dailyPriceData, dailyPriceDate, indices, runContext);
		new DerivativePlot(bubbleIndexWorker, categoryName, selectionName, windowsString, begDate, endDate,
				isCustomRange, dailyPriceData, dailyPriceDate, indices, runContext);

	}

	/**
	 * setFilePaths helper method to create the file paths which contain the
	 * daily data and any previously existing runs.
	 * 
	 */
	private void setFilePaths() {

		filePath = indices.getUserDir() + indices.getProgramDataFolder() + indices.getFilePathSymbol() + categoryName
				+ indices.getFilePathSymbol() + selectionName + indices.getFilePathSymbol() + selectionName
				+ "dailydata.csv";

		savePath = indices.getUserDir() + indices.getProgramDataFolder() + indices.getFilePathSymbol() + categoryName
				+ indices.getFilePathSymbol() + selectionName + indices.getFilePathSymbol();

		previousFilePath = indices.getUserDir() + indices.getProgramDataFolder() + indices.getFilePathSymbol()
				+ categoryName + indices.getFilePathSymbol() + selectionName + indices.getFilePathSymbol()
				+ selectionName + Integer.toString(window) + "days.csv";

		Utilities.displayOutput(runContext, "Output File Path: " + previousFilePath, false);
	}

	/**
	 * convertPrices helper method to convert the daily price data into doubles
	 * 
	 */
	private void convertPrices() {
		for (int i = 0; i < dataSize; i++) {
			try {
				dailyPriceDoubleValues[i] = Double.parseDouble(dailyPriceData.get(i));
			} catch (final NumberFormatException ex) {
				Logs.myLogger.error("Number Format Exception. Code 030. " + ex);
			}
		}
	}

	public int getWindow() {
		return window;
	}

	public List<Double> getResults() {
		return results;
	}
}
