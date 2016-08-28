package org.thebubbleindex.utilities.createXYZFiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author bigttrott
 */
public class CreateXYZFiles {

	final static String programDataFolder = "ProgramData";
	final static String categoryList = "CategoryList.csv";
	static String userDir;
	final public static String filePathSymbol = File.separator;
	private static Map<String, InputCategory> categoriesAndComponents;
	final private static String outputFolder = "/home/green/Desktop/XYZConversion/";
	final private static Integer[] windows = { 52, 100, 104, 125, 153, 175, 200, 225, 250, 256, 275, 300, 325, 350, 375,
			400, 425, 450, 475, 500, 512, 525, 550, 575, 600, 625, 650, 675, 700, 725, 750, 775, 800, 825, 850, 875,
			900, 925, 950, 975, 1000, 1050, 1100, 1150, 1200, 1260, 1500, 1700, 1764, 2000, 2200, 2520, 2700, 3000,
			3200, 3500, 3700, 4000, 4200, 4500, 4700, 5040, 5200, 5500, 5700, 6000, 6200, 6500, 6700, 7000, 7200, 7500,
			7700, 8000, 8100, 8200, 8300, 8400, 8500, 8600, 8700, 9000, 9200, 9500, 9700, 10080, 10200, 10500, 10700,
			11000, 11200, 11500, 11700, 12000, 12200, 12500, 12700, 13000, 13200, 13500, 13700, 14000, 14200, 14500,
			14700, 15000, 15200, 15500, 15700, 16000, 16200, 16500, 16700, 17000, 17200, 17500, 17700, 18000, 18200,
			18500, 18700, 19000, 19200, 19500, 19700, 20000, 20160, 20500, 20700, 21000, 21200, 21500, 21700, 22000,
			22200, 22500, 22700, 23000, 23200, 23500, 23700, 24000, 24200, 24500, 24700, 25000, 25200, 25500, 25700,
			26000, 26200, 26500, 26700, 27000, 27200, 27500, 27700, 28000, 28200, 28500, 28700, 29000, 29200, 29500,
			29700, 30000, 30200, 30500, 30700, 31000, 31200, 31500, 31700, 32000, 32200, 32500, 32700, 33000, 33200,
			33500, 33700, 34000, 34200, 34500, 34700, 35000, 35200, 35500 };

	static String Type = ".csv";
	final private static int threadNumber = Runtime.getRuntime().availableProcessors();
	final private static int arraySize = 50000;
	final public static double stdMaxValue = 10000.0;
	final public static double stdConstant = 1.0;
	public static int squareSize = 0;

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		squareSize = (args.length > 0) ? Integer.parseInt(args[0]) : 0;

		System.out.println("output Folder: " + outputFolder);
		System.out.println("windows: ");

		userDir = System.getProperty("user.dir");
		categoriesAndComponents = new TreeMap<>();

		try {
			System.out.println(
					"Input Location: " + userDir + filePathSymbol + programDataFolder + filePathSymbol + categoryList);
			final List<String> lines = Files.readAllLines(
					new File(userDir + filePathSymbol + programDataFolder + filePathSymbol + categoryList).toPath());
			for (final String line : lines) {
				final InputCategory tempInputCategory = new InputCategory(line);
				tempInputCategory.setComponents();
				categoriesAndComponents.put(line, tempInputCategory);
			}
		} catch (final FileNotFoundException ex) {
			System.out.println("File Not Found Exception. Code 008." + ex);
		} catch (final IOException ex) {
			System.out.println("IO Exception. Code 009." + ex);
		}

		final ExecutorService executor = Executors.newFixedThreadPool(threadNumber);

		final List<Callable<Boolean>> callables = new ArrayList<>();

		for (final Map.Entry<String, InputCategory> entry : categoriesAndComponents.entrySet()) {
			callables.add(new MyCallable(entry.getKey(), entry.getValue(), outputFolder, windows));
		}

		final List<Future<Boolean>> futures = executor.invokeAll(callables);

		for (final Future<Boolean> future : futures) {
			System.out.println("Results: " + (future.get() ? "Success" : "Failure"));
		}

		executor.shutdown();
	}
}
