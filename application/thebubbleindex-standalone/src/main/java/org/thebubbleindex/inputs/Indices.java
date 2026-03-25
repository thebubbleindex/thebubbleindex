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
 * Indices manages the application's category and component configuration. It
 * locates the program data folder relative to the application JAR, reads the
 * top-level category list, and builds the full mapping of categories to their
 * individual {@link InputCategory} components.
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

	/**
	 * initialize reads the category list file and populates the
	 * {@code categoriesAndComponents} map with all known categories and their
	 * associated component selections. If the program data folder or category
	 * list file do not yet exist they are created automatically.
	 */
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

	/**
	 * getCategoriesAsArray returns the names of all known categories as a
	 * sorted String array.
	 *
	 * @return an array of category name strings in alphabetical order
	 */
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

	/**
	 * getUserDir returns the absolute directory path that contains the
	 * application JAR file. All program data is stored relative to this
	 * directory.
	 *
	 * @return the application working directory path, ending with a file
	 *         separator
	 */
	public String getUserDir() {
		return userDir;
	}

	/**
	 * setUserDir overrides the detected user directory with the given path.
	 * Typically used in testing to redirect file I/O to a temporary location.
	 *
	 * @param userDir the directory path to use as the application root
	 */
	public void setUserDir(final String userDir) {
		this.userDir = userDir;
	}

	/**
	 * getProgramDataFolder returns the name of the sub-folder within
	 * {@code userDir} where all application data is stored.
	 *
	 * @return the program data folder name (e.g. "ProgramData")
	 */
	public String getProgramDataFolder() {
		return programDataFolder;
	}

	/**
	 * getCategoryList returns the file name of the top-level category list CSV.
	 *
	 * @return the category list file name (e.g. "CategoryList.csv")
	 */
	public String getCategoryList() {
		return categoryList;
	}

	/**
	 * getFilePathSymbol returns the platform-specific file path separator
	 * character used when constructing directory paths.
	 *
	 * @return the file separator string (e.g. "/" on Unix, "\" on Windows)
	 */
	public String getFilePathSymbol() {
		return filePathSymbol;
	}

	/**
	 * getCategoriesAndComponents returns the full mapping of category names to
	 * their {@link InputCategory} objects, which in turn hold the list of
	 * individual component selection names.
	 *
	 * @return a sorted map of category name to {@link InputCategory}
	 */
	public Map<String, InputCategory> getCategoriesAndComponents() {
		return categoriesAndComponents;
	}
}
