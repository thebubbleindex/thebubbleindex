package org.thebubbleindex.callable;

import com.nativelibs4java.opencl.CLBuffer;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLEvent;
import com.nativelibs4java.opencl.CLKernel;
import com.nativelibs4java.opencl.CLMem;
import com.nativelibs4java.opencl.CLProgram;
import com.nativelibs4java.opencl.CLQueue;
import static info.yeppp.Core.Add_V64fV64f_V64f;
import static info.yeppp.Core.Multiply_V64fS64f_V64f;
import static info.yeppp.Core.Multiply_V64fV64f_V64f;
import static info.yeppp.Math.Cos_V64f_V64f;
import static info.yeppp.Math.Log_V64f_V64f;
import static info.yeppp.Math.Sin_V64f_V64f;
import java.nio.ByteOrder;
import java.util.concurrent.Callable;
import org.apache.commons.math3.util.FastMath;
import org.bridj.Pointer;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.math.LombScargle;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.swing.BubbleIndexWorker;
import org.thebubbleindex.util.Utilities;

import static org.bridj.Pointer.allocateFloats;

/**
 * MyGPUCallable is the actual numerical calculation of The Bubble Index. The
 * value returned from the Run method of this class is the value of The Bubble
 * Index for a specific date.
 * 
 * @author thebubbleindex
 */
public class MyGPUCallable implements Callable<Float> {

	private final int numberOfDays;
	private final int index;

	private final CLContext context;
	private final CLQueue queue;
	private final CLKernel kernel;
	private final ByteOrder byteOrder;

	private final int freqSize;
	private final int qSize;
	private final int hSize;
	private final double[] testFrequencies;

	@SuppressWarnings("unused")
	private final double[] Q;
	private final double[] H;

	private final double[] QiM;
	private final double[] cOne;
	private final double[] cTwo;
	private final double[][] powTempVar;

	@SuppressWarnings("unused")
	private final float omegaFloat;
	private final double omegaDouble;
	private final float mCoeffFloat;
	private final double mCoeffDouble;

	private final double tCritDouble;
	private final double[] dailyPriceValues;

	private final String selectionName;
	private final String displayPeriodString;

	private final BubbleIndexWorker bubbleIndexWorker;
	
	private final RunContext runContext;

	/**
	 * MyGPUCallable constructor.
	 *
	 * @param bubbleIndexWorker   the Swing worker used to publish per-date
	 *                            output messages, or {@code null} in headless
	 *                            mode
	 * @param index               the zero-based offset into
	 *                            {@code dailyPriceValues} that marks the start
	 *                            of the data window for this calculation
	 * @param numberOfDays        the size of the time window in days
	 * @param lombScargle         the pre-computed Lomb-Scargle parameters
	 *                            shared across all GPU callables in a run
	 * @param context             the OpenCL context for the target GPU device
	 * @param queue               the command queue associated with the context
	 * @param program             the compiled OpenCL program containing the
	 *                            kernel
	 * @param kernel              the OpenCL kernel that performs the H,Q
	 *                            derivative and periodogram computation
	 * @param byteOrder           the native byte order of the GPU device,
	 *                            used when allocating NIO buffers
	 * @param tCritDouble         the critical time offset (t<sub>c</sub>) used
	 *                            to build the time-value array
	 * @param dailyPriceValues    the full price series as an array of doubles
	 * @param selectionName       the name of the financial instrument being
	 *                            analysed
	 * @param displayPeriodString the date string of the end of the window,
	 *                            used for display purposes
	 * @param runContext          shared run-time state (stop flag, GUI mode,
	 *                            etc.)
	 */
	public MyGPUCallable(final BubbleIndexWorker bubbleIndexWorker, final int index, final int numberOfDays,
			final LombScargle lombScargle, final CLContext context, final CLQueue queue, final CLProgram program,
			final CLKernel kernel, final ByteOrder byteOrder, final double tCritDouble, final double[] dailyPriceValues,
			final String selectionName, final String displayPeriodString, final RunContext runContext) {
		this.numberOfDays = numberOfDays;
		this.index = index;
		this.context = context;
		this.queue = queue;
		this.kernel = kernel;
		this.byteOrder = byteOrder;
		this.tCritDouble = tCritDouble;
		this.dailyPriceValues = dailyPriceValues;
		this.selectionName = selectionName;
		this.displayPeriodString = displayPeriodString;
		this.bubbleIndexWorker = bubbleIndexWorker;
		this.runContext = runContext;

		this.freqSize = lombScargle.freqSize;
		this.qSize = lombScargle.qSize;
		this.hSize = lombScargle.hSize;
		this.testFrequencies = lombScargle.testFrequencies;
		this.Q = lombScargle.Q;
		this.H = lombScargle.H;

		this.QiM = lombScargle.QiM;
		this.cOne = lombScargle.cOne;
		this.cTwo = lombScargle.cTwo;
		this.powTempVar = lombScargle.powTempVar;

		this.omegaFloat = lombScargle.omegaFloat;
		this.omegaDouble = lombScargle.omegaDouble;
		this.mCoeffFloat = lombScargle.mCoeffFloat;
		this.mCoeffDouble = lombScargle.mCoeffDouble;

	}

	/**
	 * call executes the Bubble Index calculation for a single date using a GPU
	 * device via OpenCL. The method performs the CPU-side matrix setup (price
	 * normalisation, linear regression, and H,Q derivative array construction),
	 * then offloads the Lomb-Scargle periodogram computation to the GPU kernel
	 * and returns the maximum spectral density value across all (H, Q) pairs.
	 *
	 * @return the Bubble Index value for the date corresponding to
	 *         {@code index + window}, or {@code 0.0f} if the time series is all
	 *         zeros, a GPU exception occurred, or a stop signal has been issued
	 */
	@Override
	public Float call() {

		if (!runContext.isStop()) {

			final Pointer<Float> Values = allocateFloats(qSize * hSize).order(byteOrder);
			final Pointer<Float> MeanArray = allocateFloats(qSize * hSize).order(byteOrder);
			final Pointer<Float> logtimeValues = allocateFloats(numberOfDays).order(byteOrder);
			final Pointer<Float> testfreq = allocateFloats(freqSize).order(byteOrder);
			final Pointer<Float> hqderiv = allocateFloats(numberOfDays * (qSize * hSize + 1)).order(byteOrder);

			for (int i = 0; i < freqSize; i++) {
				testfreq.set(i, (float) testFrequencies[i]);
			}

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
				TimeValues_M_Power[k] = FastMath.pow(TimeValues[k], mCoeffFloat);
				SelectedData[k] = dailyPriceValues[k + index + 1];
				if (!containsNonZeroDouble && SelectedData[k] > 0)
					containsNonZeroDouble = true;
			}

			// Return 0.0 if the timeseries === 0
			if (!containsNonZeroDouble) {
				return new Float(0.0f);
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

			final double[] tempSix = new double[numberOfDays];

			Multiply_V64fS64f_V64f(LogTimeValuesTemp, 0, omegaDouble, tempFour, 0, numberOfDays);

			Cos_V64f_V64f(tempFour, 0, tempFive, 0, numberOfDays);
			Sin_V64f_V64f(tempFour, 0, tempSix, 0, numberOfDays);

			final double[] mean = new double[qSize * hSize];

			for (int i = 0; i < qSize; i++) {

				final double Qi_M = QiM[i];
				final double C_one = cOne[i];
				final double C_two = cTwo[i];
				final double[] tempSeven = new double[numberOfDays];
				final double[] tempEight = new double[numberOfDays];
				final double[] tempNine = new double[numberOfDays];

				Multiply_V64fS64f_V64f(tempFive, 0, C_one, tempSeven, 0, numberOfDays);
				Multiply_V64fS64f_V64f(tempSix, 0, C_two, tempEight, 0, numberOfDays);
				Add_V64fV64f_V64f(tempSeven, 0, tempEight, 0, tempNine, 0, numberOfDays);

				for (int p = 0; p < hSize; p++) {

					final double tempVar = powTempVar[i][p];
					final double B_prime, C_prime;

					// Took out the negative sign for B_Prime
					B_prime = 1.0 * (Coef[1]) * (1.0 - Qi_M) * 1.0 / tempVar;

					C_prime = (Coef[2]) * 1.0 / tempVar;

					double sum = 0.0f;

					for (int k = 0; k < numberOfDays; k++) {

						final double G_function = tempNine[k];

						final double tempValue = (FastMath.pow(TimeValues[k], mCoeffDouble - H[p]))
								* (B_prime + C_prime * G_function);
						sum = sum + tempValue;
						hqderiv.set((i + p * qSize) * numberOfDays + k, (float) tempValue);
					}

					mean[i + p * qSize] = sum * 1.0f / numberOfDays;
				}
			}

			// Prepare the arrays to be copied to device memory
			for (int i = 0; i < numberOfDays; i++) {
				logtimeValues.set(i, (float) LogTimeValuesTemp[i]);
			}

			for (int i = 0; i < qSize * hSize; i++) {
				Values.set(i, 0.0f);
				MeanArray.set(i, (float) mean[i]);
			}

			final CLBuffer<Float> values = context.createFloatBuffer(CLMem.Usage.Output, Values),
					meanArray = context.createFloatBuffer(CLMem.Usage.Input, MeanArray),
					LogTimeValues = context.createFloatBuffer(CLMem.Usage.Input, logtimeValues),
					TestFrequencies = context.createFloatBuffer(CLMem.Usage.Input, testfreq),
					HQDerivativeData = context.createFloatBuffer(CLMem.Usage.Input, hqderiv);

			float Temp = 0.0f;

			Pointer<Float> outPtr = allocateFloats(qSize * hSize).order(byteOrder);

			for (int i = 0; i < qSize * hSize; i++) {
				outPtr.set(i, 0.0f);
			}

			try {

				final CLEvent addEvt;
				synchronized (kernel) {

					kernel.setArg(0, values);
					kernel.setArg(1, meanArray);
					kernel.setArg(2, LogTimeValues);
					kernel.setArg(3, TestFrequencies);
					kernel.setArg(4, HQDerivativeData);
					kernel.setArg(5, qSize);
					kernel.setArg(6, hSize);
					kernel.setArg(7, freqSize);
					kernel.setArg(8, numberOfDays);

					final int[] globalSizes = new int[] { qSize, hSize };
					addEvt = kernel.enqueueNDRange(queue, globalSizes);

				}

				outPtr = values.read(queue, addEvt);
				addEvt.release();

				for (int i = 0; i < qSize * hSize; i++) {
					if (outPtr.get(i) > Temp) {
						Temp = outPtr.get(i);
					}
				}

			} catch (final Throwable th) {
				Temp = 0.0f;
				Logs.myLogger.error("GPU Kernel exception. Selection Name = {}. {}", selectionName, th);
			}

			final float output = Temp;

			if (runContext.isGUI()) {
				bubbleIndexWorker.publishText(String.format("Name: %s    Date: %s    Value: %15.2f    Window: %d",
						selectionName, displayPeriodString, output, numberOfDays));
			} else {
				System.out.println(String.format("Name: %s    Date: %s    Value: %15.2f    Window: %d", selectionName,
						displayPeriodString, output, numberOfDays));
			}
			Values.release();
			MeanArray.release();
			logtimeValues.release();
			testfreq.release();
			hqderiv.release();
			outPtr.release();
			values.release();
			meanArray.release();
			LogTimeValues.release();
			HQDerivativeData.release();
			TestFrequencies.release();

			return Temp;
		}

		else {
			return 0.0f;
		}
	}
}
