package org.thebubbleindex.driver.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.thebubbleindex.driver.DailyDataCache;

/**
 * Tests that verify {@link DailyDataCache} correctly stores, retrieves, and
 * clears its cached daily price data.
 */
public class DailyDataCacheTest {

	@Test
	public void defaultSelectionNameShouldBeEmpty() {
		final DailyDataCache cache = new DailyDataCache();
		assertEquals("", cache.getSelectionName());
	}

	@Test
	public void setAndGetSelectionNameShouldWork() {
		final DailyDataCache cache = new DailyDataCache();
		cache.setSelectionName("TSLA");
		assertEquals("TSLA", cache.getSelectionName());
	}

	@Test
	public void defaultDailyPriceDataShouldBeEmptyList() {
		final DailyDataCache cache = new DailyDataCache();
		assertNotNull(cache.getDailyPriceData());
		assertTrue(cache.getDailyPriceData().isEmpty());
	}

	@Test
	public void setAndGetDailyPriceDataShouldWork() {
		final DailyDataCache cache = new DailyDataCache();
		final List<String> data = Arrays.asList("100.0", "101.5", "102.3");
		cache.setDailyPriceData(data);
		assertEquals(data, cache.getDailyPriceData());
		assertEquals(3, cache.getDailyPriceData().size());
	}

	@Test
	public void defaultDailyPriceDateShouldBeEmptyList() {
		final DailyDataCache cache = new DailyDataCache();
		assertNotNull(cache.getDailyPriceDate());
		assertTrue(cache.getDailyPriceDate().isEmpty());
	}

	@Test
	public void setAndGetDailyPriceDateShouldWork() {
		final DailyDataCache cache = new DailyDataCache();
		final List<String> dates = Arrays.asList("2023-01-01", "2023-01-02", "2023-01-03");
		cache.setDailyPriceDate(dates);
		assertEquals(dates, cache.getDailyPriceDate());
		assertEquals(3, cache.getDailyPriceDate().size());
	}

	@Test
	public void setAndGetDailyPriceDoubleValuesShouldWork() {
		final DailyDataCache cache = new DailyDataCache();
		final double[] values = { 100.0, 101.5, 102.3 };
		cache.setDailyPriceDoubleValues(values);
		final double[] stored = cache.getDailyPriceDoubleValues();
		assertEquals(3, stored.length);
		assertEquals(100.0, stored[0], 0.0);
		assertEquals(101.5, stored[1], 0.0);
		assertEquals(102.3, stored[2], 0.0);
	}

	@Test
	public void resetShouldClearSelectionNameAndData() {
		final DailyDataCache cache = new DailyDataCache();
		cache.setSelectionName("TSLA");
		cache.setDailyPriceData(Arrays.asList("100.0", "101.5"));
		cache.setDailyPriceDate(Arrays.asList("2023-01-01", "2023-01-02"));

		cache.reset();

		assertEquals("", cache.getSelectionName());
		assertTrue(cache.getDailyPriceData().isEmpty());
		assertTrue(cache.getDailyPriceDate().isEmpty());
	}
}
