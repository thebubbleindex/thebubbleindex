package org.thebubbleindex.utilities.CreateD3Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
public class CreateD3Files {

	final static String programDataFolder = "ProgramData";
	final static String categoryList = "CategoryList.csv";
	static String userDir;
	final public static String filePathSymbol = File.separator;
	private static Map<String, InputCategory> categoriesAndComponents;
	final private static String outputFolder = "/home/green/Desktop/D3/";
	final private static int maxLength = 252;
	final private static int[] windows = { 52, 104, 153, 256, 512, 1260, 1764, 2520, 5040, 10080 };
	static String Type = ".tsv";
	final private static int threadNumber = 4;

	/**
	 * @param args
	 *            the command line arguments
	 * @throws java.io.IOException
	 * @throws java.lang.InterruptedException
	 */
	public static void main(final String[] args)
			throws IOException, InterruptedException, InterruptedException, InterruptedException, ExecutionException {

		System.out.println("output Folder: " + outputFolder);
		System.out.println("maxLength: " + maxLength);
		System.out.println("windows: ");
		for (final int windowPrint : windows) {
			System.out.println(windowPrint);
		}

		userDir = System.getProperty("user.dir");
		categoriesAndComponents = new TreeMap<>();

		try {
			System.out.println(
					"Input Location: " + userDir + filePathSymbol + programDataFolder + filePathSymbol + categoryList);
			final List<String> lines = Files.readAllLines(
					new File(userDir + filePathSymbol + programDataFolder + filePathSymbol + categoryList).toPath());
			for (final String line : lines) {
				InputCategory tempInputCategory = new InputCategory(line);
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

		for (final Entry<String, InputCategory> entry : categoriesAndComponents.entrySet()) {
			callables.add(new MyCallable(entry.getKey(), entry.getValue(), maxLength, outputFolder, windows));
		}

		final List<Future<Boolean>> futures = executor.invokeAll(callables);

		for (final Future<Boolean> future : futures) {
			System.out.println("Results: " + (future.get() ? "Success" : "Failure"));
		}
		executor.shutdown();
	}
}
