package org.thebubbleindex.data;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.swing.UpdateWorker;

/**
 * URLS manages the download URL construction and the full data-retrieval
 * pipeline for a single financial time series. It supports three data sources:
 * Yahoo Finance, the Federal Reserve Economic Data (FRED) service, and Quandl.
 * <p>
 * A typical usage sequence is:
 * <ol>
 *   <li>Construct a {@code URLS} instance.</li>
 *   <li>Call the appropriate {@code set*Url()} method to configure the download
 *       URL.</li>
 *   <li>Call {@link #readURL_file(ByteArrayOutputStream)} to download the raw
 *       data into a byte stream.</li>
 *   <li>Call {@link #parseAndCleanDataStream} to parse and de-duplicate the
 *       downloaded data.</li>
 *   <li>Call {@link #updateData} to merge the new records into the existing
 *       local daily-data file.</li>
 * </ol>
 *
 * @author thebubbleindex
 */
public class URLS {
	/** Name of the daily data file appended to every selection folder. */
	public static final String dailyDataFile = "dailydata.csv";

	private String dataName;
	private String url;
	private String dataType;
	private String source;
	private boolean isYahooIndex;
	private String quandlDataset;
	private String quandlName;
	private int quandlColumn;
	private UpdateWorker updateWorker;
	private boolean overwrite;
	private String yahooCookie = null;
	private final Indices indices;
	private final RunContext runContext;

	/**
	 * URLS constructor.
	 *
	 * @param indices    application index configuration used to build local
	 *                   file paths
	 * @param runContext shared run-time state (stop flag, GUI mode, etc.)
	 */
	public URLS(final Indices indices, final RunContext runContext) {
		this.indices = indices;
		this.runContext = runContext;
	}

	/**
	 * setUpdateWorker sets the GUI worker used to publish progress messages
	 * during the download. May be {@code null} in headless (non-GUI) mode.
	 *
	 * @param updateWorker the GUI worker, or {@code null}
	 */
	public void setUpdateWorker(final UpdateWorker updateWorker) {
		this.updateWorker = updateWorker;
	}

	/**
	 * setDataName sets the selection name (ticker symbol or series identifier)
	 * used when constructing the data download URL.
	 *
	 * @param dataName the selection name
	 */
	public void setDataName(final String dataName) {
		this.dataName = dataName;
	}

	/**
	 * setYahooUrl constructs the Yahoo Finance v7 historical-data download URL
	 * for the configured selection. A session cookie is obtained from the Yahoo
	 * Finance lookup page and cached for subsequent calls. The URL covers all
	 * available history from the Unix epoch to today at daily interval.
	 */
	public void setYahooUrl() {

		final String yahooSymbol = isYahooIndex ? "%5E" : "";
		final HttpGet httpRequest = new HttpGet("https://finance.yahoo.com/lookup?s=rubbish");
		httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		if (yahooCookie == null) {
			final HttpClient client = HttpClientBuilder.create().build();

			HttpResponse response = null;
			try {
				response = client.execute(httpRequest);
			} catch (final IOException ex) {
				Logs.myLogger.error(ex);
			}

			if (response != null) {
				try {
					// Consume the entity body to allow the connection to be released
					EntityUtils.consume(response.getEntity());
				} catch (final IOException ex) {
					Logs.myLogger.error(ex);
				}

				final Header setCookieHeader = response.getFirstHeader("Set-Cookie");
				yahooCookie = setCookieHeader != null ? setCookieHeader.getValue().split(";")[0] : "cookie";
			}
		}

		if (yahooCookie == null) {
			Logs.myLogger.error("Failed to find valid cookie.");
			url = "";
			return;
		}

		final String unixStartDate = "0"; // 1/1/1970
		final String unixEndDate = String.valueOf(new Date().getTime()); // Today's
																			// date
		final String urlInterval = "1d";
		final String urlEvents = "history";
		url = "https://query1.finance.yahoo.com/v7/finance/download/" + yahooSymbol + dataName + "?period1="
				+ unixStartDate + "&period2=" + unixEndDate + "&interval=" + urlInterval + "&events=" + urlEvents
				+ "&includeAdjustedClose=true";
	}

	@Override
	public String toString() {
		return "URLS [dataName=" + dataName + ", url=" + url + ", dataType=" + dataType + ", source=" + source
				+ ", isYahooIndex=" + isYahooIndex + ", QuandlDataset=" + quandlDataset + ", QuandlName=" + quandlName
				+ ", QuandlColumn=" + quandlColumn + ", overwrite=" + overwrite + "]";
	}

	/**
	 * setFEDUrl constructs the FRED (Federal Reserve Economic Data) download
	 * URL for the configured data name.
	 */
	public void setFEDUrl() {
		url = "https://research.stlouisfed.org/fred2/series/" + dataName + "/downloaddata/" + dataName + ".csv";
	}

	/**
	 * setQuandlUrl constructs the Quandl API v3 download URL for the given
	 * dataset and series. If a non-empty API key is provided it is appended to
	 * the URL.
	 *
	 * @param dataset   the Quandl dataset code (e.g. "WIKI")
	 * @param name      the data series name within the dataset (e.g. "AAPL")
	 * @param quandlKey the Quandl API key, or an empty string for anonymous
	 *                  access
	 */
	public void setQuandlUrl(final String dataset, final String name, final String quandlKey) {
		this.quandlDataset = dataset;
		this.quandlName = name;
		url = "https://www.quandl.com/api/v3/datasets/" + quandlDataset + "/" + quandlName + "/data.csv?sort_order=asc";
		if (quandlKey.trim().length() > 0) {
			url = url + "&api_key=" + quandlKey.trim();
		}
	}

	/**
	 * setQuandlColumn sets the one-based column index that contains the price
	 * value in the Quandl CSV response.
	 *
	 * @param Column the one-based column index to read (e.g. 2 for the second
	 *               column)
	 */
	public void setQuandlColumn(final int Column) {
		this.quandlColumn = Column;
	}

	/**
	 * setYahooIndex sets whether the selection is a Yahoo Finance index symbol
	 * (e.g. "^GSPC"). Index symbols require the "%5E" URL encoding prefix.
	 *
	 * @param isYahooIndex {@code true} if the selection is a Yahoo index symbol
	 */
	public void setYahooIndex(final boolean isYahooIndex) {
		this.isYahooIndex = isYahooIndex;
	}

	/**
	 * setDataType sets the category name, which is used to build the local
	 * file system path where the data will be stored.
	 *
	 * @param dataType the category name (e.g. "Currencies")
	 */
	public void setDataType(final String dataType) {
		this.dataType = dataType;
	}

	/**
	 * setSource sets the data source identifier. Supported values are
	 * {@code "YAHOO"}, {@code "QUANDL"}, and {@code "FED"} (case-insensitive).
	 *
	 * @param source the data source identifier string
	 */
	public void setSource(final String source) {
		this.source = source;
	}

	/**
	 * getSource returns the data source identifier string.
	 *
	 * @return the data source identifier (e.g. "YAHOO", "QUANDL", or "FED")
	 */
	public String getSource() {
		return this.source;
	}

	/**
	 * setOverwrite controls whether an existing local daily-data file should be
	 * replaced entirely. When {@code false} only new records that post-date the
	 * last entry in the existing file are appended.
	 *
	 * @param overwrite {@code true} to delete and recreate the local file
	 */
	public void setOverwrite(final Boolean overwrite) {
		this.overwrite = overwrite;
	}

	/**
	 * readURL_file downloads the raw data from the configured URL and writes it
	 * to the provided output stream. For Yahoo Finance requests, the cached
	 * session cookie is included in the HTTP request header.
	 *
	 * @param outputstream the stream that will receive the raw downloaded bytes
	 * @throws IOException if the HTTP connection fails or an I/O error occurs
	 *                     while reading or writing the data
	 */
	public void readURL_file(final ByteArrayOutputStream outputstream) throws IOException {
		if (runContext.isGUI()) {
			updateWorker.publishText("GET: " + dataName);
		} else {
			System.out.println("GET: " + dataName);
		}
		if (runContext.isGUI()) {
			updateWorker.publishText("Downloading file: " + dataName + ". Connecting to: " + url + " ...");
		} else {
			System.out.println("Downloading file: " + dataName + ". Connecting to: " + url + " ...");
		}

		if (source.equalsIgnoreCase("Yahoo")) {
			final HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Cookie", yahooCookie);
			final HttpClient client = HttpClientBuilder.create().build();

			// Execute the request
			final HttpResponse response = client.execute(httpGet);

			final byte[] result = EntityUtils.toByteArray(response.getEntity());
			final ReadableByteChannel rbc = Channels.newChannel(new ByteArrayInputStream(result));
			final WritableByteChannel outputChannel = Channels.newChannel(outputstream);
			fastChannelCopy(rbc, outputChannel);
			rbc.close();
			outputChannel.close();
		} else {
			final URL urlFile = new URL(url);
			final ReadableByteChannel rbc = Channels.newChannel(urlFile.openStream());
			final WritableByteChannel outputChannel = Channels.newChannel(outputstream);
			fastChannelCopy(rbc, outputChannel);
			rbc.close();
			outputChannel.close();
		}
	}

	/**
	 * fastChannelCopy efficiently copies all bytes from a readable NIO channel
	 * to a writable NIO channel using a direct 16 KB buffer.
	 * <p>
	 * Based on:
	 * https://thomaswabner.wordpress.com/2007/10/09/fast-stream-copy-using-javanio-channels/
	 *
	 * @param src  the source channel to read from
	 * @param dest the destination channel to write to
	 * @throws IOException if an I/O error occurs during the transfer
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
	 * parseAndCleanDataStream parses the raw byte stream downloaded from the
	 * data source, de-duplicates records by date, and populates two parallel
	 * lists with the cleaned date and price strings sorted in chronological
	 * order.
	 * <p>
	 * The column index used to read the price value depends on the configured
	 * data source:
	 * <ul>
	 *   <li>Yahoo: adjusted-close column (index 5)</li>
	 *   <li>Quandl: the column specified by {@link #setQuandlColumn(int)}</li>
	 *   <li>FED: column index 1</li>
	 * </ul>
	 *
	 * @param outputstream the raw downloaded data as a byte stream
	 * @param dateData     output list that will be populated with date strings
	 *                     in "yyyy-MM-dd" format
	 * @param priceData    output list that will be populated with price value
	 *                     strings corresponding to each date
	 * @throws IOException if the byte stream cannot be read or if it contains
	 *                     unrecoverable parse errors
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
				final TreeMap<LocalDate, Double> dateValueMap = new TreeMap<LocalDate, Double>();

				// Ignore header is True for Yahoo, FED, and QUANDL
				String line = reader.readLine();

				while ((line = reader.readLine()) != null) {
					final String[] splits = line.split(",|\\t");
					if (splits == null || splits.length <= 0) {
						continue;
					}

					final LocalDate date = LocalDate.parse(splits[0]);

					if (dateValueMap.containsKey(date)) {
						continue;
					}

					if (YAHOO && splits.length > 6) {
						try {
							dateValueMap.put(date, Double.parseDouble(splits[5]));
						} catch (final NumberFormatException ex) {
							Logs.myLogger.error("Failed to write line: {}. Category Name = {}. Selection Name = {}.",
									line, dataType, dataName);
						}
					} else if (QUANDL && splits.length > quandlColumn - 1) {
						try {
							dateValueMap.put(date, Double.parseDouble(splits[quandlColumn - 1]));
						} catch (final NumberFormatException ex) {
							Logs.myLogger.error("Failed to write line: {}. Category Name = {}. Selection Name = {}.",
									line, dataType, dataName);
						}
					} else {
						if (splits.length > 1 && !splits[1].equals(".")) {
							try {
								dateValueMap.put(date, Double.parseDouble(splits[1]));
							} catch (final NumberFormatException ex) {
								Logs.myLogger.error(
										"Failed to write line: {}. Category Name = {}. Selection Name = {}.", line,
										dataType, dataName);
							}
						}
					}
				}

				for (final Entry<LocalDate, Double> entry : dateValueMap.entrySet()) {
					priceData.add(String.valueOf(entry.getValue()));
					dateData.add(entry.getKey().toString());
				}

				/*
				 * Reverse Yahoo Data - No longer need to reverse the order as
				 * of new Yahoo Crumb API - July 2017 if (YAHOO) {
				 * Collections.reverse(dateData);
				 * Collections.reverse(priceData); }
				 */
			}
		} catch (final IOException x) {
			Logs.myLogger.error("Failed to write CSV file. Category Name = {}, Selection Name = {}. {}", dataType,
					dataName, x);
			if (runContext.isGUI()) {
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
	 * updateData merges the freshly downloaded date and price records into the
	 * existing local daily-data file. If no local file exists a new one is
	 * created. If {@code overwrite} is set to {@code true} any existing file is
	 * deleted first and replaced with the new data. Otherwise only records that
	 * post-date the last entry in the existing file are appended.
	 *
	 * @param dateData  the cleaned list of date strings in chronological order
	 * @param priceData the cleaned list of price value strings corresponding to
	 *                  each date
	 * @throws IOException if the local data file cannot be read, created, or
	 *                     written
	 */
	public void updateData(final List<String> dateData, final List<String> priceData) throws IOException {

		final List<String> oldpriceData = new ArrayList<String>(1000);
		final List<String> olddateData = new ArrayList<String>(1000);

		final Path filepath = new File(indices.getUserDir() + indices.getProgramDataFolder()
				+ indices.getFilePathSymbol() + dataType + indices.getFilePathSymbol() + dataName
				+ indices.getFilePathSymbol() + dataName + dailyDataFile).toPath();

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

			final File dailydata = new File(indices.getUserDir() + indices.getProgramDataFolder()
					+ indices.getFilePathSymbol() + dataType + indices.getFilePathSymbol() + dataName
					+ indices.getFilePathSymbol() + dataName + dailyDataFile);

			dailydata.createNewFile();

			try (final FileWriter writer = new FileWriter(dailydata)) {
				for (int i = 0; i < olddateData.size(); i++) {
					writer.write(String.format("%s\t%s%n", olddateData.get(i), oldpriceData.get(i)));
				}

				if (match > 0) {
					for (int i = match + 1; i < dateData.size(); i++) {
						writer.write(String.format("%s\t%s%n", dateData.get(i), priceData.get(i)));
					}
				}
				writer.flush();
			}

		} else {

			try {
				final File dailydata = new File(indices.getUserDir() + indices.getProgramDataFolder()
						+ indices.getFilePathSymbol() + dataType + indices.getFilePathSymbol() + dataName
						+ indices.getFilePathSymbol() + dataName + dailyDataFile);

				new File(indices.getUserDir() + indices.getProgramDataFolder() + indices.getFilePathSymbol() + dataType
						+ indices.getFilePathSymbol() + dataName + indices.getFilePathSymbol()).mkdirs();

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
				if (runContext.isGUI()) {
					updateWorker.publishText("Failed to create daily data: " + dataName);
				} else {
					System.out.println("Failed to create daily data: " + dataName);
				}
				throw new IOException("Failed to create daily data: " + dataName);
			}
		}
	}
}
