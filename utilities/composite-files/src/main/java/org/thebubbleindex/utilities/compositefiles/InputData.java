package org.thebubbleindex.utilities.compositefiles;

import com.google.common.collect.Table;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Scanner;
import org.apache.commons.math3.util.FastMath;

public class InputData {

	/**
	 * ReadValues reads an external file containing two columns separated by
	 * either a tab or comma, storing each column into its own string list
	 * 
	 * @param CSVLocation
	 *            the string with the address of the file containing the data
	 *            with two columns
	 * @param DateList
	 *            string list containing the first column
	 * @param DataList
	 *            string list containing the second column
	 * @param firstLine
	 *            indicates whether the file contains a header
	 * @param update
	 *            indicates whether the file is a previously created Bubble
	 *            Index If true then the file being read has name =
	 *            'DJIA''1764'days.csv
	 * @return void
	 */
	public static void ReadValues(final String CSVLocation, final List<String> ColumnOne, final List<String> ColumnTwo,
			final boolean firstLine, final boolean update) {

		Scanner s = null;
		try {
			s = new Scanner(new BufferedReader(new FileReader(CSVLocation)));
			String line;

			// check for header
			if (firstLine) {
				s.nextLine();
			}
			while (s.hasNext()) {
				line = s.nextLine();
				final Scanner lineScan = new Scanner(line);
				lineScan.useDelimiter(",|\t");
				/*
				 * When updating... The Bubble Index files contain three
				 * columns. The first column is not needed.
				 */
				if (update) {
					lineScan.next();
					ColumnOne.add(lineScan.next());
					ColumnTwo.add(lineScan.next());
				}

				else {
					ColumnOne.add(lineScan.next());
					ColumnTwo.add(lineScan.next());
				}
				lineScan.close();
			}
		} catch (final IOException | NoSuchElementException ex) {
		} finally {
			if (s != null) {
				s.close();
			}
		}
	}

	public static void writetoFile(final Map<String, Double> quantileIndex, final String savePath) throws IOException {

		FileWriter writer;
		final Path filepath = new File(savePath).toPath();
		System.out.println("Write To: " + filepath);
		if (Files.exists(filepath)) {
			Files.delete(filepath);
		}
		try {
			writer = new FileWriter(savePath);

			for (final Entry<String, Double> entry : quantileIndex.entrySet()) {
				String key = entry.getKey();
				double temp = entry.getValue();
				writer.append(key);
				writer.append(",");
				writer.append(String.valueOf(temp));
				writer.append("\n");
			}
			writer.flush();
			writer.close();
		} catch (final IOException ex) {
			System.out.println("File Writer failed");
		}
	}

	static void createD3File(final Table<String, Integer, Double> d3Table, final String d3savePath) throws IOException {

		final int windows[] = new int[d3Table.columnKeySet().size()];
		final int tableSize = d3Table.rowKeySet().size();
		System.out.println("Table size: " + tableSize);
		final List<String> rows = new ArrayList<String>();

		if (tableSize > CreateCompositeFiles.maxLength) {
			final Iterator<String> rowIterator = d3Table.rowKeySet().iterator();
			int itIndex = 0;
			while (rowIterator.hasNext()) {

				final String row = rowIterator.next();

				if (itIndex >= tableSize - CreateCompositeFiles.maxLength) {
					rows.add(row);
				}
				itIndex = itIndex + 1;
			}
			System.out.println(
					"Table larger than " + CreateCompositeFiles.maxLength + ". Added " + rows.size() + " rows");
		} else {
			final Iterator<String> rowIterator = d3Table.rowKeySet().iterator();

			while (rowIterator.hasNext()) {
				rows.add(rowIterator.next());
			}
			System.out.println(
					"Table smaller than " + CreateCompositeFiles.maxLength + ". Added " + rows.size() + " rows");
		}

		int i = 0;
		for (final Integer col : d3Table.columnKeySet()) {
			windows[i++] = col;
		}

		final File file = new File(d3savePath);

		if (file.exists()) {
			file.delete();
		}
		file.getParentFile().mkdirs();

		file.createNewFile();

		try (final FileWriter writer = new FileWriter(file)) {

			// write header
			writer.append("date");

			for (final int window : windows) {
				if (!d3Table.column(window).isEmpty())
					writer.append("\t" + String.valueOf(window) + " Days");
			}

			writer.append("\n");

			for (final String row : rows) {

				writer.append(row);

				for (final int window : windows) {
					if (!d3Table.column(window).isEmpty()) {

						int dataValue;

						try {
							dataValue = bubbleStandardValue(d3Table.get(row, window), window);
						} catch (NullPointerException e) {
							dataValue = 0;
						}
						writer.append("\t" + String.valueOf(dataValue));
					}
				}
				writer.append("\n");
			}
			writer.close();
		}
	}

	private static int bubbleStandardValue(final Double value, final int window) {
		final int roundedValue = (int) Math
				.round(value / (FastMath.exp(-9.746393 + 3.613444 * FastMath.log(window)) * 2.0 + 550.0) * 100.0);
		return roundedValue;
	}
}
