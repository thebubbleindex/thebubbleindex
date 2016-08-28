package org.thebubbleindex.utilities.compositefiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class CreateCompositeFiles {
	final static String programDataFolder = "ProgramData";
	final static String categoryList = "CompositeCategoryList.csv";
	static String userDir;
	public final static String filePathSymbol = File.separator;
	private static Map<String, InputCategory> categoriesAndComponents;
	final private static String outputFolder = "/home/green/Desktop/D3/";
	final private static int maxLength = 252;
	final private static int[] windows = { 52, 104, 153, 256, 512, 1260, 1764 };
	final private static double[] quantileValues = { 0.5, 0.8, 0.9, 0.95, 0.99 };

	static String fileType = ".tsv";
	final public static int threadNumber = 4;

	public static void main(final String[] args) {

		System.out.println("output Folder: " + outputFolder);
		System.out.println("maxLength: " + maxLength);
		System.out.println("Quantile Values: " + Arrays.toString(quantileValues));
		System.out.println("windows: " + Arrays.toString(windows));

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

		for (final Entry<String, InputCategory> entry : categoriesAndComponents.entrySet()) {

			System.out.println("Name of Type: " + entry.getKey());
			final CompositeIndex compositeindex = new CompositeIndex(entry.getValue(), windows, quantileValues,
					(entry.getKey().equals("Stocks")) ? 100 : 30);
			compositeindex.run();
		}
	}
}