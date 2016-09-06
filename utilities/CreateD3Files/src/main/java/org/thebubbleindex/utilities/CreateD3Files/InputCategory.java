package org.thebubbleindex.utilities.CreateD3Files;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.util.FastMath;

/**
 *
 * @author bigttrott
 */
public class InputCategory {

	private String name;
	private String location;
	private ArrayList<String> components;
	private String folder;

	InputCategory(final String name) {
		components = new ArrayList<String>();

		this.name = name;

		this.folder = CreateD3Files.userDir + CreateD3Files.filePathSymbol + CreateD3Files.programDataFolder
				+ CreateD3Files.filePathSymbol + this.name + CreateD3Files.filePathSymbol;
		this.location = CreateD3Files.userDir + CreateD3Files.filePathSymbol + CreateD3Files.programDataFolder
				+ CreateD3Files.filePathSymbol + this.name + ".csv";
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
			System.out.println("File Not Found Exception. " + ex);
		} catch (final IOException ex) {
			System.out.println("IOException Exception. " + ex);
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

	void createD3Files(final String outputFolder, final int maxLength, final int[] windows) throws IOException {
		for (final String component : components) {
			final Table<String, Integer, String> d3Table = TreeBasedTable.create();
			for (final int window : windows) {
				try {
					final File file = new File(this.folder + component + CreateD3Files.filePathSymbol + component
							+ String.valueOf(window) + "days.csv");
					if (file.exists()) {
						final List<String> lines = Files
								.readAllLines(new File(this.folder + component + CreateD3Files.filePathSymbol
										+ component + String.valueOf(window) + "days.csv").toPath());
						lines.remove(0);// remove the header
						final int sizeEntries = lines.size();

						if (sizeEntries >= maxLength) {
							for (int i = sizeEntries - maxLength; i < sizeEntries; i++) {
								final String[] lineEntries = lines.get(i).split(",|\\t");
								if (lineEntries.length >= 3) {
									d3Table.put(lineEntries[2], window, lineEntries[1]);
								}
							}
						} else {
							for (final String line : lines) {
								final String[] lineEntries = line.split(",|\\t");
								if (lineEntries.length >= 3) {
									d3Table.put(lineEntries[2], window, lineEntries[1]);
								}
							}
						}
					}
				} catch (final FileNotFoundException ex) {
					System.out.println(ex);
				} catch (final IOException ex) {
					System.out.println(ex);
				}
			}
			writeFileFromTable(d3Table, outputFolder, component, windows, CreateD3Files.fileType);
		}
	}

	private void writeFileFromTable(final Table<String, Integer, String> d3Table, final String outputFolder,
			final String component, final int[] windows, final String fileType) throws IOException {

		final File fileFolder = new File(
				outputFolder + name + CreateD3Files.filePathSymbol + component + CreateD3Files.filePathSymbol);

		final File file = new File(outputFolder + name + CreateD3Files.filePathSymbol + component
				+ CreateD3Files.filePathSymbol + component + fileType);

		if (file.exists()) {
			file.delete();
		}

		fileFolder.mkdirs();
		file.createNewFile();

		switch (fileType) {
		case ".tsv":
			try (final FileWriter writer = new FileWriter(file)) {

				final Iterator<String> rowIterator = d3Table.rowKeySet().iterator();

				// write header
				writer.append("date");

				for (final int window : windows) {
					if (!d3Table.column(window).isEmpty())
						writer.append("\t" + String.valueOf(window) + " Days");
				}
				writer.append("\n");

				while (rowIterator.hasNext()) {
					final String date = rowIterator.next();
					writer.append(date);

					for (final int window : windows) {
						if (!d3Table.column(window).isEmpty()) {

							int dataValue;

							try {
								dataValue = bubbleStandardValue(d3Table.get(date, window), window);
							} catch (final NullPointerException e) {
								dataValue = 0;
							}
							writer.append("\t" + String.valueOf(dataValue));
						}
					}
					writer.append("\n");
				}
				writer.close();
			}
			break;
		}
	}

	private static int bubbleStandardValue(final String value, final int window) {
		final int roundedValue = (int) Math.round(Double.parseDouble(value)
				/ (FastMath.exp(-9.746393 + 3.613444 * FastMath.log(window)) * 2.0 + 550.0) * 100.0);
		return roundedValue;
	}
}
