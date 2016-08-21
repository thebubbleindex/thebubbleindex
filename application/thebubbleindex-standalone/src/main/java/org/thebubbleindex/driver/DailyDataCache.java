package org.thebubbleindex.driver;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ttrott
 */
public class DailyDataCache {

	public static String selectionName;
	public static List<String> dailyPriceData;
	public static List<String> dailyPriceDate;
	public static double[] dailyPriceDoubleValues;

	static {
		selectionName = "";
		dailyPriceData = new ArrayList<String>(10000);
		dailyPriceDate = new ArrayList<String>(10000);
	}

	public static void reset() {
		selectionName = "";
		dailyPriceData = new ArrayList<String>(10000);
		dailyPriceDate = new ArrayList<String>(10000);
	}
}
