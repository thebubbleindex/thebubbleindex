package org.thebubbleindex.util.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.thebubbleindex.util.Utilities;

public class DataReverseTest {

	final double epsilon = 0.0000000001;
	
	@Test
	public void dataShouldBeReversedTest() {
		final int size = 100;
		final double[] arrayDoubles = new double[size];
		
		for (int i = 0; i < size; i++) {
			arrayDoubles[i] = (double) i;
		}
		
		Utilities.DataReverse(arrayDoubles, size);
		
		for (int i = 0; i < size; i++) {
			assertEquals((double) (size - 1 - i), arrayDoubles[i], epsilon);
		}
	}
	
}
