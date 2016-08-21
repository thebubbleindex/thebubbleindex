package org.thebubbleindex.inputs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.thebubbleindex.data.URLS;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.util.OSValidator;

/**
 *
 * @author ttrott
 * 
 */
public class Indices {

	public final static String programDataFolder = "ProgramData";
	public final static String categoryList = "CategoryList.csv";
	public static String userDir;
	public final static String filePathSymbol = File.separator;
	public final static Map<String, InputCategory> categoriesAndComponents = new TreeMap<>();

	public static void initialize() {

		Logs.myLogger.info("Initializing categories and selections.");

		try {
			userDir = getFilePath();
			Logs.myLogger.info("File path: {}", userDir);

		} catch (final UnsupportedEncodingException ex) {
			Logs.myLogger.error("userDir = {}. {}", userDir, ex);
		}

		if (!new File(userDir + programDataFolder).exists()) {
			Logs.myLogger.error("Unable to find program data folder... creating it.");
			new File(userDir + programDataFolder).mkdirs();
		}

		if (!new File(userDir + programDataFolder + filePathSymbol + categoryList).exists()) {
			Logs.myLogger.error("Unable to find category list... creating it.");
			try {
				new File(userDir + programDataFolder + filePathSymbol + categoryList).createNewFile();
			} catch (final IOException e) {
				Logs.myLogger.error("Unable to create category list file.");
			}
		}

		try {

			Logs.myLogger.info("Reading category list: {}",
					userDir + programDataFolder + filePathSymbol + categoryList);

			final List<String> lines = Files
					.readAllLines(new File(userDir + programDataFolder + filePathSymbol + categoryList).toPath());

			for (final String line : lines) {
				final String categoryName = line;
				final InputCategory tempInputCategory = new InputCategory(categoryName);
				tempInputCategory.setComponents();
				categoriesAndComponents.put(categoryName, tempInputCategory);
			}

		} catch (final FileNotFoundException ex) {
			Logs.myLogger.error("File = {}. {}", userDir + programDataFolder + filePathSymbol + categoryList, ex);
		} catch (final IOException ex) {
			Logs.myLogger.error("File = {}. {}", userDir + programDataFolder + filePathSymbol + categoryList, ex);
		}
	}

	public static String[] getCategoriesAsArray() {
		final String[] categories = new String[categoriesAndComponents.size()];
		int index = 0;
		for (final String category : categoriesAndComponents.keySet()) {
			categories[index] = category;
			index++;
		}
		return categories;
	}

	/**
	 * getFilePath returns the string containing the system address location of
	 * Bubble_Index.jar
	 * 
	 * @return decodedPath the address string without the "Bubble_Index.jar"
	 *         attached
	 * @throws java.io.UnsupportedEncodingException
	 */
	public static String getFilePath() throws UnsupportedEncodingException {
		String path1, decodedPath, Name;
		if (OSValidator.isWindows()) {
			final String rawPath = URLS.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			final String newPath = rawPath.substring(1);
			path1 = newPath.replace("/", "\\");
			decodedPath = URLDecoder.decode(path1, "UTF-8");
			Name = "Bubble_Index.jar";
			final int Size = Name.length();
			decodedPath = decodedPath.substring(0, decodedPath.length() - Size);
		} else {
			path1 = URLS.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			decodedPath = URLDecoder.decode(path1, "UTF-8");
			Name = "Bubble_Index.jar";
			final int Size = Name.length();
			decodedPath = decodedPath.substring(0, decodedPath.length() - Size);
		}

		return decodedPath;
	}
}