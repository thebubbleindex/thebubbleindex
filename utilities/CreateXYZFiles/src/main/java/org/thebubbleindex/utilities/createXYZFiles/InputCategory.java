package org.thebubbleindex.utilities.createXYZFiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.apache.commons.math3.util.FastMath;

/**
 *
 * @author thebubbleindex
 */
public class InputCategory {

	private String name;
	private String location;
	private final ArrayList<String> components;
	private String folder;

	InputCategory(final String name) {
		components = new ArrayList<String>();

		this.name = name;

		this.folder = CreateXYZFiles.userDir + CreateXYZFiles.filePathSymbol + CreateXYZFiles.programDataFolder
				+ CreateXYZFiles.filePathSymbol + this.name + CreateXYZFiles.filePathSymbol;
		this.location = CreateXYZFiles.userDir + CreateXYZFiles.filePathSymbol + CreateXYZFiles.programDataFolder
				+ CreateXYZFiles.filePathSymbol + this.name + ".csv";
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
		return components;
	}

	public String[] getComponentsAsArray() {
		String[] stringArray = new String[components.size()];
		stringArray = components.toArray(stringArray);
		return stringArray;
	}

	public void createXYZfiles(final String outputFolder, final Integer[] windows) throws IOException {
		for (final String component : components) {
			System.out.println("Reading component: " + component);
			final Map<String, int[]> allDataTable = new HashMap<String, int[]>(42000);
			final List<Integer> windowsExist = new ArrayList<Integer>(windows.length);
			int maximumValue = 0;
			int dailyDataSize = 5000;

			final File dailyData = new File(
					folder + component + CreateXYZFiles.filePathSymbol + component + "dailydata.csv");

			if (dailyData.exists()) {
				final String dailyDataAsString = new String(Files.readAllBytes(dailyData.toPath()));
				dailyDataSize = dailyDataAsString.split(System.getProperty("line.separator")).length;
			}

			for (int i = 0; i < windows.length; i++) {

				final File file = new File(folder + component + CreateXYZFiles.filePathSymbol + component
						+ String.valueOf(windows[i]) + "days.csv");
				if (file.exists()) {

					windowsExist.add(windows[i]);

					final String fileAsString = new String(Files.readAllBytes(file.toPath()));
					final String[] fileLines = fileAsString.split(System.getProperty("line.separator"));

					for (int j = 1; j < fileLines.length; j++) {
						final String[] line = fileLines[j].split(",");
						if (line.length >= 3) {
							try {
								final int value = bubbleStandardValue(line[1], windows[i]);
								if (value > maximumValue)
									maximumValue = value;
								if (allDataTable.containsKey(line[2])) {
									allDataTable.get(line[2])[i] = value;
								} else {
									final int[] valueArray = new int[windows.length];
									valueArray[i] = value;
									allDataTable.put(line[2], valueArray);
								}
							} catch (final Exception ex) {
								System.out.println("Exception. Component: " + component + ex);

							}
						}
					}
				}
			}

			Integer[] actualWindows = new Integer[windowsExist.size()];
			actualWindows = windowsExist.toArray(actualWindows);

			writeXYZFile(allDataTable, actualWindows, outputFolder, component, maximumValue, dailyDataSize);
			createNetGridFile(allDataTable, actualWindows, outputFolder, component, maximumValue, dailyDataSize);
		}
	}

	private static int bubbleStandardValue(final String value, final Integer window) {
		final int roundedValue = (int) Math.round(Double.parseDouble(value)
				/ (FastMath.exp(-9.746393 + 3.613444 * FastMath.log(window)) * 2.0 + 550.0) * 100.0);
		return roundedValue;
	}

	private void writeXYZFile(final Map<String, int[]> allDataTable, final Integer[] windows, final String outputFolder,
			final String component, final int max, final int dailyDataSize) throws IOException {

		final double scale = dailyDataSize * CreateXYZFiles.stdConstant / (double) max;
		final TreeSet<String> rowSet = new TreeSet<String>(allDataTable.keySet());

		final String[] rowsArray = rowSet.toArray(new String[rowSet.size()]);

		int index = 1;
		final List<byte[]> byteListOutputFile = new ArrayList<byte[]>(rowsArray.length);

		for (final String date : rowsArray) {
			final int[] valueArray = allDataTable.get(date);

			for (int i = 0; i < windows.length; i++) {
				int value;
				try {
					value = valueArray[i];
				} catch (final Exception ex) {
					value = -1;
				}
				if (value != -1) {
					try {
						final String temp = (String.valueOf(index) + "," + String.valueOf(windows[i]) + ","
								+ String.valueOf((int) (value * scale)) + "\n");
						final byte[] b = temp.getBytes(Charset.forName("UTF-8"));
						byteListOutputFile.add(b);
					} catch (final Exception e) {
						System.out.print(component + " " + windows[i] + " " + e);
					}
				}
			}
			index++;
		}

		final File fileFolder = new File(
				outputFolder + name + CreateXYZFiles.filePathSymbol + component + CreateXYZFiles.filePathSymbol);
		final File file = new File(outputFolder + name + CreateXYZFiles.filePathSymbol + component
				+ CreateXYZFiles.filePathSymbol + component + CreateXYZFiles.fileType);

		if (file.exists()) {
			file.delete();
		}

		fileFolder.mkdirs();

		try (final BufferedWriter fw = Files.newBufferedWriter(file.toPath(), Charset.forName("UTF-8"));) {
			for (final byte[] line : byteListOutputFile) {
				fw.write(new String(line));
			}
		}

		final File maxValueFile = new File(outputFolder + name + CreateXYZFiles.filePathSymbol + component
				+ CreateXYZFiles.filePathSymbol + "maxValue" + CreateXYZFiles.fileType);

		if (maxValueFile.exists()) {
			maxValueFile.delete();
		}

		try (final BufferedWriter fw = Files.newBufferedWriter(maxValueFile.toPath(), Charset.forName("UTF-8"));) {
			fw.write(String.valueOf(max));
		}
	}

	public static <T extends Comparable<? super T>> List<T> asSortedList(final Collection<T> c) {
		final List<T> list = new ArrayList<T>(c);
		java.util.Collections.sort(list);
		return list;
	}

	private void createNetGridFile(final Map<String, int[]> allDataTable, final Integer[] windows,
			final String outputFolder, final String component, final int max, final int dailyDataSize)
			throws IOException {

		final int squareSide = (CreateXYZFiles.squareSize != 0) ? CreateXYZFiles.squareSize : 80;
		final String squareSideString = String.format("%.6f", (float) squareSide);
		final String doubleSquareSideString = String.format("%.6f", (float) squareSide * 2);
		final int numberOfDates = dailyDataSize;
		int largestWindow = 0;
		for (final Integer window : windows) {
			if (window > largestWindow) {
				largestWindow = window;
			}
		}

		final int largest = (largestWindow > numberOfDates) ? largestWindow : numberOfDates;

		final int x_len = largest / squareSide;

		final File fileFolder = new File(
				outputFolder + name + CreateXYZFiles.filePathSymbol + component + CreateXYZFiles.filePathSymbol);

		final File file = new File(outputFolder + name + CreateXYZFiles.filePathSymbol + component
				+ CreateXYZFiles.filePathSymbol + "EqNet.net");

		if (file.exists()) {
			file.delete();
		}

		fileFolder.mkdirs();

		try (final BufferedWriter fw = Files.newBufferedWriter(file.toPath(), Charset.forName("UTF-8"));) {

			fw.write("EqNet:01\r\n");
			fw.write("0\r\n");
			fw.write("0.00000\r\n");
			fw.write("0.000000 0.000000 " + String.format("%.6f", (float) largest) + " "
					+ String.format("%.6f", (float) largest) + "\r\n");
			fw.write(String.valueOf(x_len + 1) + "\r\n");
			for (int i = 0; i < x_len; i++) {
				if (i == 0)
					fw.write(doubleSquareSideString + "\r\n");
				else
					fw.write(squareSideString + "\r\n");
			}

			fw.write(String.valueOf(x_len + 1) + "\r\n");
			for (int i = 0; i < x_len; i++) {
				if (i == 0)
					fw.write(doubleSquareSideString + "\r\n");
				else
					fw.write(squareSideString + "\r\n");
			}
		}
	}
}
