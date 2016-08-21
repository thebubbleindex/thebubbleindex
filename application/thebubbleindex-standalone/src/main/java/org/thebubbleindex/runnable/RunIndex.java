package org.thebubbleindex.runnable;

import com.nativelibs4java.opencl.CLBuildException;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLDevice;
import com.nativelibs4java.opencl.CLKernel;
import com.nativelibs4java.opencl.CLPlatform;
import com.nativelibs4java.opencl.CLProgram;
import com.nativelibs4java.opencl.CLQueue;
import com.nativelibs4java.opencl.JavaCL;

import java.io.File;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.thebubbleindex.callable.MyCPUCallable;
import org.thebubbleindex.callable.MyGPUCallable;
import org.thebubbleindex.exception.FailedToRunIndex;
import org.thebubbleindex.exception.InvalidData;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.math.LombScargle;
import org.thebubbleindex.swing.BubbleIndexWorker;
import org.thebubbleindex.util.Utilities;

/**
 * RunIndex creates a Run instance with the GPU or CPU execution code
 * 
 * @author ttrott
 */
public class RunIndex {

	final private double[] dailyPriceValues;
	final private int dataSize;
	final private int window;

	final private List<Double> results;
	final private List<String> dailyPriceDate;
	final private double omegaDouble;
	final private double mCoeffDouble;
	final private double tCritDouble;
	final private String selectionName;
	final private String previousFilePath;
	final private BubbleIndexWorker bubbleIndexWorker;

	public static String src;

	/**
	 * RunIndex constructor
	 * 
	 * @param bubbleIndexWorker
	 * @param dailyPriceValues
	 * @param dataSize
	 * @param window
	 * @param results
	 * @param dailyPriceDate
	 * @param previousFilePath
	 * @param selectionName
	 * @param omegaDouble
	 * @param mCoeffDouble
	 * @param tCritDouble
	 */
	public RunIndex(final BubbleIndexWorker bubbleIndexWorker, final double[] dailyPriceValues, final int dataSize,
			final int window, final List<Double> results, final List<String> dailyPriceDate,
			final String previousFilePath, final String selectionName, final double omegaDouble,
			final double mCoeffDouble, final double tCritDouble) {
		this.dailyPriceValues = dailyPriceValues;
		this.dataSize = dataSize;
		this.window = window;
		this.results = results;
		this.dailyPriceDate = dailyPriceDate;
		this.previousFilePath = previousFilePath;
		this.selectionName = selectionName;
		this.omegaDouble = omegaDouble;
		this.mCoeffDouble = mCoeffDouble;
		this.tCritDouble = tCritDouble;
		this.bubbleIndexWorker = bubbleIndexWorker;
	}

	/**
	 * execIndexWithGPU executes The Bubble Index run with a GPU context created
	 * by JavaCL.
	 * <p>
	 * The Run essentially prepares a list of callables containing the numerical
	 * calculation. It then gives these callables to the execution service.
	 * 
	 * @throws FailedToRunIndex
	 */
	public void execIndexWithGPU() throws FailedToRunIndex {

		final LombScargle lombScargle = new LombScargle(70, 18, 19, omegaDouble, mCoeffDouble);

		final List<String> DateList = new ArrayList<String>(10000);
		final List<String> DataList = new ArrayList<String>(10000);

		if (dataSize - window <= 1) {
			throw new FailedToRunIndex("Window larger than Data. Data Size = " + dataSize + " :: Window = " + window);
		}

		final int START_INDEX;

		if (new File(previousFilePath).exists()) {

			Utilities.ReadValues(previousFilePath, DataList, DateList, true, true);

			int UpdateLength = 0;
			try {
				UpdateLength = dailyPriceDate.size() - updateDateMatch(DateList) - 1;
				if (UpdateLength == 0) {
					throw new FailedToRunIndex("No need to run window " + window + ". Fully Updated.");
				}
			} catch (final InvalidData ex) {
				throw new FailedToRunIndex("Error with data while calculating update length..." + ex);
			}

			START_INDEX = dataSize - window - UpdateLength;
		}

		else {
			START_INDEX = 0;
		}

		final int numBatches = (int) Math.ceil((dataSize - window - START_INDEX) / 500.0);

		final List<CLPlatform> platforms = new ArrayList<CLPlatform>(5);
		final List<CLContext> contexts = new ArrayList<CLContext>(5);
		final List<Integer> maxComputeUnits = new ArrayList<Integer>(5);
		final List<CLQueue> queues = new ArrayList<CLQueue>(5);
		final List<CLProgram> programs = new ArrayList<CLProgram>(5);
		final List<CLKernel> addFloatsKernels = new ArrayList<CLKernel>(5);
		final List<ByteOrder> byteOrders = new ArrayList<ByteOrder>(5);

		try {
			final CLPlatform[] platformsArray = JavaCL.listGPUPoweredPlatforms();
			for (final CLPlatform platform : platformsArray) {
				if (RunContext.isGUI) {
					bubbleIndexWorker.publishText(platform.getName());
				} else {
					System.out.println(platform.getName());
				}
				platforms.add(platform);
				final CLDevice[] allDevices = platform.listGPUDevices(true);
				for (final CLDevice device : allDevices) {
					final CLContext context = JavaCL.createContext(null, device);
					contexts.add(context);
					maxComputeUnits.add(device.getMaxComputeUnits());
					final CLQueue queue = context.createDefaultQueue();
					queues.add(queue);
					final String deviceName = device.getName();
					String driverVersion = device.getDriverVersion();
					if (RunContext.isGUI) {
						bubbleIndexWorker
								.publishText("GPU context created with " + deviceName + " :: Driver " + driverVersion);
					} else {
						System.out.println("GPU context created with " + deviceName + " :: Driver " + driverVersion);
					}
					Logs.myLogger.info("GPU context created with {} :: Driver {}", deviceName, driverVersion);
					final CLProgram program = context.createProgram(src);
					programs.add(program);
					final CLKernel kernel = program.createKernel("hq_derivative");
					addFloatsKernels.add(kernel);
					final ByteOrder byteOrder = context.getByteOrder();
					byteOrders.add(byteOrder);
				}
			}

		} catch (final CLBuildException th) {
			Logs.myLogger.error("CLBuildException. Selection Name = {}. {}", selectionName, th);
			if (RunContext.isGUI) {
				bubbleIndexWorker.publishText("No GPU found. Using CPU.");
			} else {
				System.out.println("No GPU found. Using CPU.");
			}
			releaseGPUs(platforms, contexts, programs, addFloatsKernels, queues);
			Logs.myLogger.info("Unable to create GPU context. Executing with CPUs only.");
			execIndexWithCPU();

		}

		for (int batch = 0; batch < numBatches; batch++) {
			final int batchStartIndex = batch * 500 + START_INDEX;
			final int batchEndIndex = (batch + 1) * 500 + START_INDEX;
			final ExecutorService executor = Executors.newFixedThreadPool(RunContext.threadNumber);

			final List<Callable<Float>> callables = new ArrayList<Callable<Float>>(dataSize);

			createGPUCallables(contexts, programs, addFloatsKernels, queues, byteOrders, maxComputeUnits, callables,
					batchStartIndex, Math.min(dataSize - window, batchEndIndex), lombScargle);
			try {
				final List<Future<Float>> tempResults = executor.invokeAll(callables);

				for (final Future<Float> futures : tempResults) {
					results.add((double) futures.get());
				}

				executor.shutdown();
				executor.awaitTermination(5, TimeUnit.SECONDS);
			} catch (final InterruptedException | ExecutionException ex) {
				executor.shutdownNow();
				Logs.myLogger.error("Execution exception. Selection Name = {}. {}", selectionName, ex);
				releaseGPUs(platforms, contexts, programs, addFloatsKernels, queues);

				throw new FailedToRunIndex(ex);
			}
		}
		releaseGPUs(platforms, contexts, programs, addFloatsKernels, queues);
		Logs.myLogger.info("Finished GPU execution.");
	}

	/**
	 * execIndexWithCPU executes The Bubble Index run with only CPU resources.
	 * <p>
	 * The Run essentially prepares a list of callables containing the numerical
	 * calculation. It then gives these callables to the execution service.
	 * 
	 * @throws FailedToRunIndex
	 */
	public void execIndexWithCPU() throws FailedToRunIndex {

		final LombScargle lombScargle = new LombScargle(70, 18, 19, omegaDouble, mCoeffDouble);

		final List<String> DateList = new ArrayList<String>(10000);
		final List<String> DataList = new ArrayList<String>(10000);

		if (dataSize - window <= 1) {
			throw new FailedToRunIndex("Window larger than Data. Data Size = " + dataSize + " :: Window = " + window);
		}

		final int START_INDEX;
		if (new File(previousFilePath).exists()) {

			Utilities.ReadValues(previousFilePath, DataList, DateList, true, true);

			int UpdateLength = 0;

			try {
				UpdateLength = dailyPriceDate.size() - updateDateMatch(DateList) - 1;
				if (UpdateLength == 0) {
					throw new FailedToRunIndex("No need to run index. Fully Updated.");
				}
			} catch (final InvalidData ex) {
				throw new FailedToRunIndex(ex);
			}

			START_INDEX = dataSize - window - UpdateLength;
		}

		else {
			START_INDEX = 0;
		}
		final int numBatches = (int) Math.ceil((dataSize - window - START_INDEX) / 500.0);
		for (int batch = 0; batch < numBatches; batch++) {
			final int batchStartIndex = batch * 500 + START_INDEX;
			final int batchEndIndex = (batch + 1) * 500 + START_INDEX;
			final ExecutorService executor = Executors.newFixedThreadPool(RunContext.threadNumber);

			final List<Callable<Double>> callables = new ArrayList<Callable<Double>>(dataSize);

			for (int j = batchStartIndex; j < Math.min(dataSize - window, batchEndIndex); j++) {
				callables.add(new MyCPUCallable(bubbleIndexWorker, j, window, lombScargle, tCritDouble,
						dailyPriceValues, dailyPriceDate.get(j + window), selectionName));
			}
			try {
				final List<Future<Double>> tempResults = executor.invokeAll(callables);

				for (final Future<Double> futures : tempResults) {
					results.add(futures.get());
				}

				executor.shutdown();
				executor.awaitTermination(5, TimeUnit.SECONDS);
			} catch (final InterruptedException | ExecutionException ex) {
				executor.shutdownNow();
				Logs.myLogger.error("Execution exception. Selection Name = {}. {}", selectionName, ex);

				throw new FailedToRunIndex(ex);
			}
		}
		Logs.myLogger.info("Finished CPU execution.");

	}

	/**
	 * createGPUCallables method forms the list of callables which will be given
	 * to the execution service.
	 * <p>
	 * If only one GPU exists, then all callables will be created with this GPU
	 * as the single context.
	 * <p>
	 * If multiple GPUs exist, then each GPU's processing power is calculated
	 * and compared. Each callable task is then pseudo-randomly assigned to a
	 * specific GPU device context.
	 * 
	 * @param contexts
	 * @param programs
	 * @param addFloatsKernels
	 * @param queues
	 * @param byteOrders
	 * @param maxComputeUnits
	 * @param callables
	 * @param START
	 * @param SIZE
	 * @param lombScargle
	 */
	private void createGPUCallables(final List<CLContext> contexts, final List<CLProgram> programs,
			final List<CLKernel> addFloatsKernels, final List<CLQueue> queues, final List<ByteOrder> byteOrders,
			final List<Integer> maxComputeUnits, final List<Callable<Float>> callables, final int START, final int SIZE,
			final LombScargle lombScargle) {

		final int numberGPUContexts = contexts.size();

		if (numberGPUContexts == 0) {
			throw new FailedToRunIndex("No GPU Contexts found.");
		}

		else if (numberGPUContexts == 1) {
			for (int j = START; j < SIZE; j++) {
				callables.add(new MyGPUCallable(bubbleIndexWorker, j, window, lombScargle, contexts.get(0),
						queues.get(0), programs.get(0), addFloatsKernels.get(0), byteOrders.get(0), tCritDouble,
						dailyPriceValues, selectionName, dailyPriceDate.get(j + window)));
			}
		}

		else {
			int totalComputeUnitsSum = 0;
			for (final Integer computeUnits : maxComputeUnits) {
				totalComputeUnitsSum = totalComputeUnitsSum + computeUnits;
			}

			final Map<CLContext, Double> contextComputeMapping = new LinkedHashMap<>();
			final Map<CLContext, Integer> contextOrder = new HashMap<>();
			double randomSum = 0.0;
			int index = 0;

			for (final CLContext context : contexts) {
				final int computeUnits = context.getDevices()[0].getMaxComputeUnits();// only
																						// one
																						// device
																						// per
																						// context

				randomSum = Math.min(randomSum + computeUnits * 1.0 / totalComputeUnitsSum, 1.0);
				contextComputeMapping.put(context, randomSum);
				contextOrder.put(context, index);
				index++;
			}

			final Random random = new Random(System.currentTimeMillis());

			for (int j = START; j < SIZE; j++) {
				final double randomValue = random.nextDouble();

				for (final Map.Entry<CLContext, Double> entry : contextComputeMapping.entrySet()) {

					if (randomValue <= entry.getValue()) {
						final int contextIndex = contextOrder.get(entry.getKey());
						callables.add(new MyGPUCallable(bubbleIndexWorker, j, window, lombScargle,
								contexts.get(contextIndex), queues.get(contextIndex), programs.get(contextIndex),
								addFloatsKernels.get(contextIndex), byteOrders.get(contextIndex), tCritDouble,
								dailyPriceValues, selectionName, dailyPriceDate.get(j + window)));
						break;
					}
				}
			}
		}
	}

	/**
	 * updateDateMatch helper method to find the how much data needs to be
	 * processed if there already exists a run.
	 * 
	 * @param DateList
	 * @return length of data which is new
	 * @throws InvalidData
	 */
	public int updateDateMatch(final List<String> DateList) throws InvalidData {
		final String finalValue = DateList.get(DateList.size() - 1);

		for (int i = 0; i < dailyPriceDate.size(); i++) {
			if (finalValue.equals(dailyPriceDate.get(i))) {
				return i;
			}
		}

		/*
		 * Since previous file exists but no dates matched, must be error...
		 * start from beginning
		 */
		throw new InvalidData("Previous file exists, but no dates matched.");
	}

	/**
	 * releaseGPUs helper method to clean up the GPU contexts in JavaCL
	 * 
	 * @param platforms
	 * @param contexts
	 * @param programs
	 * @param addFloatsKernels
	 * @param queues
	 */
	private void releaseGPUs(final List<CLPlatform> platforms, final List<CLContext> contexts,
			final List<CLProgram> programs, final List<CLKernel> addFloatsKernels, final List<CLQueue> queues) {

		for (final CLProgram program : programs) {
			program.release();
		}

		for (final CLKernel kernel : addFloatsKernels) {
			kernel.release();
		}

		for (final CLQueue queue : queues) {
			queue.flush();
			queue.release();
		}

		for (final CLContext context : contexts) {
			context.release();
		}

		for (final CLPlatform platform : platforms) {
			platform.release();
		}
	}
}