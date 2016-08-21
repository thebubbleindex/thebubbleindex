package org.thebubbleindex.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.thebubbleindex.util.Utilities;

public class ReadValuesTest {

	@Test
	public void dataShouldReadTimeSeriesCorrectlyTest() throws IOException {
		final List<String> lines = new ArrayList<String>();
		lines.add(String.format("2013-01-02\t553.546"));
		lines.add(String.format("2013-01-03,555.6"));
		lines.add(String.format("2013-01-04,558.56"));
		lines.add(String.format("2013-01-05\t557.46"));

		final Path tempFile = Files.createTempFile("tempfilesOne", ".tmp");
		Files.write(tempFile, lines, Charset.defaultCharset(), StandardOpenOption.WRITE);
		tempFile.toFile().deleteOnExit();

		final List<String> columnOne = new ArrayList<String>();
		final List<String> columnTwo = new ArrayList<String>();

		Utilities.ReadValues(tempFile.toString(), columnOne, columnTwo, false, false);

		assertEquals("2013-01-02", columnOne.get(0));
		assertEquals("2013-01-03", columnOne.get(1));
		assertEquals("2013-01-04", columnOne.get(2));
		assertEquals("2013-01-05", columnOne.get(3));

		assertEquals("553.546", columnTwo.get(0));
		assertEquals("555.6", columnTwo.get(1));
		assertEquals("558.56", columnTwo.get(2));
		assertEquals("557.46", columnTwo.get(3));

	}

	@Test
	public void dataShouldReadWindowFilesCorrectlyTest() throws IOException {
		final List<String> lines = new ArrayList<String>();
		lines.add(String.format("Period,Date,Value"));
		lines.add(String.format("1,553.546\t2013-01-02"));
		lines.add(String.format("2\t555.6,2013-01-03"));
		lines.add(String.format("3\t558.56\t2013-01-04"));
		lines.add(String.format("4,557.46,2013-01-05"));

		final Path tempFile = Files.createTempFile("tempfilesTwo", ".tmp");
		Files.write(tempFile, lines, Charset.defaultCharset(), StandardOpenOption.WRITE);
		tempFile.toFile().deleteOnExit();

		final List<String> columnOne = new ArrayList<String>();
		final List<String> columnTwo = new ArrayList<String>();

		Utilities.ReadValues(tempFile.toString(), columnOne, columnTwo, true, true);

		assertEquals("2013-01-02", columnTwo.get(0));
		assertEquals("2013-01-03", columnTwo.get(1));
		assertEquals("2013-01-04", columnTwo.get(2));
		assertEquals("2013-01-05", columnTwo.get(3));

		assertEquals("553.546", columnOne.get(0));
		assertEquals("555.6", columnOne.get(1));
		assertEquals("558.56", columnOne.get(2));
		assertEquals("557.46", columnOne.get(3));

	}

	@Test
	public void dataShouldReadBlankWindowFileCorrectlyTest() throws IOException {
		final List<String> lines = new ArrayList<String>();
		lines.add(String.format("Period,Date,Value"));

		final Path tempFile = Files.createTempFile("tempfilesThree", ".tmp");
		Files.write(tempFile, lines, Charset.defaultCharset(), StandardOpenOption.WRITE);
		tempFile.toFile().deleteOnExit();

		final List<String> columnOne = new ArrayList<String>();
		final List<String> columnTwo = new ArrayList<String>();

		Utilities.ReadValues(tempFile.toString(), columnOne, columnTwo, true, true);

		assertTrue(columnOne.isEmpty());
		assertTrue(columnTwo.isEmpty());

	}

	@Test
	public void dataShouldReadBlankTimeSeriesCorrectlyTest() throws IOException {
		final List<String> lines = new ArrayList<String>();

		final Path tempFile = Files.createTempFile("tempfilesFour", ".tmp");
		Files.write(tempFile, lines, Charset.defaultCharset(), StandardOpenOption.WRITE);
		tempFile.toFile().deleteOnExit();

		final List<String> columnOne = new ArrayList<String>();
		final List<String> columnTwo = new ArrayList<String>();

		Utilities.ReadValues(tempFile.toString(), columnOne, columnTwo, true, true);

		assertTrue(columnOne.isEmpty());
		assertTrue(columnTwo.isEmpty());

	}
}
