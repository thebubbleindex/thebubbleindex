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

import org.thebubbleindex.logging.Logs;

/**
 *
 * @author thebubbleindex
 * 
 */
public class Indices {

	private final String programDataFolder = "ProgramData";
	private final String categoryList = "CategoryList.csv";
	private String userDir;
	private final String filePathSymbol = File.separator;
	private final Map<String, InputCategory> categoriesAndComponents = new TreeMap<String, InputCategory>();

	public void initialize() {

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
				final InputCategory tempInputCategory = new InputCategory(categoryName, this);
				tempInputCategory.setComponents();
				categoriesAndComponents.put(categoryName, tempInputCategory);
			}

			if (lines.isEmpty()) {
				categoriesAndComponents.put("Null", new InputCategory("Null", this));
			}

		} catch (final FileNotFoundException ex) {
			Logs.myLogger.error("File = {}. {}", userDir + programDataFolder + filePathSymbol + categoryList, ex);
		} catch (final IOException ex) {
			Logs.myLogger.error("File = {}. {}", userDir + programDataFolder + filePathSymbol + categoryList, ex);
		}
	}

	public String[] getCategoriesAsArray() {
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
	 * @return path the jar
	 * @throws java.io.UnsupportedEncodingException
	 */
	public String getFilePath() throws UnsupportedEncodingException {
		final String rawPath = Indices.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		final File jarFile = new File(URLDecoder.decode(rawPath, "UTF-8"));
		return jarFile.getParent() + filePathSymbol;
	}

	public String getUserDir() {
		return userDir;
	}

	public void setUserDir(final String userDir) {
		this.userDir = userDir;
	}

	public String getProgramDataFolder() {
		return programDataFolder;
	}

	public String getCategoryList() {
		return categoryList;
	}

	public String getFilePathSymbol() {
		return filePathSymbol;
	}

	public Map<String, InputCategory> getCategoriesAndComponents() {
		return categoriesAndComponents;
	}
}
