package org.thebubbleindex.runnable.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.thebubbleindex.exception.InvalidData;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.runnable.RunIndex;

/**
 * Tests that verify {@link RunIndex#updateDateMatch(List)} correctly locates a
 * date in the daily price date series and throws {@link InvalidData} when no
 * match is found.
 */
public class RunIndexUpdateDateMatchTest {

	private RunIndex buildRunIndex(final List<String> dailyPriceDates) {
		final RunContext runContext = new RunContext();
		return new RunIndex(
				null,
				new double[dailyPriceDates.size()],
				dailyPriceDates.size(),
				10,
				new ArrayList<Double>(),
				dailyPriceDates,
				"/nonexistent/path.csv",
				"TEST",
				6.28,
				0.38,
				21.0,
				null,
				null,
				runContext);
	}

	@Test
	public void updateDateMatchShouldReturnIndexOfLastMatchedDate() throws InvalidData {
		final List<String> dates = Arrays.asList(
				"2023-01-01", "2023-01-02", "2023-01-03", "2023-01-04", "2023-01-05");

		final RunIndex runIndex = buildRunIndex(dates);

		// DateList whose last entry is "2023-01-03" (index 2 in dailyPriceDate)
		final List<String> dateList = Arrays.asList("2023-01-01", "2023-01-02", "2023-01-03");
		final int result = runIndex.updateDateMatch(dateList);
		assertEquals(2, result);
	}

	@Test
	public void updateDateMatchShouldReturnZeroWhenLastDateIsFirst() throws InvalidData {
		final List<String> dates = Arrays.asList("2023-01-01", "2023-01-02", "2023-01-03");

		final RunIndex runIndex = buildRunIndex(dates);

		final List<String> dateList = Arrays.asList("2023-01-01");
		final int result = runIndex.updateDateMatch(dateList);
		assertEquals(0, result);
	}

	@Test(expected = InvalidData.class)
	public void updateDateMatchShouldThrowInvalidDataWhenNoDateMatches() throws InvalidData {
		final List<String> dates = Arrays.asList("2023-01-01", "2023-01-02", "2023-01-03");

		final RunIndex runIndex = buildRunIndex(dates);

		// DateList whose last entry does not exist in dailyPriceDate
		final List<String> dateList = Arrays.asList("1999-12-31");
		runIndex.updateDateMatch(dateList);
	}
}
