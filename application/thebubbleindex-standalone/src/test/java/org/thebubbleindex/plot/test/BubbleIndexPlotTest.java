package org.thebubbleindex.plot.test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.plot.BubbleIndexPlot;

/**
 * 
 * @author bigttrott
 *
 */
public class BubbleIndexPlotTest {
	final String fileSep = File.separator;

	// will find a better way, for now to view the plot un-comment out the @Test
	// line
	// and then run Junit test
	// @Test
	public void bubbleIndexPlotShouldDisplayCustomDate()
			throws ParseException, URISyntaxException, IOException, InterruptedException {
		Indices.initialize();
		final List<String> dailyPriceData = new ArrayList<String>();
		final List<String> dailyPriceDate = new ArrayList<String>();
		final String categoryName = "Currencies";
		final String selectionName = "BITSTAMPUSD";
		final String windowsString = "52,104,153,256";
		final Date begDate = new SimpleDateFormat("yyyy-MM-dd").parse("2015-01-01");
		final Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2016-08-01");
		final boolean isCustomRange = true;
		final String testFolder = "plot-testing";
		final String dailyDataPricePathRoot = testFolder + fileSep + selectionName + fileSep + selectionName;
		final URL dailyDataUrl = getClass().getClassLoader().getResource(dailyDataPricePathRoot + "dailydata.csv");
		final Path dailyDataPath = new File(dailyDataUrl.toURI()).toPath();
		final List<String> lines = Files.readAllLines(dailyDataPath, Charset.defaultCharset());

		parseDailyData(lines, dailyPriceData, dailyPriceDate);
		moveFiles(dailyDataPricePathRoot, windowsString, categoryName, selectionName);

		new BubbleIndexPlot(null, categoryName, selectionName, windowsString, begDate, endDate, isCustomRange,
				dailyPriceData, dailyPriceDate);

		for (int i = 0; i < 10; i++) {
			// Pause for 4 seconds
			Thread.sleep(4000);
			// Print a message
			System.out.println("Sleeping Thread");
		}
	}

	// will find a better way, for now to view the plot un-comment out the @Test
	// line
	// and then run Junit test
	// @Test
	public void bubbleIndexPlotShouldDisplayFullDates()
			throws ParseException, URISyntaxException, IOException, InterruptedException {
		Indices.initialize();
		final List<String> dailyPriceData = new ArrayList<String>();
		final List<String> dailyPriceDate = new ArrayList<String>();
		final String categoryName = "Currencies";
		final String selectionName = "BITSTAMPUSD";
		final String windowsString = "52,104,153,256";
		final Date begDate = new SimpleDateFormat("yyyy-MM-dd").parse("2015-01-01");
		final Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2016-08-01");
		final boolean isCustomRange = false;
		final String testFolder = "plot-testing";
		final String dailyDataPricePathRoot = testFolder + fileSep + selectionName + fileSep + selectionName;
		final URL dailyDataUrl = getClass().getClassLoader().getResource(dailyDataPricePathRoot + "dailydata.csv");
		final Path dailyDataPath = new File(dailyDataUrl.toURI()).toPath();
		final List<String> lines = Files.readAllLines(dailyDataPath, Charset.defaultCharset());

		parseDailyData(lines, dailyPriceData, dailyPriceDate);
		moveFiles(dailyDataPricePathRoot, windowsString, categoryName, selectionName);

		new BubbleIndexPlot(null, categoryName, selectionName, windowsString, begDate, endDate, isCustomRange,
				dailyPriceData, dailyPriceDate);

		for (int i = 0; i < 10; i++) {
			// Pause for 4 seconds
			Thread.sleep(4000);
			// Print a message
			System.out.println("Sleeping Thread");
		}
	}

	private void moveFiles(final String dailyDataPricePathRoot, final String windowsString, final String categoryName,
			final String selectionName) throws IOException {
		final String targetPathRoot = getClass().getProtectionDomain().getCodeSource().getLocation().getPath()
				.replaceFirst("test-classes/", "") + "ProgramData" + fileSep + categoryName + fileSep + selectionName;
		new File(targetPathRoot).mkdirs();
		final String[] windows = windowsString.split(",");
		for (final String window : windows) {
			final String sourceURL = getClass().getProtectionDomain().getCodeSource().getLocation().getPath()
					+ dailyDataPricePathRoot + String.valueOf(window) + "days.csv";
			final Path source = new File(sourceURL).toPath();
			final Path target = new File(targetPathRoot + fileSep + selectionName + String.valueOf(window) + "days.csv")
					.toPath();
			Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
		}
	}

	private void parseDailyData(final List<String> lines, final List<String> priceValues,
			final List<String> dailyPriceDate) {
		for (final String line : lines) {
			final Scanner lineScan = new Scanner(line);
			lineScan.useDelimiter(",|\t");
			dailyPriceDate.add(lineScan.next());
			priceValues.add(lineScan.next());
			lineScan.close();
		}
	}
}
