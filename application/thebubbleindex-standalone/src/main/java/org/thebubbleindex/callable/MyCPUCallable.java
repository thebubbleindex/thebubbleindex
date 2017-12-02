package org.thebubbleindex.callable;

import static info.yeppp.Core.Multiply_V64fS64f_V64f;
import static info.yeppp.Core.Multiply_V64fV64f_V64f;
import static info.yeppp.Math.Cos_V64f_V64f;
import static info.yeppp.Math.Log_V64f_V64f;

import java.util.concurrent.Callable;
import org.apache.commons.math3.util.FastMath;
import org.thebubbleindex.math.LombScargle;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.swing.BubbleIndexWorker;
import org.thebubbleindex.util.Utilities;

/**
 * MyCPUCallable is the actual numerical calculation of The Bubble Index. The
 * value returned from the Run method of this class is the value of The Bubble
 * Index for a specific date.
 * 
 * @author bigttrott
 */
public class MyCPUCallable implements Callable<Double> {

	private final int numberOfDays;
	private final int index;
	private final LombScargle lombScargle;
	private final double tCritDouble;
	private final double mCoeffDouble;
	private final double omegaDouble;
	private final double[] dailyPriceValues;
	private final String displayPeriodString;
	private final String selectionName;
	private final BubbleIndexWorker bubbleIndexWorker;

	/**
	 * MyCPUCallable constructor
	 * 
	 * @param bubbleIndexWorker
	 * @param index
	 * @param numberOfDays
	 * @param lombScargle
	 * @param tCritDouble
	 * @param mCoeffDouble
	 * @param dailyPriceValues
	 * @param displayPeriodString
	 * @param selectionName
	 */
	public MyCPUCallable(final BubbleIndexWorker bubbleIndexWorker, final int index, final int numberOfDays,
			final LombScargle lombScargle, final double tCritDouble, final double[] dailyPriceValues,
			final String displayPeriodString, final String selectionName) {
		this.numberOfDays = numberOfDays;
		this.index = index;
		this.lombScargle = lombScargle;
		this.tCritDouble = tCritDouble;
		this.dailyPriceValues = dailyPriceValues;
		this.displayPeriodString = displayPeriodString;
		this.selectionName = selectionName;
		this.bubbleIndexWorker = bubbleIndexWorker;
		this.omegaDouble = lombScargle.omegaDouble;
		this.mCoeffDouble = lombScargle.mCoeffDouble;

	}

	/**
	 * call method to execute the calculation.
	 * 
	 * @return the value of The Bubble Index at a specific date
	 */
	@Override
	public Double call() {

		if (!RunContext.Stop) {
			final double[] TimeValues = new double[numberOfDays];
			final double[] TimeValues_M_Power = new double[numberOfDays];
			final double[] LogCosTimeValues = new double[numberOfDays];
			final double[] SelectedData = new double[numberOfDays];
			final double[] Coef = new double[3];
			final double[] LogTimeValuesTemp = new double[numberOfDays];

			boolean containsNonZeroDouble = false;

			// Arrays used in the H,Q derivative calculation and periodogram
			for (int k = 0; k < numberOfDays; k++) {
				TimeValues[k] = numberOfDays + tCritDouble - k;
				TimeValues_M_Power[k] = FastMath.pow(TimeValues[k], mCoeffDouble);
				SelectedData[k] = dailyPriceValues[k + index + 1];
				if (!containsNonZeroDouble && SelectedData[k] > 0)
					containsNonZeroDouble = true;
			}

			// Return 0.0 if the timeseries === 0
			if (!containsNonZeroDouble) {
				return new Double(0.0);
			}

			final double[] tempFour = new double[numberOfDays];
			final double[] tempFive = new double[numberOfDays];

			Log_V64f_V64f(TimeValues, 0, tempFour, 0, numberOfDays);

			System.arraycopy(tempFour, 0, LogTimeValuesTemp, 0, numberOfDays);

			Multiply_V64fS64f_V64f(tempFour, 0, omegaDouble, tempFive, 0, numberOfDays);
			Cos_V64f_V64f(tempFive, 0, tempFour, 0, numberOfDays);
			Multiply_V64fV64f_V64f(tempFour, 0, TimeValues_M_Power, 0, tempFive, 0, numberOfDays);

			System.arraycopy(tempFive, 0, LogCosTimeValues, 0, numberOfDays);

			// Normalize data to a price starting at 100
			Utilities.Normalize(SelectedData, numberOfDays);

			Utilities.DataReverse(SelectedData, numberOfDays);

			// Fit the curve with the equation given in:
			Utilities.LinearFit(SelectedData, TimeValues_M_Power, LogCosTimeValues, Coef, numberOfDays);

			final double Temp = lombScargle.hqDerivative(TimeValues, Coef, numberOfDays);

			final double output = Temp;

			if (RunContext.isGUI) {
				bubbleIndexWorker.publishText(String.format("Name: %s    Date: %s    Value: %15.2f    Window: %d",
						selectionName, displayPeriodString, output, numberOfDays));
			} else {
				System.out.println(String.format("Name: %s    Date: %s    Value: %15.2f    Window: %d", selectionName,
						displayPeriodString, output, numberOfDays));
			}
			return Temp;
		}

		else {
			return 0.0;
		}
	}
}