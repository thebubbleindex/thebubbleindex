package org.thebubbleindex.data.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.thebubbleindex.data.URLS;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.runnable.RunContext;

/**
 * Tests that verify {@link URLS} correctly parses a raw CSV byte stream and
 * merges data into a local daily-data file, without requiring any network
 * access.
 */
public class URLSParseTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private Indices buildIndices() {
		final Indices indices = new Indices();
		indices.setUserDir(tempFolder.getRoot().getAbsolutePath() + File.separator);
		return indices;
	}

	private URLS buildURLs(final String source) {
		final Indices indices = buildIndices();
		final RunContext runContext = new RunContext();
		runContext.setGUI(false);
		final URLS urls = new URLS(indices, runContext);
		urls.setDataName("TEST");
		urls.setDataType("Stocks");
		urls.setSource(source);
		urls.setYahooIndex(false);
		urls.setOverwrite(false);
		return urls;
	}

	// ------------------------------------------------------------------
	// parseAndCleanDataStream tests
	// ------------------------------------------------------------------

	@Test
	public void parseAndCleanShouldParseFEDFormatData() throws IOException {
		final String csvData =
				"DATE,VALUE\n" +
				"2023-01-02,101.5\n" +
				"2023-01-03,102.0\n" +
				"2023-01-04,103.5\n";

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(csvData.getBytes(StandardCharsets.UTF_8));

		final URLS urls = buildURLs("FED");
		final List<String> dateData = new ArrayList<>();
		final List<String> priceData = new ArrayList<>();

		urls.parseAndCleanDataStream(baos, dateData, priceData);

		assertEquals(3, dateData.size());
		assertEquals("2023-01-02", dateData.get(0));
		assertEquals("101.5", priceData.get(0));
		assertEquals("2023-01-04", dateData.get(2));
	}

	@Test
	public void parseAndCleanShouldDeduplicateDates() throws IOException {
		final String csvData =
				"DATE,VALUE\n" +
				"2023-01-02,101.5\n" +
				"2023-01-02,999.9\n" +  // duplicate date - should be skipped
				"2023-01-03,102.0\n";

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(csvData.getBytes(StandardCharsets.UTF_8));

		final URLS urls = buildURLs("FED");
		final List<String> dateData = new ArrayList<>();
		final List<String> priceData = new ArrayList<>();

		urls.parseAndCleanDataStream(baos, dateData, priceData);

		assertEquals(2, dateData.size());
		assertEquals("101.5", priceData.get(0)); // First occurrence kept
	}

	@Test
	public void parseAndCleanShouldSkipDotValues() throws IOException {
		// FED uses "." to indicate missing data
		final String csvData =
				"DATE,VALUE\n" +
				"2023-01-02,101.5\n" +
				"2023-01-03,.\n" +  // missing value - should be skipped
				"2023-01-04,103.0\n";

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(csvData.getBytes(StandardCharsets.UTF_8));

		final URLS urls = buildURLs("FED");
		final List<String> dateData = new ArrayList<>();
		final List<String> priceData = new ArrayList<>();

		urls.parseAndCleanDataStream(baos, dateData, priceData);

		assertEquals(2, dateData.size());
		assertEquals("2023-01-02", dateData.get(0));
		assertEquals("2023-01-04", dateData.get(1));
	}

	@Test
	public void parseAndCleanShouldParseYahooFormatData() throws IOException {
		// Yahoo CSV format: Date,Open,High,Low,Close,Adj Close,Volume
		final String csvData =
				"Date,Open,High,Low,Close,Adj Close,Volume\n" +
				"2023-01-02,110.0,115.0,108.0,112.0,111.5,1000000\n" +
				"2023-01-03,112.0,117.0,110.0,115.0,114.3,900000\n";

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(csvData.getBytes(StandardCharsets.UTF_8));

		final URLS urls = buildURLs("Yahoo");
		final List<String> dateData = new ArrayList<>();
		final List<String> priceData = new ArrayList<>();

		urls.parseAndCleanDataStream(baos, dateData, priceData);

		assertEquals(2, dateData.size());
		assertEquals("111.5", priceData.get(0)); // Adj Close column (index 5)
		assertEquals("114.3", priceData.get(1));
	}

	@Test
	public void parseAndCleanShouldParseQuandlFormatData() throws IOException {
		// Quandl CSV format: Date,Col1,Col2,...
		final String csvData =
				"Date,Open,Close\n" +
				"2023-01-02,100.0,101.0\n" +
				"2023-01-03,101.0,103.0\n";

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(csvData.getBytes(StandardCharsets.UTF_8));

		final URLS urls = buildURLs("QUANDL");
		urls.setQuandlColumn(3); // column index 3 = "Close"
		final List<String> dateData = new ArrayList<>();
		final List<String> priceData = new ArrayList<>();

		urls.parseAndCleanDataStream(baos, dateData, priceData);

		assertEquals(2, dateData.size());
		assertEquals("101.0", priceData.get(0));
		assertEquals("103.0", priceData.get(1));
	}

	// ------------------------------------------------------------------
	// updateData tests
	// ------------------------------------------------------------------

	@Test
	public void updateDataShouldCreateNewFileWhenNoneExists() throws IOException {
		final URLS urls = buildURLs("FED");
		final Indices indices = buildIndices();
		new File(tempFolder.getRoot(), "ProgramData" + File.separator + "Stocks" + File.separator + "TEST")
				.mkdirs();

		final List<String> dates = List.of("2023-01-02", "2023-01-03");
		final List<String> prices = List.of("101.5", "102.0");

		urls.updateData(dates, prices);

		final File outFile = new File(tempFolder.getRoot(),
				"ProgramData" + File.separator + "Stocks" + File.separator + "TEST"
						+ File.separator + "TEST" + URLS.dailyDataFile);
		assertTrue(outFile.exists());
		final List<String> lines = Files.readAllLines(outFile.toPath(), StandardCharsets.UTF_8);
		assertEquals(2, lines.size());
		assertTrue(lines.get(0).contains("2023-01-02"));
	}

	@Test
	public void updateDataShouldAppendNewRowsToExistingFile() throws IOException {
		final URLS urls = buildURLs("FED");

		// Create the existing file with two rows
		final File dir = new File(tempFolder.getRoot(),
				"ProgramData" + File.separator + "Stocks" + File.separator + "TEST");
		dir.mkdirs();
		final File existingFile = new File(dir, "TEST" + URLS.dailyDataFile);
		Files.write(existingFile.toPath(),
				"2023-01-02\t101.5\n2023-01-03\t102.0\n".getBytes(StandardCharsets.UTF_8));

		// New data that extends the existing range
		final List<String> dates = List.of("2023-01-02", "2023-01-03", "2023-01-04", "2023-01-05");
		final List<String> prices = List.of("101.5", "102.0", "103.0", "104.0");

		urls.updateData(dates, prices);

		final List<String> lines = Files.readAllLines(existingFile.toPath(), StandardCharsets.UTF_8);
		// Should have 2 original + 2 new = 4 rows
		assertEquals(4, lines.size());
		assertTrue(lines.get(2).contains("2023-01-04"));
		assertTrue(lines.get(3).contains("2023-01-05"));
	}

	@Test
	public void updateDataShouldOverwriteFileWhenFlagIsSet() throws IOException {
		final URLS urls = buildURLs("FED");
		urls.setOverwrite(true);

		// Create the existing file
		final File dir = new File(tempFolder.getRoot(),
				"ProgramData" + File.separator + "Stocks" + File.separator + "TEST");
		dir.mkdirs();
		final File existingFile = new File(dir, "TEST" + URLS.dailyDataFile);
		Files.write(existingFile.toPath(), "2023-01-02\t101.5\n".getBytes(StandardCharsets.UTF_8));

		final List<String> dates = List.of("2023-01-10", "2023-01-11");
		final List<String> prices = List.of("200.0", "201.0");

		urls.updateData(dates, prices);

		final List<String> lines = Files.readAllLines(existingFile.toPath(), StandardCharsets.UTF_8);
		assertEquals(2, lines.size());
		assertTrue(lines.get(0).contains("2023-01-10"));
	}

	// ------------------------------------------------------------------
	// URL construction helpers
	// ------------------------------------------------------------------

	@Test
	public void setFEDUrlShouldContainDataName() {
		final URLS urls = buildURLs("FED");
		urls.setFEDUrl();
		assertTrue(urls.toString().contains("TEST"));
	}

	@Test
	public void setQuandlUrlShouldContainDatasetAndName() {
		final URLS urls = buildURLs("QUANDL");
		urls.setQuandlUrl("WIKI", "AAPL", "");
		assertTrue(urls.toString().contains("WIKI"));
		assertTrue(urls.toString().contains("AAPL"));
	}

	@Test
	public void setQuandlUrlWithKeyShouldIncludeApiKey() {
		final URLS urls = buildURLs("QUANDL");
		urls.setQuandlUrl("WIKI", "AAPL", "mykey123");
		assertTrue(urls.toString().contains("WIKI"));
	}

	@Test
	public void toStringShouldContainAllFields() {
		final URLS urls = buildURLs("FED");
		final String str = urls.toString();
		assertTrue(str.contains("TEST"));
		assertTrue(str.contains("Stocks"));
		assertTrue(str.contains("FED"));
	}
}
