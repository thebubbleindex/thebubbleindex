package org.thebubbleindex.util.test;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import org.thebubbleindex.util.Utilities;

public class NormalizeTest {
	final double epsilon = 0.0000000001;

	@Test
	public void dataShouldBeNormalizedTest_ConstRate() {
		final double rate = 1.01;
		final int size = 100;
		final double[] arrayDoubles = new double[size];
		final double[] expectedArray = new double[size];
		arrayDoubles[0] = 568.8;
		expectedArray[0] = 100.0;

		for (int i = 1; i < size; i++) {
			arrayDoubles[i] = arrayDoubles[i - 1] * rate;
			expectedArray[i] = expectedArray[i - 1] * rate;
		}

		Utilities.Normalize(arrayDoubles, size);

		for (int i = 0; i < size; i++) {
			assertEquals(Math.log(expectedArray[i]), arrayDoubles[i], epsilon);
		}
	}

	@Test
	public void dataShouldBeNormalizedTest_RandomRate() {
		final int size = 100;
		final double[] arrayDoubles = new double[size];
		final double[] expectedArray = new double[size];
		final double[] rates = new double[size];
		arrayDoubles[0] = 568.8;
		expectedArray[0] = 100.0;
		final Random random = new Random(100);
		for (int i = 1; i < size; i++) {
			rates[i] = 1.0 + random.nextDouble();
			arrayDoubles[i] = arrayDoubles[i - 1] * rates[i];
			expectedArray[i] = expectedArray[i - 1] * rates[i];
		}

		Utilities.Normalize(arrayDoubles, size);

		for (int i = 0; i < size; i++) {
			assertEquals(Math.log(expectedArray[i]), arrayDoubles[i], epsilon);
		}
	}
}
