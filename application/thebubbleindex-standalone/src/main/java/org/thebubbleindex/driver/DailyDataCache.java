package org.thebubbleindex.driver;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bigttrott
 */
public class DailyDataCache {

	private String selectionName = "";
	private List<String> dailyPriceData = new ArrayList<String>(10000);
	private List<String> dailyPriceDate = new ArrayList<String>(10000);
	private double[] dailyPriceDoubleValues;
	
	public void reset() {
		selectionName = "";
		dailyPriceData = new ArrayList<String>(10000);
		dailyPriceDate = new ArrayList<String>(10000);
	}

	public String getSelectionName() {
		return selectionName;
	}

	public void setSelectionName(final String selectionName) {
		this.selectionName = selectionName;
	}

	public List<String> getDailyPriceData() {
		return dailyPriceData;
	}

	public void setDailyPriceData(final List<String> dailyPriceData) {
		this.dailyPriceData = dailyPriceData;
	}

	public List<String> getDailyPriceDate() {
		return dailyPriceDate;
	}

	public void setDailyPriceDate(final List<String> dailyPriceDate) {
		this.dailyPriceDate = dailyPriceDate;
	}

	public double[] getDailyPriceDoubleValues() {
		return dailyPriceDoubleValues;
	}

	public void setDailyPriceDoubleValues(final double[] dailyPriceDoubleValues) {
		this.dailyPriceDoubleValues = dailyPriceDoubleValues;
	}
}
