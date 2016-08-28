package org.thebubbleindex.utilities.compositefiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

public class CreateCompositeFiles {
	final static Properties properties = new Properties();
	static String programDataFolder = "ProgramData";
	static String categoryList = "CompositeCategoryList.csv";
	public static String userDir = System.getProperty("user.dir");
	public final static String filePathSymbol = File.separator;
	private static Map<String, InputCategory> categoriesAndComponents = new TreeMap<String, InputCategory>();
	public static String outputFolder = System.getProperty("user.home") + filePathSymbol + "D3" + filePathSymbol;
	public static int maxLength = 252;
	public static int[] windows = { 52, 104, 153, 256, 512, 1260, 1764 };
	public static double[] quantileValues = { 0.5, 0.8, 0.9, 0.95, 0.99 };

	static String fileType = ".tsv";
	final public static int threadNumber = 4;

	public static void main(final String[] args) {

		loadProperties(args);

		System.out.println("User Dir: " + userDir);
		System.out.println("Output Folder: " + outputFolder);
		System.out.println("Program Data Folder: " + programDataFolder);
		System.out.println("Category List: " + categoryList);
		System.out.println("maxLength: " + maxLength);
		System.out.println("Quantile Values: " + Arrays.toString(quantileValues));
		System.out.println("windows: " + Arrays.toString(windows));

		try {
			System.out.println("Reading Category List from Location: " + userDir + filePathSymbol + programDataFolder
					+ filePathSymbol + categoryList);
			final List<String> lines = Files.readAllLines(
					new File(userDir + filePathSymbol + programDataFolder + filePathSymbol + categoryList).toPath());
			for (final String line : lines) {
				final InputCategory tempInputCategory = new InputCategory(line);
				tempInputCategory.setComponents();
				categoriesAndComponents.put(line, tempInputCategory);
			}

		} catch (final FileNotFoundException ex) {
			System.out.println("File Not Found Exception: " + ex);
		} catch (final IOException ex) {
			System.out.println("IO Exception: " + ex);
		}

		for (final Entry<String, InputCategory> entry : categoriesAndComponents.entrySet()) {

			System.out.println("Name of Type: " + entry.getKey());
			final CompositeIndex compositeindex = new CompositeIndex(entry.getValue(), windows, quantileValues,
					(entry.getKey().equals("Stocks")) ? 100 : 30);
			compositeindex.run();
		}
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
				System.out.println("Loading properties from: " + userDir + filePathSymbol + "composite.properties");
				properties.load(new FileInputStream(userDir + filePathSymbol + "composite.properties"));

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
		if (properties.containsKey("quantileValues")) {
			final String[] quantileArray = properties.getProperty("quantileValues").split(",");
			quantileValues = new double[quantileArray.length];
			for (int i = 0; i < quantileArray.length; i++) {
				quantileValues[i] = Double.parseDouble(quantileArray[i]);
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