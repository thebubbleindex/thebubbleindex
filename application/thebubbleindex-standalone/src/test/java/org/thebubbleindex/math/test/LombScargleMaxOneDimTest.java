package org.thebubbleindex.math.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.math.LombScargle;

/**
 * Tests that verify {@link LombScargle#MaxOneDim(double[], int)} correctly
 * returns the maximum value in a one-dimensional array, and that
 * {@link LombScargle#hqDerivative} and
 * {@link LombScargle#computeLombScargle} produce non-negative results on a
 * small synthetic data set.
 */
public class LombScargleMaxOneDimTest {

	final double epsilon = 1e-12;

	@Test
	public void maxOneDimShouldReturnMaxOfPositiveValues() {
		final double[] array = { 1.0, 5.0, 3.0, 7.0, 2.0 };
		final double max = LombScargle.MaxOneDim(array, array.length);
		assertEquals(7.0, max, epsilon);
	}

	@Test
	public void maxOneDimShouldReturnZeroForAllZeros() {
		final double[] array = { 0.0, 0.0, 0.0 };
		final double max = LombScargle.MaxOneDim(array, array.length);
		assertEquals(0.0, max, epsilon);
	}

	@Test
	public void maxOneDimShouldReturnZeroForAllNegativeValues() {
		// The method starts with MaxValue = 0.0 and only updates when Array[i] > 0
		final double[] array = { -1.0, -2.0, -3.0 };
		final double max = LombScargle.MaxOneDim(array, array.length);
		assertEquals(0.0, max, epsilon);
	}

	@Test
	public void maxOneDimShouldFindMaxAtFirstElement() {
		final double[] array = { 42.0, 1.0, 2.0, 3.0 };
		final double max = LombScargle.MaxOneDim(array, array.length);
		assertEquals(42.0, max, epsilon);
	}

	@Test
	public void maxOneDimShouldFindMaxAtLastElement() {
		final double[] array = { 1.0, 2.0, 3.0, 99.0 };
		final double max = LombScargle.MaxOneDim(array, array.length);
		assertEquals(99.0, max, epsilon);
	}

	@Test
	public void maxOneDimShouldHandleSingleElementArray() {
		final double[] array = { 5.5 };
		final double max = LombScargle.MaxOneDim(array, array.length);
		assertEquals(5.5, max, epsilon);
	}

	@Test
	public void maxOneDimShouldRespectSizeParameter() {
		// Only the first 3 elements are considered (SIZE=3), 99.0 is at index 3
		final double[] array = { 1.0, 2.0, 3.0, 99.0 };
		final double max = LombScargle.MaxOneDim(array, 3);
		assertEquals(3.0, max, epsilon);
	}

	// -------------------------------------------------------------------------
	// Tests for computeLombScargle and hqDerivative using a minimal LombScargle
	// configuration so the tests run fast.
	// -------------------------------------------------------------------------

	/**
	 * Builds a minimal {@link LombScargle} instance. The lombscargle.properties
	 * file will not be found so the constructor values (freqSize=5, qSize=4,
	 * hSize=4) are used directly.
	 */
	private LombScargle buildSmallLombScargle() {
		final Indices indices = new Indices();
		// userDir is set to /tmp so lombscargle.properties will not be found;
		// the constructor falls back to the provided values.
		indices.setUserDir(System.getProperty("java.io.tmpdir") + java.io.File.separator);
		return new LombScargle(5, 4, 4, 6.28, 0.38, indices);
	}

	@Test
	public void computeLombScargleShouldProduceNonNegativeSpectralDensity() {
		final LombScargle ls = buildSmallLombScargle();
		final int size = 30;
		final double[] timeValues = new double[size];
		final double[] timeSeries = new double[size];
		final double[] spectralDensity = new double[ls.freqSize];

		for (int i = 0; i < size; i++) {
			timeValues[i] = size + 21.0 - i; // positive, decreasing time values
			timeSeries[i] = 4.5 + 0.1 * i;
		}

		ls.computeLombScargle(timeValues, timeSeries, spectralDensity, size);

		for (int i = 0; i < ls.freqSize; i++) {
			assertTrue("SpectralDensity[" + i + "] should be non-negative", spectralDensity[i] >= 0.0);
		}
	}

	@Test
	public void hqDerivativeShouldReturnNonNegativeValue() {
		final LombScargle ls = buildSmallLombScargle();
		final int size = 30;
		final double[] timeValues = new double[size];
		final double[] coef = { 4.6, 0.5, 0.3 };

		for (int i = 0; i < size; i++) {
			timeValues[i] = size + 21.0 - i;
		}

		final double result = ls.hqDerivative(timeValues, coef, size);
		assertTrue("hqDerivative should return a non-negative value", result >= 0.0);
	}

	// -------------------------------------------------------------------------
	// Constructor field-initialisation tests
	// -------------------------------------------------------------------------

	@Test
	public void constructorShouldInitialiseFreqSizeQSizeHSizeFields() {
		final LombScargle ls = buildSmallLombScargle();
		assertEquals(5, ls.freqSize);
		assertEquals(4, ls.qSize);
		assertEquals(4, ls.hSize);
	}

	@Test
	public void constructorShouldSetOmegaAndMCoeffFields() {
		final LombScargle ls = buildSmallLombScargle();
		assertEquals(6.28, ls.omegaDouble, epsilon);
		assertEquals(0.38, ls.mCoeffDouble, epsilon);
		assertEquals((float) 6.28, ls.omegaFloat, 1e-4f);
		assertEquals((float) 0.38, ls.mCoeffFloat, 1e-4f);
	}

	@Test
	public void constructorShouldAllocateArraysWithCorrectLengths() {
		final LombScargle ls = buildSmallLombScargle();
		assertEquals(5, ls.testFrequencies.length);
		assertEquals(4, ls.Q.length);
		assertEquals(4, ls.H.length);
		assertEquals(4, ls.logQi.length);
		assertEquals(4, ls.QiM.length);
		assertEquals(4, ls.cOne.length);
		assertEquals(4, ls.cTwo.length);
		assertEquals(4, ls.powTempVar.length);
		assertEquals(4, ls.powTempVar[0].length);
	}

	@Test
	public void constructorShouldPopulateTestFrequenciesWithIncreasingValues() {
		final LombScargle ls = buildSmallLombScargle();
		for (int i = 1; i < ls.freqSize; i++) {
			assertTrue("testFrequencies should be strictly increasing",
					ls.testFrequencies[i] > ls.testFrequencies[i - 1]);
		}
	}

	@Test
	public void constructorShouldPopulateQArrayWithPositiveValues() {
		final LombScargle ls = buildSmallLombScargle();
		for (int i = 0; i < ls.qSize; i++) {
			assertTrue("Q[" + i + "] should be positive", ls.Q[i] > 0.0);
		}
	}
}
