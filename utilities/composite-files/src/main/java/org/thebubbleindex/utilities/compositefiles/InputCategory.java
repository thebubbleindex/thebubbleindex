package org.thebubbleindex.utilities.compositefiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class InputCategory {
	private String name;
	private String location;
	private ArrayList<String> components;

	InputCategory(final String name) {
		components = new ArrayList<String>();
		this.name = name;
		this.location = CreateCompositeFiles.userDir + CreateCompositeFiles.filePathSymbol
				+ CreateCompositeFiles.programDataFolder + CreateCompositeFiles.filePathSymbol + this.name + ".csv";
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
			System.out.println("Location of Component: " + location);
			final List<String> lines = Files.readAllLines(new File(location).toPath());
			for (final String line : lines) {
				components.add(line);
			}
		} catch (final FileNotFoundException ex) {
			System.out.println("File Not Found: " + name + " at location: " + location);
		} catch (final IOException ex) {
			System.out.println("File Not Found: " + name + " at location: " + location);
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