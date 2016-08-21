package org.thebubbleindex.util.test;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import org.thebubbleindex.util.Utilities;

public class LinearFitTest {
	final double epsilon = 0.0000000001;

	@Test
	public void answerShouldSolveLinearEquationTest() {
		final int size = 512;
		final double[] arrayDoublesOne = new double[size];
		final double[] arrayDoublesTwo = new double[size];
		final double[] arrayDoublesThree = new double[size];

		final Random random = new Random(8899);
		for (int i = 0; i < size; i++) {
			arrayDoublesOne[i] = random.nextDouble() * 1000.0;
			arrayDoublesTwo[i] = FastMath.pow(arrayDoublesOne[i], 0.4);
			arrayDoublesThree[i] = FastMath.cos(arrayDoublesOne[i]);
		}

		final double[] coef = new double[3];

		Utilities.LinearFit(arrayDoublesOne, arrayDoublesTwo, arrayDoublesThree, coef, size);

		assertEquals(-441.3594627276007, coef[0], epsilon);
		assertEquals(83.76623921085569, coef[1], epsilon);
		assertEquals(-1.7254895273344615, coef[2], epsilon);

		random.setSeed(6677);
		for (int i = 0; i < size; i++) {
			arrayDoublesOne[i] = random.nextDouble() * 1000.0;
			arrayDoublesTwo[i] = FastMath.pow(arrayDoublesOne[i], 0.4);
			arrayDoublesThree[i] = FastMath.cos(arrayDoublesOne[i]);
		}

		Utilities.LinearFit(arrayDoublesOne, arrayDoublesTwo, arrayDoublesThree, coef, size);

		assertEquals(-423.73895345581604, coef[0], epsilon);
		assertEquals(81.50190028089978, coef[1], epsilon);
		assertEquals(-4.443288951721952, coef[2], epsilon);

	}

	@Test
	public void simpleExampleShouldSolveLinearEquationTest() {
		final int size = 3;
		final double[] arrayDoublesOne = new double[] { 1.0, 3.0, 5.0 };
		final double[] arrayDoublesTwo = new double[] { 2.0, 2.0, 6.0 };
		final double[] arrayDoublesThree = new double[] { 1.0, 6.0, 8.0 };

		final double[] coef = new double[3];

		Utilities.LinearFit(arrayDoublesOne, arrayDoublesTwo, arrayDoublesThree, coef, size);

		assertEquals(0.0, coef[0], epsilon);
		assertEquals(0.3, coef[1], epsilon);
		assertEquals(0.4, coef[2], epsilon);
	}
}
