package org.thebubbleindex.utilities.compositefiles;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class CompositeIndex {

	private String countryName;
	private int[] dayWindow;
	private double[] quantileValues;
	private HashMap<String, ArrayList<Double>> rawValueMap;
	// private String[] stockNames;
	private List<String> stockNames;
	private int minSize;
	Map<String, Table<String, Integer, Double>> d3Tables;

	static final Map<Integer, String> quantiles;

	private final InputCategory category;

	static {
		quantiles = new HashMap<Integer, String>(5);
		quantiles.put(50, "Fifty");
		quantiles.put(80, "Eighty");
		quantiles.put(90, "Ninety");
		quantiles.put(95, "NinetyFive");
		quantiles.put(99, "NinetyNine");
	}

	CompositeIndex(final InputCategory category, final int[] dayWindow, final double[] quantileValues,
			final int minSize) {
		this.countryName = category.getName();
		this.category = category;
		this.stockNames = category.getComponents();
		this.dayWindow = dayWindow;
		this.quantileValues = quantileValues;
		this.minSize = minSize;
		rawValueMap = new HashMap<String, ArrayList<Double>>();
		d3Tables = new HashMap<String, Table<String, Integer, Double>>(5);
	}

	public void run() {
		System.out.println("Name of country: " + category.getName());
		System.out.println("Number of of stocks: " + stockNames.size());

		for (int i = 0; i < dayWindow.length; i++) {
			for (int j = 0; j < stockNames.size(); j++) {

				final String PreviousFilePath = CreateCompositeFiles.userDir + CreateCompositeFiles.filePathSymbol
						+ "ProgramData" + CreateCompositeFiles.filePathSymbol + countryName
						+ CreateCompositeFiles.filePathSymbol + stockNames.get(j) + CreateCompositeFiles.filePathSymbol
						+ stockNames.get(j) + Integer.toString(dayWindow[i]) + "days.csv";

				final Path filepath = FileSystems.getDefault().getPath(PreviousFilePath);
				if (Files.exists(filepath)) {

					final List<String> dataValues = new ArrayList<String>();
					final List<String> dateValues = new ArrayList<String>();

					InputData.ReadValues(PreviousFilePath, dataValues, dateValues, true, true);

					putRawValues(dataValues, dateValues);
				}

			}
			final String savePath = CreateCompositeFiles.userDir + CreateCompositeFiles.filePathSymbol + "Composite"
					+ CreateCompositeFiles.filePathSymbol + countryName + String.valueOf(dayWindow[i]);
			final String d3savePath = "/home/green/Desktop/D3/";
			calculateQuantileValues(dayWindow[i], savePath, d3savePath);
			rawValueMap.clear();
		}
	}

	private void putRawValues(final List<String> dataValues, final List<String> dateValues) {
		final Iterator<String> dataIterator = dataValues.iterator();
		final Iterator<String> dateIterator = dateValues.iterator();

		if (dataValues.size() != dateValues.size()) {
			System.out.println("Length of lists do not match!");
		} else {
			while (dataIterator.hasNext()) {
				final String date = dateIterator.next();
				final double data = Double.parseDouble(dataIterator.next());

				if (rawValueMap.containsKey(date)) {
					final ArrayList<Double> tempList = rawValueMap.get(date);
					tempList.add(data);
					rawValueMap.put(date, tempList);
				} else {
					final ArrayList<Double> tempList = new ArrayList<Double>();
					tempList.add(data);
					rawValueMap.put(date, tempList);
				}
			}
		}
	}

	void setCountryName(final String countryName) {
		this.countryName = countryName;
	}

	void setDayWindow(final int[] dayWindow) {
		this.dayWindow = dayWindow;
	}

	void setQuantileValues(final double[] quantileValues) {
		this.quantileValues = quantileValues;
	}

	private void calculateQuantileValues(final int dayWindow, final String savePath, final String d3savePath) {

		for (int i = 0; i < quantileValues.length; i++) {

			final String quantileString = String.valueOf((int) (quantileValues[i] * 100));
			final Table<String, Integer, Double> quantileTable;

			if (d3Tables.containsKey(quantileString)) {
				quantileTable = d3Tables.get(quantileString);
			}

			else {
				quantileTable = TreeBasedTable.create();
			}

			final HashMap<String, Double> quantileIndex = new HashMap<String, Double>();

			// iterate over hashmap
			for (Map.Entry<String, ArrayList<Double>> entry : rawValueMap.entrySet()) {
				final String key = entry.getKey();
				final ArrayList<Double> tempList = entry.getValue();
				final double quantileValue = calculateQuantile(quantileValues[i], tempList);
				quantileIndex.put(key, quantileValue);
			}
			final Map<String, Double> sorted = new TreeMap<String, Double>(quantileIndex);
			try {
				System.out.println("Save Path: " + savePath + "Quantile" + quantileString + ".csv");
				InputData.writetoFile(sorted, savePath + "Quantile" + quantileString + ".csv");
				// InputData.createD3File(sorted, d3savePath + "Composite" +
				// quantiles.get((int)(quantileValues[i]*100)) +
				// Indices.filePathSymbol
				// + this.countryName + Indices.filePathSymbol +
				// this.countryName + ".tsv");
				addToTable(quantileTable, sorted, dayWindow);
				if (dayWindow == 1764) {
					System.out.println("Save Path: " + d3savePath + "Composite"
							+ getQuantileName((int) (quantileValues[i] * 100)) + CreateCompositeFiles.filePathSymbol
							+ countryName + CreateCompositeFiles.filePathSymbol + countryName + ".tsv");

					InputData.createD3File(quantileTable,
							d3savePath + "Composite" + getQuantileName((int) (quantileValues[i] * 100))
									+ CreateCompositeFiles.filePathSymbol + countryName
									+ CreateCompositeFiles.filePathSymbol + countryName + ".tsv");
				} else {
					d3Tables.put(quantileString, quantileTable);
				}
			} catch (final IOException ex) {
				// ex.printStackTrace();
				System.out.println("Error writing the file:" + ex);
			}
		}
	}

	private double calculateQuantile(final double quantileValue, final ArrayList<Double> tempList) {

		// remove all zeros
		int i = 0;
		final double EPSILON = 0.01;
		final int SIZE = this.minSize;
		while (i < tempList.size()) {
			if (tempList.get(i) < EPSILON) {
				tempList.remove(i);
			} else {
				i++;
			}
		}

		if (tempList.size() < SIZE)
			return 0.0;
		else {
			int indexValue = (int) Math.round(tempList.size() * quantileValue);
			Collections.sort(tempList);
			if (indexValue == tempList.size()) {
				return tempList.get(indexValue - 1);
			} else {
				return tempList.get(indexValue);
			}
		}
	}

	private void addToTable(final Table<String, Integer, Double> quantileTable, final Map<String, Double> sorted,
			final int dayWindow) {
		for (final Entry<String, Double> entry : sorted.entrySet()) {
			if (entry.getValue() > 0)
				quantileTable.put(entry.getKey(), dayWindow, entry.getValue());
		}
	}

	private String getQuantileName(final int i) {

		switch (i) {
		case 50:
			return "Fifty";
		case 80:
			return "Eighty";
		case 90:
			return "Ninety";
		case 95:
			return "NinetyFive";
		case 99:
			return "NinetyNine";
		default:
			return "";
		}
	}
}
