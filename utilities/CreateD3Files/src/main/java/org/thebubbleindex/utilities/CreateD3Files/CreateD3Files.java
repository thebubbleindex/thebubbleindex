package org.thebubbleindex.utilities.CreateD3Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author thebubbleindex
 */
public class CreateD3Files {

	static String programDataFolder = "ProgramData";
	static String categoryList = "CategoryList.csv";
	static String userDir = System.getProperty("user.dir");;
	static Map<String, InputCategory> categoriesAndComponents = new TreeMap<String, InputCategory>();
	static String outputFolder = "/home/green/Desktop/D3/";
	static int maxLength = 252;
	static int[] windows = { 52, 104, 153, 256, 512, 1260, 1764, 2520, 5040, 10080 };
	static String fileType = ".tsv";
	static int threadNumber = 4;
	final static Properties properties = new Properties();
	final static String filePathSymbol = File.separator;

	/**
	 * @param args
	 *            the command line arguments
	 * @throws java.io.IOException
	 * @throws java.lang.InterruptedException
	 */
	public static void main(final String[] args)
			throws IOException, InterruptedException, InterruptedException, InterruptedException, ExecutionException {

		loadProperties(args);
		
		System.out.println("output Folder: " + outputFolder);
		System.out.println("maxLength: " + maxLength);
		System.out.println("programDataFolder: " + programDataFolder);
		System.out.println("userDir: " + userDir);
		System.out.println("categoryList: " + categoryList);
		System.out.println("fileType: " + fileType);

		System.out.println("windows: ");
		for (final int windowPrint : windows) {
			System.out.println(windowPrint);
		}

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
			System.out.println("Category list File Not Found Exception. " + ex);
		} catch (final IOException ex) {
			System.out.println("IO Exception. " + ex);
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
	
	private static void loadProperties(final String[] args) {
		if (args != null && args.length > 0) {
			System.out.println("Loading properties from: " + args[0]);
			try {
				properties.load(new FileInputStream(args[0]));
			} catch (final IOException e) {
				System.out.println("Failed to load properties file: " + args[0]);
			}
		} else {
			try {
				System.out.println("Loading properties from: " + userDir + filePathSymbol + "d3.properties");
				properties.load(new FileInputStream(userDir + filePathSymbol + "d3.properties"));

			} catch (final IOException e) {
				System.out.println("Failed to load properties file.");
			}
		}
		if (properties.containsKey("programDataFolder")) {
			programDataFolder = properties.getProperty("programDataFolder");
		}
		if (properties.containsKey("categoryList")) {
			categoryList = properties.getProperty("categoryList");
		}
		if (properties.containsKey("outputFolder")) {
			outputFolder = properties.getProperty("outputFolder");
		}
		if (properties.containsKey("maxLength")) {
			maxLength = Integer.parseInt(properties.getProperty("maxLength"));
		}
		if (properties.containsKey("windows")) {
			final String[] windowArray = properties.getProperty("windows").split(",");
			windows = new int[windowArray.length];
			for (int i = 0; i < windowArray.length; i++) {
				windows[i] = Integer.parseInt(windowArray[i]);
			}
		}
		if (properties.containsKey("fileType")) {
			fileType = properties.getProperty("fileType");
		}
		if (properties.containsKey("userDir")) {
			userDir = properties.getProperty("userDir");
		}
	}
}
