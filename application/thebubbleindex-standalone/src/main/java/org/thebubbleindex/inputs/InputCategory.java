package org.thebubbleindex.inputs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.thebubbleindex.logging.Logs;

/**
 * InputCategory represents a named category of financial instruments along
 * with its list of individual selection components. The category name is used
 * to locate the corresponding CSV file that lists all component names belonging
 * to the category.
 *
 * @author thebubbleindex
 */
public class InputCategory {

	private String name;
	private String location;
	private final ArrayList<String> components;
	/** File extension appended to each category name to form its component list file. */
	public static final String fileEnding = ".csv";

	/**
	 * InputCategory constructor that derives the file location from the
	 * provided {@link Indices} configuration.
	 *
	 * @param name    the name of the category (e.g. "Currencies")
	 * @param indices the application indices configuration used to build the
	 *                file path
	 */
	public InputCategory(final String name, final Indices indices) {
		components = new ArrayList<String>(100);
		this.name = name;
		this.location = indices.getUserDir() + indices.getProgramDataFolder() + indices.getFilePathSymbol() + this.name
				+ fileEnding;
	}

	/**
	 * InputCategory constructor with an explicit file location path.
	 *
	 * @param name     the name of the category
	 * @param location the absolute file system path to the component list CSV
	 */
	public InputCategory(final String name, final String location) {
		components = new ArrayList<String>(100);
		this.name = name;
		this.location = location;
	}

	/**
	 * setName sets the category name.
	 *
	 * @param name the new category name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * setLocation sets the absolute file system path to the component list CSV
	 * file for this category.
	 *
	 * @param location the file path to the component list
	 */
	public void setLocation(final String location) {
		this.location = location;
	}

	/**
	 * setComponents reads the component list CSV file and populates the
	 * internal components list. If the file does not exist it is created as an
	 * empty file.
	 *
	 * @throws IOException if an error occurs while reading or creating the file
	 */
	public void setComponents() throws IOException {
		try {
			final List<String> lines = Files.readAllLines(new File(location).toPath());
			for (final String line : lines) {
				components.add(line);
			}
		} catch (final FileNotFoundException ex) {
			Logs.myLogger.error("Category List not found, creating it at location = {}", location);
			new File(location).createNewFile();
		} catch (final IOException ex) {
			Logs.myLogger.error("Category List not found, creating it at location = {}", location);
			new File(location).createNewFile();
		}
	}

	/**
	 * getName returns the name of this category.
	 *
	 * @return the category name
	 */
	public String getName() {
		return name;
	}

	/**
	 * getLocation returns the absolute file system path to the component list
	 * CSV file for this category.
	 *
	 * @return the file path to the component list
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * getComponents returns the list of selection component names that belong
	 * to this category.
	 *
	 * @return the list of component names
	 */
	public ArrayList<String> getComponents() {
		return components;
	}

	/**
	 * getComponentsAsArray returns the component names as a plain String array.
	 *
	 * @return an array containing all component names
	 */
	public String[] getComponentsAsArray() {
		String[] stringArray = new String[components.size()];
		stringArray = components.toArray(stringArray);
		return stringArray;
	}
}
