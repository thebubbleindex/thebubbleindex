package org.thebubbleindex.callable.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.thebubbleindex.callable.MyCPUCallable;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.math.LombScargle;
import org.thebubbleindex.runnable.RunContext;

/**
 * Fast unit tests for {@link MyCPUCallable} that exercise the CPU calculation
 * path with a small synthetic price series so the test completes quickly.
 */
public class MyCPUCallableSmallTest {

	/**
	 * Builds a minimal {@link LombScargle} with a small parameter set so
	 * computations complete in milliseconds.
	 */
	private LombScargle buildSmallLombScargle() {
		final Indices indices = new Indices();
		indices.setUserDir(System.getProperty("java.io.tmpdir") + File.separator);
		return new LombScargle(5, 4, 4, 6.28, 0.38, indices);
	}

	/** Creates a synthetic price series starting at 100 and growing at 1 % / day. */
	private double[] buildPriceSeries(final int size) {
		final double[] prices = new double[size];
		prices[0] = 100.0;
		for (int i = 1; i < size; i++) {
			prices[i] = prices[i - 1] * 1.01;
		}
		return prices;
	}

	@Test
	public void callShouldReturnNonNegativeValueForSyntheticData() throws Exception {
		final int totalSize = 80;
		final int window = 52;
		final double[] prices = buildPriceSeries(totalSize);
		final LombScargle ls = buildSmallLombScargle();
		final RunContext runContext = new RunContext();
		runContext.setGUI(false);

		final MyCPUCallable callable = new MyCPUCallable(
				null,       // no BubbleIndexWorker in headless mode
				0,          // index = start of window
				window,
				ls,
				21.0,       // tCritDouble
				prices,
				"2023-01-01",
				"TEST",
				runContext);

		final Double result = callable.call();
		assertTrue("Bubble Index value should be non-negative", result >= 0.0);
	}

	@Test
	public void callShouldReturnZeroWhenStopFlagIsSet() throws Exception {
		final int totalSize = 80;
		final int window = 52;
		final double[] prices = buildPriceSeries(totalSize);
		final LombScargle ls = buildSmallLombScargle();
		final RunContext runContext = new RunContext();
		runContext.setStop(true);
		runContext.setGUI(false);

		final MyCPUCallable callable = new MyCPUCallable(
				null, 0, window, ls, 21.0, prices, "2023-01-01", "TEST", runContext);

		final Double result = callable.call();
		assertEquals(0.0, result, 0.0);
	}

	@Test
	public void callShouldReturnZeroForAllZeroPrices() throws Exception {
		final int totalSize = 80;
		final int window = 52;
		final double[] prices = new double[totalSize]; // all zeros
		final LombScargle ls = buildSmallLombScargle();
		final RunContext runContext = new RunContext();
		runContext.setGUI(false);

		final MyCPUCallable callable = new MyCPUCallable(
				null, 0, window, ls, 21.0, prices, "2023-01-01", "TEST", runContext);

		final Double result = callable.call();
		assertEquals(0.0, result, 0.0);
	}
}
