/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.thebubbleindex.driver;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author green
 */
public class DailyDataCache {

	public static String selectionName;
	public static List<String> dailyPriceData;
	public static List<String> dailyPriceDate;
	public static double[] dailyPriceDoubleValues;

	static {
		selectionName = "";
		dailyPriceData = new ArrayList<>(10000);
		dailyPriceDate = new ArrayList<>(10000);
	}

	public static void reset() {
		selectionName = "";
		dailyPriceData = new ArrayList<>(10000);
		dailyPriceDate = new ArrayList<>(10000);
	}
}
