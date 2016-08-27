package org.thebubbleindex.testutil;

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
}
