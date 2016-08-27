package org.thebubbleindex.data;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.swing.UpdateWorker;

/**
 *
 * @author bigttrott
 */
public class URLS {
	private static final String todaysYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
	public static final String dailyDataFile = "dailydata.csv";

	private String dataName;
	private String url;
	private String dataType;
	private String source;
	private final int startYear = 1900;
	private boolean isYahooIndex;
	private String quandlDataset;
	private String quandlName;
	private int quandlColumn;
	private UpdateWorker updateWorker;
	private boolean overwrite;

	public void setUpdateWorker(final UpdateWorker updateWorker) {
		this.updateWorker = updateWorker;
	}

	/**
	 * 
	 * @param dataName
	 */
	public void setDataName(final String dataName) {
		this.dataName = dataName;
	}

	/**
	 * 
	 */
	public void setYahooUrl() {
		final String yahooSymbol = isYahooIndex ? "%5E" : "";
		url = "http://ichart.yahoo.com/table.csv?s=" + yahooSymbol + dataName + "&a=0&b=1&c=" + startYear
				+ "&d=11&e=31&f=" + todaysYear + "&g=d&ignore=.csv";
	}

	@Override
	public String toString() {
		return "URLS [dataName=" + dataName + ", url=" + url + ", dataType=" + dataType + ", source=" + source
				+ ", isYahooIndex=" + isYahooIndex + ", QuandlDataset=" + quandlDataset + ", QuandlName=" + quandlName
				+ ", QuandlColumn=" + quandlColumn + ", overwrite=" + overwrite + "]";
	}

	/**
	 * 
	 */
	public void setFEDUrl() {
		url = "https://research.stlouisfed.org/fred2/series/" + dataName + "/downloaddata/" + dataName + ".csv";
	}

	/**
	 * 
	 * @param dataset
	 * @param name
	 * @param quandlKey
	 */
	public void setQuandlUrl(final String dataset, final String name, final String quandlKey) {
		this.quandlDataset = dataset;
		this.quandlName = name;
		url = "https://www.quandl.com/api/v1/datasets/" + quandlDataset + "/" + quandlName + ".csv?sort_order=asc";
		if (quandlKey.trim().length() > 0) {
			url = url + "&api_key=" + quandlKey.trim();
		}
	}

	/**
	 * 
	 * @param Column
	 */
	public void setQuandlColumn(final int Column) {
		this.quandlColumn = Column;
	}

	/**
	 * 
	 */
	public void setYahooIndex(final boolean isYahooIndex) {
		this.isYahooIndex = isYahooIndex;
	}

	/**
	 * 
	 * @param dataType
	 */
	public void setDataType(final String dataType) {
		this.dataType = dataType;
	}

	/**
	 * 
	 * @param source
	 */
	public void setSource(final String source) {
		this.source = source;
	}

	/**
	 * 
	 * @return
	 */
	public String getSource() {
		return this.source;
	}

	/**
	 * 
	 * @param overwrite
	 */
	public void setOverwrite(final Boolean overwrite) {
		this.overwrite = overwrite;
	}

	/**
	 * 
	 * @param outputstream
	 * @throws IOException
	 */
	public void readURL_file(final ByteArrayOutputStream outputstream) throws IOException {
		if (RunContext.isGUI) {
			updateWorker.publishText("GET: " + dataName);
		} else {
			System.out.println("GET: " + dataName);
		}
		if (RunContext.isGUI) {
			updateWorker.publishText("Downloading file: " + dataName + ". Connecting to: " + url + " ...");
		} else {
			System.out.println("Downloading file: " + dataName + ". Connecting to: " + url + " ...");
		}
		final URL urlFile = new URL(url);
		final ReadableByteChannel rbc = Channels.newChannel(urlFile.openStream());
		final WritableByteChannel outputChannel = Channels.newChannel(outputstream);
		fastChannelCopy(rbc, outputChannel);
		rbc.close();
		outputChannel.close();
	}

	/**
	 * Source:
	 * https://thomaswabner.wordpress.com/2007/10/09/fast-stream-copy-using-javanio-channels/
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void fastChannelCopy(final ReadableByteChannel src, final WritableByteChannel dest)
			throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
		while (src.read(buffer) != -1) {
			// prepare the buffer to be drained
			buffer.flip();
			// write to the channel, may block
			dest.write(buffer);
			// If partial transfer, shift remainder down
			// If buffer is empty, same as doing clear()
			buffer.compact();
		}
		// EOF will leave buffer in fill state
		buffer.flip();
		// make sure the buffer is fully drained.
		while (buffer.hasRemaining()) {
			dest.write(buffer);
		}
	}

	/**
	 * 
	 * @param outputstream
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void parseAndCleanDataStream(final ByteArrayOutputStream outputstream, final List<String> dateData,
			final List<String> priceData) throws IOException {
		boolean YAHOO = false;
		boolean QUANDL = false;

		if (source.equalsIgnoreCase("Yahoo")) {
			YAHOO = true;
		} else if (source.equalsIgnoreCase("QUANDL")) {
			QUANDL = true;
		}
		InputStream is = null;
		try {
			final byte[] content = outputstream.toByteArray();
			is = new ByteArrayInputStream(content);

			try (final BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

				// ignore the lines with the date repeats (common in yahoo data)
				final Set<String> dateSet = new HashSet<String>(1000);

				// Ignore header is True for Yahoo, FED, and QUANDL
				String line = reader.readLine();

				while ((line = reader.readLine()) != null) {
					final String[] splits = line.split(",|\\t");
					// TODO : check if a date is out of order
					if (splits.length > 0 && dateSet.contains(splits[0])) {
						continue;
					}
					if (YAHOO && splits.length > 6) {
						try {
							priceData.add(String.valueOf(Double.parseDouble(splits[6])));
							dateData.add(splits[0]);
							dateSet.add(splits[0]);
						} catch (final NumberFormatException ex) {
							Logs.myLogger.error("Failed to write line: {}. Category Name = {}. Selection Name = {}.",
									line, dataType, dataName);
						}
					} else if (QUANDL && splits.length > quandlColumn - 1) {
						try {
							priceData.add(String.valueOf(Double.parseDouble(splits[quandlColumn - 1])));
							dateData.add(splits[0]);
							dateSet.add(splits[0]);
						} catch (final NumberFormatException ex) {
							Logs.myLogger.error("Failed to write line: {}. Category Name = {}. Selection Name = {}.",
									line, dataType, dataName);
						}
					} else {
						if (splits.length > 1 && !splits[1].equals(".")) {
							try {
								priceData.add(String.valueOf(Double.parseDouble(splits[1])));
								dateData.add(splits[0]);
								dateSet.add(splits[0]);
							} catch (final NumberFormatException ex) {
								Logs.myLogger.error(
										"Failed to write line: {}. Category Name = {}. Selection Name = {}.", line,
										dataType, dataName);
							}
						}
					}
				}

				if (YAHOO) {
					// Reverse Yahoo Data
					Collections.reverse(dateData);
					Collections.reverse(priceData);
				}
				// write data
			}
		} catch (final IOException x) {
			Logs.myLogger.error("Failed to write CSV file. Category Name = {}, Selection Name = {}. {}", dataType,
					dataName, x);
			if (RunContext.isGUI) {
				updateWorker.publishText("Failed to process CSV file: " + dataName);
			} else {
				System.out.println("Failed to process CSV file: " + dataName);
			}
			throw new IOException("Failed to process CSV file: " + dataName);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	/**
	 * 
	 * @param dateData
	 * @param priceData
	 * @throws IOException
	 */
	public void updateData(final List<String> dateData, final List<String> priceData) throws IOException {

		final List<String> oldpriceData = new ArrayList<String>(1000);
		final List<String> olddateData = new ArrayList<String>(1000);

		final Path filepath = new File(Indices.userDir + Indices.programDataFolder + Indices.filePathSymbol + dataType
				+ Indices.filePathSymbol + dataName + Indices.filePathSymbol + dataName + dailyDataFile).toPath();

		if (overwrite) {
			if (Files.exists(filepath)) {
				Files.delete(filepath);
			}
		}

		if (Files.exists(filepath)) {

			// No Header
			try (final BufferedReader reader = Files.newBufferedReader(filepath)) {
				// No Header
				String line; // = reader.readLine();

				// All daily data is tab separated .tsv file really incorrect to
				// name .csv
				while ((line = reader.readLine()) != null) {
					final String[] splits = line.split("\t");
					olddateData.add(splits[0]);
					oldpriceData.add(splits[1]);
				}
			}

			// find where dates match
			final String oldDateDataLastString = olddateData.get(olddateData.size() - 1);

			int match = 0;
			for (int i = 0; i < dateData.size(); i++) {
				if (oldDateDataLastString.equals(dateData.get(i))) {
					match = i;
				}
			}

			final File dailydata = new File(Indices.userDir + Indices.programDataFolder + Indices.filePathSymbol
					+ dataType + Indices.filePathSymbol + dataName + Indices.filePathSymbol + dataName + dailyDataFile);

			dailydata.createNewFile();

			final FileWriter writer = new FileWriter(dailydata);

			for (int i = 0; i < olddateData.size(); i++) {
				writer.write(String.format("%s\t%s%n", olddateData.get(i), oldpriceData.get(i)));
			}

			if (match > 0) {
				for (int i = match + 1; i < dateData.size(); i++) {
					writer.write(String.format("%s\t%s%n", dateData.get(i), priceData.get(i)));
				}
			}
			writer.flush();
			writer.close();

		} else {

			try {
				final File dailydata = new File(Indices.userDir + Indices.programDataFolder + Indices.filePathSymbol
						+ dataType + Indices.filePathSymbol + dataName + Indices.filePathSymbol + dataName
						+ dailyDataFile);

				new File(Indices.userDir + Indices.programDataFolder + Indices.filePathSymbol + dataType
						+ Indices.filePathSymbol + dataName + Indices.filePathSymbol).mkdirs();

				dailydata.createNewFile();

				try (final FileWriter writer = new FileWriter(dailydata)) {
					for (int i = 0; i < dateData.size(); i++) {
						writer.write(String.format("%s\t%s%n", dateData.get(i), priceData.get(i)));
					}
					writer.flush();
				}

			} catch (final IOException th) {
				Logs.myLogger.error("Failed to create daily data. Category Name = {}, Selection Name = {}. {}",
						dataType, dataName, th);
				if (RunContext.isGUI) {
					updateWorker.publishText("Failed to create daily data: " + dataName);
				} else {
					System.out.println("Failed to create daily data: " + dataName);
				}
				throw new IOException("Failed to create daily data: " + dataName);
			}
		}
	}
}
