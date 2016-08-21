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
 * @author windows
 */
public class InputCategory {

	private String name;
	private String location;
	private final ArrayList<String> components;

	InputCategory(final String name) {
		components = new ArrayList<String>();
		this.name = name;
		this.location = Indices.userDir + Indices.programDataFolder + Indices.filePathSymbol + this.name + ".csv";
	}

	InputCategory(final String name, final String location) {
		components = new ArrayList<String>();
		this.name = name;
		this.location = location;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setLocation(final String location) {
		this.location = location;
	}

	public void setComponents() {
		try {
			final List<String> lines = Files.readAllLines(new File(location).toPath());
			for (final String line : lines) {
				components.add(line);
			}
		} catch (final FileNotFoundException ex) {
			Logs.myLogger.error("location = {}. {}", location, ex);
		} catch (final IOException ex) {
			Logs.myLogger.error("location = {}. {}", location, ex);
		}
	}

	public String getName() {
		return this.name;
	}

	public String getLocation() {
		return this.location;
	}

	public ArrayList<String> getComponents() {
		return this.components;
	}

	public String[] getComponentsAsArray() {
		String[] stringArray = new String[this.components.size()];
		stringArray = this.components.toArray(stringArray);
		return stringArray;
	}
}
