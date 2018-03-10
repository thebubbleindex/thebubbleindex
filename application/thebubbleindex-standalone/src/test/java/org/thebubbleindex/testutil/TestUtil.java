package org.thebubbleindex.testutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
