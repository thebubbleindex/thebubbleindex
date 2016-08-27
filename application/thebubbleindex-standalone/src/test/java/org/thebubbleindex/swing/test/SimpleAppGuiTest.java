package org.thebubbleindex.swing.test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.thebubbleindex.driver.noGUI;

public class SimpleAppGuiTest {
	final String fileSep = File.separator;

	// un-comment to debug
	// @Test
	public void shouldOpenGuiForDebugging()
			throws InterruptedException, IOException, ParseException, URISyntaxException, ClassNotFoundException {

		final String targetPathRoot = getClass().getProtectionDomain().getCodeSource().getLocation().getPath()
				.replaceFirst("test-classes/", "") + "ProgramData";

		final List<String> lines = new ArrayList<String>();
		lines.add(String.format("Indices"));
		lines.add(String.format("Currencies"));
		lines.add(String.format("Stocks"));

		new File(targetPathRoot).mkdirs();

		final File tempFile = new File(targetPathRoot + fileSep + "CategoryList.csv");

		final Path tempFilePath = tempFile.toPath();
		Files.write(tempFilePath, lines, Charset.defaultCharset());

		final String categoryName = "Currencies";
		final String selectionName = "BITSTAMPUSD";
		final String windowsString = "52,104,153,256";

		new File(targetPathRoot + fileSep + categoryName).mkdirs();
		final File updateSelectionFile = new File(targetPathRoot + fileSep + categoryName + ".csv");
		final List<String> updateSelectionLines = new ArrayList<String>();
		updateSelectionLines.add(selectionName);
		Files.write(updateSelectionFile.toPath(), updateSelectionLines, Charset.defaultCharset());

		final String testFolder = "plot-testing";
		final String dailyDataPricePathRoot = testFolder + fileSep + selectionName + fileSep + selectionName;
		final URL dailyDataUrl = getClass().getClassLoader().getResource(dailyDataPricePathRoot + "dailydata.csv");
		final Path dailyDataPath = new File(dailyDataUrl.toURI()).toPath();
		final List<String> dailyDatalines = Files.readAllLines(dailyDataPath, Charset.defaultCharset());

		moveFiles(dailyDataPricePathRoot, windowsString, categoryName, selectionName);

		Files.write(new File(targetPathRoot + fileSep + categoryName + fileSep + selectionName + fileSep + selectionName
				+ "dailydata.csv").toPath(), dailyDatalines, Charset.defaultCharset());
		new noGUI();

		noGUI.main(new String[] {});

		for (int i = 0; i < 20; i++) {
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
}
