package org.thebubbleindex.driver;

import java.util.ArrayList;
import java.util.List;

/**
 * DailyDataCache caches the most recently read daily price data for a given
 * selection, avoiding redundant file reads when the same selection is processed
 * across multiple time windows in a single run.
 *
 * @author thebubbleindex
 */
public class DailyDataCache {

	private String selectionName = "";
	private List<String> dailyPriceData = new ArrayList<String>(10000);
	private List<String> dailyPriceDate = new ArrayList<String>(10000);
	private double[] dailyPriceDoubleValues;

	/**
	 * reset clears the cached data and resets the selection name so that the
	 * next request will trigger a fresh file read.
	 */
	public void reset() {
		selectionName = "";
		dailyPriceData = new ArrayList<String>(10000);
		dailyPriceDate = new ArrayList<String>(10000);
	}

	/**
	 * getSelectionName returns the name of the currently cached selection.
	 *
	 * @return the selection name whose data is held in the cache
	 */
	public String getSelectionName() {
		return selectionName;
	}

	/**
	 * setSelectionName sets the name of the selection whose data is currently
	 * cached.
	 *
	 * @param selectionName the name of the selection
	 */
	public void setSelectionName(final String selectionName) {
		this.selectionName = selectionName;
	}

	/**
	 * getDailyPriceData returns the list of raw daily price strings read from
	 * the daily data CSV file.
	 *
	 * @return list of price value strings in chronological order
	 */
	public List<String> getDailyPriceData() {
		return dailyPriceData;
	}

	/**
	 * setDailyPriceData stores the list of raw daily price strings in the
	 * cache.
	 *
	 * @param dailyPriceData list of price value strings in chronological order
	 */
	public void setDailyPriceData(final List<String> dailyPriceData) {
		this.dailyPriceData = dailyPriceData;
	}

	/**
	 * getDailyPriceDate returns the list of date strings corresponding to each
	 * daily price entry.
	 *
	 * @return list of date strings in "yyyy-MM-dd" format
	 */
	public List<String> getDailyPriceDate() {
		return dailyPriceDate;
	}

	/**
	 * setDailyPriceDate stores the list of date strings in the cache.
	 *
	 * @param dailyPriceDate list of date strings in "yyyy-MM-dd" format
	 */
	public void setDailyPriceDate(final List<String> dailyPriceDate) {
		this.dailyPriceDate = dailyPriceDate;
	}

	/**
	 * getDailyPriceDoubleValues returns the array of daily price values already
	 * converted to primitive doubles.
	 *
	 * @return array of double-precision price values in chronological order
	 */
	public double[] getDailyPriceDoubleValues() {
		return dailyPriceDoubleValues;
	}

	/**
	 * setDailyPriceDoubleValues stores the array of converted double-precision
	 * price values in the cache.
	 *
	 * @param dailyPriceDoubleValues array of double-precision price values
	 */
	public void setDailyPriceDoubleValues(final double[] dailyPriceDoubleValues) {
		this.dailyPriceDoubleValues = dailyPriceDoubleValues;
	}
}
