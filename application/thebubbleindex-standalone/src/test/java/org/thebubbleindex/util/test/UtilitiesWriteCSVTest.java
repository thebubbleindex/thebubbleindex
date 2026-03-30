package org.thebubbleindex.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.thebubbleindex.util.Utilities;

/**
 * Tests that verify {@link Utilities#WriteCSV} correctly writes results to a
 * CSV file with a header in new-file mode, and appends without a header in
 * update mode.
 */
public class UtilitiesWriteCSVTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Test
	public void writeCSVShouldCreateFileWithHeaderInNewMode() throws IOException {
		final File dir = tempFolder.newFolder("output");
		final List<Double> results = Arrays.asList(10.0, 20.0, 30.0);
		final List<String> dates = Arrays.asList("2024-01-01", "2024-01-02", "2024-01-03");
		final String fileName = "UtilTest52days.csv";

		Utilities.WriteCSV(dir.getAbsolutePath(), results, 52, fileName, dates, false);

		final File out = new File(dir, fileName);
		assertTrue(out.exists());

		final List<String> lines = Files.readAllLines(out.toPath(), Charset.defaultCharset());
		// Header + 3 data rows
		assertEquals(4, lines.size());
		assertEquals("Period Number,Value,Date", lines.get(0));
		assertEquals("50,10.0,2024-01-01", lines.get(1));
		assertEquals("51,20.0,2024-01-02", lines.get(2));
		assertEquals("52,30.0,2024-01-03", lines.get(3));
	}

	@Test
	public void writeCSVShouldAppendWithoutHeaderInUpdateMode() throws IOException {
		final File dir = tempFolder.newFolder("update-output");
		final List<Double> firstResults = Arrays.asList(1.0, 2.0);
		final List<String> firstDates = Arrays.asList("2024-01-01", "2024-01-02");
		final String fileName = "UtilUpdate52days.csv";

		// Create the file first (no header skipped - non-update mode)
		Utilities.WriteCSV(dir.getAbsolutePath(), firstResults, 52, fileName, firstDates, false);

		// Append two more rows in update mode
		final List<Double> moreResults = Arrays.asList(3.0, 4.0);
		final List<String> moreDates = Arrays.asList("2024-01-03", "2024-01-04");
		Utilities.WriteCSV(dir.getAbsolutePath(), moreResults, 52, fileName, moreDates, true);

		final List<String> lines = Files.readAllLines(new File(dir, fileName).toPath(), Charset.defaultCharset());
		// Header + 2 original rows + 2 appended rows = 5 lines total
		assertEquals(5, lines.size());
		assertEquals("Period Number,Value,Date", lines.get(0));
		// Third data row (first appended)
		assertEquals("51,3.0,2024-01-03", lines.get(3));
		assertEquals("52,4.0,2024-01-04", lines.get(4));
	}
}
