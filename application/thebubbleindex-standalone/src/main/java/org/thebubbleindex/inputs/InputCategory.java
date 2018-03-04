package org.thebubbleindex.inputs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.thebubbleindex.logging.Logs;

/**
 *
 * @author thebubbleindex
 */
public class InputCategory {

	private String name;
	private String location;
	private final ArrayList<String> components;
	public static final String fileEnding = ".csv";

	public InputCategory(final String name, final Indices indices) {
		components = new ArrayList<String>(100);
		this.name = name;
		this.location = indices.getUserDir() + indices.getProgramDataFolder() + indices.getFilePathSymbol() + this.name
				+ fileEnding;
	}

	public InputCategory(final String name, final String location) {
		components = new ArrayList<String>(100);
		this.name = name;
		this.location = location;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setLocation(final String location) {
		this.location = location;
	}

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

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public ArrayList<String> getComponents() {
		return components;
	}

	public String[] getComponentsAsArray() {
		String[] stringArray = new String[components.size()];
		stringArray = components.toArray(stringArray);
		return stringArray;
	}
}
