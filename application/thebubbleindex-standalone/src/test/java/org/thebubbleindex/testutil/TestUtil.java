package org.thebubbleindex.testutil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestUtil {

	public static void parseDailyData(final List<String> lines, final List<String> priceValues,
			final List<String> dailyPriceDate) {
		for (final String line : lines) {
			final Scanner lineScan = new Scanner(line);
			lineScan.useDelimiter(",|\t");
			dailyPriceDate.add(lineScan.next());
			priceValues.add(lineScan.next());
			lineScan.close();
		}
	}

	public static void parseDailyDataDoubles(final List<String> lines, final List<Double> priceValues,
			final List<String> dailyPriceDate) {
		for (final String line : lines) {
			final Scanner lineScan = new Scanner(line);
			lineScan.useDelimiter(",|\t");
			dailyPriceDate.add(lineScan.next());
			priceValues.add(Double.parseDouble(lineScan.next()));
			lineScan.close();
		}
	}
	
	public static void moveFiles(final String dailyDataPricePathRoot, final String windowsString, final String categoryName,
			final String selectionName) throws IOException {
		final String fileSep = File.separator;
		final String targetPathRoot = TestUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath()
				.replaceFirst("test-classes/", "").replaceFirst("classes/", "") + "ProgramData" + fileSep + categoryName + fileSep + selectionName;
		new File(targetPathRoot).mkdirs();
		final String[] windows = windowsString.split(",");
		for (final String window : windows) {
			final String sourceURL = TestUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath()
					+ dailyDataPricePathRoot + String.valueOf(window) + "days.csv";
			final Path source = new File(sourceURL).toPath();
			final Path target = new File(targetPathRoot + fileSep + selectionName + String.valueOf(window) + "days.csv")
					.toPath();
			Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
		}
	}

	public static List<String> getLines(final String string) throws IOException {
		final List<String> lines = new ArrayList<String>(100);
		try (BufferedReader br = new BufferedReader(new StringReader(string))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	lines.add(line);
		    }
		}
		
		return lines;
	}
}
