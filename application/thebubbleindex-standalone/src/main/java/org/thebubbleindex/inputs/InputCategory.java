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
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(this.location));
			final List<String[]> myEntries = reader.readAll();
			for (final String[] myEntry : myEntries) {
				components.add(myEntry[0]);
			}
		} catch (final FileNotFoundException ex) {
			Logs.myLogger.error("location = {}. {}", this.location, ex);
		} catch (final IOException ex) {
			Logs.myLogger.error("location = {}. {}", this.location, ex);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					Logs.myLogger.error("Failed to close reader.");
				}
			}
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
