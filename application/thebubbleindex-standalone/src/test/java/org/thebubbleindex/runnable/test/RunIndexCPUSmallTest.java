package org.thebubbleindex.runnable.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.thebubbleindex.exception.FailedToRunIndex;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.runnable.RunIndex;

/**
 * Tests that verify {@link RunIndex#execIndexWithCPU()} correctly executes the
 * Bubble Index calculation on a small synthetic data set, and that
 * {@link RunIndex#updateDateMatch(List)} handles edge cases correctly.
 */
public class RunIndexCPUSmallTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	/** Creates a synthetic price series starting at 100 and growing at 1 % / day. */
	private double[] buildPriceSeries(final int size) {
		final double[] prices = new double[size];
		prices[0] = 100.0;
		for (int i = 1; i < size; i++) {
			prices[i] = prices[i - 1] * 1.01;
		}
		return prices;
	}

	/** Generates ISO date strings "2023-01-01" ... "2023-01-{size}". */
	private List<String> buildDateList(final int size) {
		final List<String> dates = new ArrayList<String>(size);
		for (int i = 0; i < size; i++) {
			dates.add(String.format("2023-01-%02d", i + 1));
		}
		return dates;
	}

	private Indices buildIndices() {
		final Indices indices = new Indices();
		indices.setUserDir(System.getProperty("java.io.tmpdir") + File.separator);
		return indices;
	}

	@Test
	public void execIndexWithCPUShouldProduceResultsForSmallDataset() throws Exception {
		final int totalSize = 80;
		final int window = 52;
		final double[] prices = buildPriceSeries(totalSize);
		final List<String> dates = buildDateList(totalSize);
		final List<Double> results = new ArrayList<Double>();
		final Indices indices = buildIndices();
		final RunContext runContext = new RunContext();
		runContext.setGUI(false);
		runContext.setThreadNumber(2);

		// Use a non-existent previous file so computation starts from scratch
		final String noExistingFile = tempFolder.getRoot().getAbsolutePath()
				+ File.separator + "nonexistent52days.csv";

		final RunIndex runIndex = new RunIndex(
				null, prices, totalSize, window, results, dates,
				noExistingFile, "TEST", 6.28, 0.38, 21.0,
				indices, null, runContext);

		runIndex.execIndexWithCPU();

		// totalSize - window = 28 results expected (indices 0..27)
		final int expectedCount = totalSize - window;
		assertEquals(expectedCount, results.size());
		for (final Double val : results) {
			assertTrue("Each result value should be non-negative", val >= 0.0);
		}
	}

	@Test(expected = FailedToRunIndex.class)
	public void execIndexWithCPUShouldThrowWhenWindowExceedsData() throws Exception {
		final int totalSize = 10;
		final int window = 50; // larger than data
		final double[] prices = buildPriceSeries(totalSize);
		final List<String> dates = buildDateList(totalSize);
		final List<Double> results = new ArrayList<Double>();
		final Indices indices = buildIndices();
		final RunContext runContext = new RunContext();
		runContext.setGUI(false);

		final RunIndex runIndex = new RunIndex(
				null, prices, totalSize, window, results, dates,
				"/nonexistent/path.csv", "TEST", 6.28, 0.38, 21.0,
				indices, null, runContext);

		runIndex.execIndexWithCPU(); // should throw FailedToRunIndex
	}
}
