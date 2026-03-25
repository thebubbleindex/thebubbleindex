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
 * @author thebubbleindex
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
	private final RunContext runContext;

	/**
	 * MyCPUCallable constructor.
	 *
	 * @param bubbleIndexWorker   the Swing worker used to publish per-date
	 *                            output messages, or {@code null} in headless
	 *                            mode
	 * @param index               the zero-based offset into
	 *                            {@code dailyPriceValues} that marks the start
	 *                            of the data window for this calculation
	 * @param numberOfDays        the size of the time window in days
	 * @param lombScargle         the pre-computed Lomb-Scargle parameters and
	 *                            helper methods
	 * @param tCritDouble         the critical time offset (t<sub>c</sub>) used
	 *                            to build the time-value array
	 * @param dailyPriceValues    the full price series as an array of doubles
	 * @param displayPeriodString the date string of the end of the window,
	 *                            used for display purposes
	 * @param selectionName       the name of the financial instrument being
	 *                            analysed
	 * @param runContext          shared run-time state (stop flag, GUI mode,
	 *                            etc.)
	 */
	public MyCPUCallable(final BubbleIndexWorker bubbleIndexWorker, final int index, final int numberOfDays,
			final LombScargle lombScargle, final double tCritDouble, final double[] dailyPriceValues,
			final String displayPeriodString, final String selectionName, final RunContext runContext) {
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
		this.runContext = runContext;
	}

	/**
	 * call executes the Bubble Index calculation for a single date on the CPU.
	 * The method normalises the selected price window, fits the log-periodic
	 * power-law equation via linear regression, and then computes the maximum
	 * Lomb-Scargle spectral density of the H,Q derivative over all test
	 * frequencies and (H, Q) pairs.
	 *
	 * @return the Bubble Index value for the date corresponding to
	 *         {@code index + window}, or {@code 0.0} if the time series is all
	 *         zeros or a stop signal has been issued
	 */
	@Override
	public Double call() {

		if (!runContext.isStop()) {
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
				return 0.0;
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

			if (runContext.isGUI()) {
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