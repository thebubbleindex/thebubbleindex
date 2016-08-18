package org.thebubbleindex.inputs;

import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

	InputCategory(String name) {
		components = new ArrayList<>();
		this.name = name;
		this.location = Indices.userDir + Indices.programDataFolder + Indices.filePathSymbol + this.name + ".csv";
	}

	InputCategory(String name, String location) {
		components = new ArrayList<>();
		this.name = name;
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setComponents() {
		try {
			final CSVReader reader = new CSVReader(new FileReader(this.location));
			final List<String[]> myEntries = reader.readAll();
			for (final String[] myEntry : myEntries) {
				components.add(myEntry[0]);
			}
		} catch (final FileNotFoundException ex) {
			Logs.myLogger.error("location = {}. {}", this.location, ex);
		} catch (final IOException ex) {
			Logs.myLogger.error("location = {}. {}", this.location, ex);
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
