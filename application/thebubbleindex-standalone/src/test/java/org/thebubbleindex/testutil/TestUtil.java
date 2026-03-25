package org.thebubbleindex.testutil;

import java.util.List;
import java.util.Scanner;

/**
 * Shared test helper methods used across multiple test classes to parse
 * daily price data files into the list structures expected by the production
 * code.
 */
public class TestUtil {

	/**
	 * parseDailyData reads lines from a daily-data file (tab- or
	 * comma-delimited, with the date in the first column and the price in the
	 * second) and populates the two provided output lists.
	 *
	 * @param lines          the raw lines read from the daily-data file
	 * @param priceValues    output list that will be populated with price
	 *                       strings
	 * @param dailyPriceDate output list that will be populated with date
	 *                       strings
	 */
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

	/**
	 * parseDailyDataDoubles reads lines from a daily-data file (tab- or
	 * comma-delimited, with the date in the first column and the price in the
	 * second) and populates the two provided output lists, converting price
	 * strings to {@code Double} values.
	 *
	 * @param lines          the raw lines read from the daily-data file
	 * @param priceValues    output list that will be populated with parsed
	 *                       {@code Double} price values
	 * @param dailyPriceDate output list that will be populated with date
	 *                       strings
	 */
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
