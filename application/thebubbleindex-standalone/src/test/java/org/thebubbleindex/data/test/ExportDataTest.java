package org.thebubbleindex.data.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.thebubbleindex.data.ExportData;

/**
 * Tests that verify {@link ExportData#WriteCSV} correctly writes results to a
 * CSV file, including the header in new-file mode and omitting it in update
 * (append) mode.
 */
public class ExportDataTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Test
	public void writeCSVShouldCreateFileWithHeaderInNewMode() throws IOException {
		final File dir = tempFolder.newFolder("output");
		final List<Double> results = Arrays.asList(1.23, 4.56, 7.89);
		final List<String> dates = Arrays.asList("2023-01-01", "2023-01-02", "2023-01-03");
		final String fileName = "TestOutput52days.csv";

		ExportData.WriteCSV(dir.getAbsolutePath(), results, 52, fileName, dates, false);

		final File out = new File(dir, fileName);
		assertTrue(out.exists());

		final List<String> lines = Files.readAllLines(out.toPath(), Charset.defaultCharset());
		// Header + 3 data rows
		assertEquals(4, lines.size());
		assertEquals("Period Number,Value,Date", lines.get(0));
		assertEquals("50,1.23,2023-01-01", lines.get(1));
		assertEquals("51,4.56,2023-01-02", lines.get(2));
		assertEquals("52,7.89,2023-01-03", lines.get(3));
	}

	@Test
	public void writeCSVShouldAppendWithoutHeaderInUpdateMode() throws IOException {
		final File dir = tempFolder.newFolder("output-update");
		final List<Double> firstResults = Arrays.asList(1.0, 2.0);
		final List<String> firstDates = Arrays.asList("2023-01-01", "2023-01-02");
		final String fileName = "UpdateTest52days.csv";

		// Write initial file (no update)
		ExportData.WriteCSV(dir.getAbsolutePath(), firstResults, 100, fileName, firstDates, false);

		// Now append two more results in update mode
		final List<Double> moreResults = Arrays.asList(3.0, 4.0);
		final List<String> moreDates = Arrays.asList("2023-01-03", "2023-01-04");
		ExportData.WriteCSV(dir.getAbsolutePath(), moreResults, 100, fileName, moreDates, true);

		final List<String> lines = Files.readAllLines(new File(dir, fileName).toPath(), Charset.defaultCharset());
		// Header + 2 original rows + 2 appended rows = 5 lines
		assertEquals(5, lines.size());
		assertEquals("Period Number,Value,Date", lines.get(0));
		// Appended rows should not have a second header
		assertEquals("99,3.0,2023-01-03", lines.get(3));
		assertEquals("100,4.0,2023-01-04", lines.get(4));
	}

	@Test
	public void writeCSVShouldCreateFileWithJustHeaderWhenResultsAreEmpty() throws IOException {
		final File dir = tempFolder.newFolder("output-empty");
		final List<Double> results = Collections.emptyList();
		final List<String> dates = Collections.emptyList();

		ExportData.WriteCSV(dir.getAbsolutePath(), results, 52, "empty.csv", dates, false);

		final File out = new File(dir, "empty.csv");
		assertTrue("Output file should be created even for empty results", out.exists());

		final List<String> lines = Files.readAllLines(out.toPath(), Charset.defaultCharset());
		// Only the header row should be present
		assertEquals(1, lines.size());
		assertEquals("Period Number,Value,Date", lines.get(0));
	}
}
