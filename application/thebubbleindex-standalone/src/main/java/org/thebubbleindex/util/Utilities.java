package org.thebubbleindex.util;

import static info.yeppp.Core.Multiply_V64fV64f_V64f;
import static info.yeppp.Core.Subtract_V64fV64f_V64f;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import javax.swing.SwingUtilities;

import org.apache.commons.math3.util.FastMath;
import org.thebubbleindex.exception.FailedToRunIndex;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.swing.GUI;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

/**
 *
 * @author thebubbleindex
 */
public class Utilities implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -196637027374023030L;

	/**
	 * 
	 * @param displayText
	 * @param resetTextArea
	 */
	public static void displayOutput(final RunContext runContext, final String displayText,
			final boolean resetTextArea) {
		if (runContext.isGUI()) {
			final int numberOfLines = runContext.incrementAndGetNumberOfLines();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (resetTextArea || numberOfLines > 200) {
						GUI.OutputText.setText("");
						runContext.resetNumberOfLines();
					}
					GUI.OutputText.append(displayText + "\n");
				}
			});
		} else {
			Logs.myLogger.info("{}", displayText);
		}
	}

	/**
	 * DataReverse method takes an array and reverses the linear order of the
	 * array.
	 * 
	 * @param data
	 *            The array to be reversed
	 * @param size
	 *            The size of the array
	 */
	public static void DataReverse(final double[] data, final int size) {
		final int halfSize = size / 2;
		for (int i = 0; i < halfSize; i++) {
			final double tempDouble = data[i];
			data[i] = data[size - 1 - i];
			data[size - 1 - i] = tempDouble;
		}
	}

	/**
	 * Normalize takes the data, calculates the returns. Then normalizes the
	 * price data series to begin at a value of 100.0 and takes the log
	 * 
	 * @param SelectedData
	 * @param NumberOfDays
	 */
	public static void Normalize(final double[] SelectedData, final int NumberOfDays) {

		final double[] tempOne = new double[NumberOfDays];
		final double[] tempTwo = new double[NumberOfDays];
		final double[] tempThree = new double[NumberOfDays];
		final double[] tempFour = new double[NumberOfDays];
		final double[] tempFive = new double[NumberOfDays];

		for (int i = 1; i < NumberOfDays; i++) {
			tempOne[i] = SelectedData[i];
			tempTwo[i] = SelectedData[i - 1];
			tempThree[i] = 1.0 / SelectedData[i - 1];
		}

		Subtract_V64fV64f_V64f(tempOne, 0, tempTwo, 0, tempFour, 0, NumberOfDays);

		Multiply_V64fV64f_V64f(tempFour, 0, tempThree, 0, tempFive, 0, NumberOfDays);

		SelectedData[0] = FastMath.log(100.0);
		double tempVar = 100.0;
		for (int i = 1; i < NumberOfDays; i++) {
			tempVar = tempVar * tempFive[i] + tempVar;
			SelectedData[i] = FastMath.log(tempVar);
		}
	}

	/**
	 * LinearFit method solves the b = Ax matrix for the x vector. In other
	 * words this is a linear regression to fit the equation to the log prices.
	 * The equation is y = b + Ax1 + Bx2 where x1 and x2 are TimeValues_M_power
	 * and LogCosTimeValues respectively.
	 * 
	 * @param Data
	 *            The array containing the log prices
	 * @param TimeValues_M_Power
	 *            Array of time values raised to the M power
	 * @param LogCosTimeValues
	 *            Array of logcos time values
	 * @param Coef
	 *            Array containing the models fitted coefficients
	 * @param SIZE
	 *            Size of the data window (days)
	 */
	public static void LinearFit(final double[] Data, final double[] TimeValues_M_Power,
			final double[] LogCosTimeValues, final double[] Coef, final int SIZE) {

		final double[][] array = new double[SIZE][3];

		for (int i = 0; i < SIZE; i++) {
			array[i][0] = 1.0;
			array[i][1] = TimeValues_M_Power[i];
			array[i][2] = LogCosTimeValues[i];
		}
		final Matrix A = new DenseMatrix(array);
		final Vector b = new DenseVector(Data);
		Vector x = new DenseVector(3);
		x = A.solve(b, x);
		for (int i = 0; i < 3; i++) {
			Coef[i] = x.get(i);
		}
	}

	/**
	 * ReadValues reads an external file containing two columns separated by
	 * either a tab or comma, storing each column into its own string list.
	 * 
	 * @param locationPath
	 * @param ColumnOne
	 * @param ColumnTwo
	 * @param firstLine
	 * @param update
	 * @throws FailedToRunIndex
	 */
	public static void ReadValues(final String locationPath, final List<String> ColumnOne, final List<String> ColumnTwo,
			final boolean firstLine, final boolean update) throws FailedToRunIndex {
		try {
			final List<String> lines = Files.readAllLines(Paths.get(locationPath), Charset.defaultCharset());
			int index = 0;
			for (final String line : lines) {
				// check for header
				if (index == 0 && firstLine) {
					index++;
					continue;
				}
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
				index++;
			}

		} catch (final Exception ex) {
			Logs.myLogger.error("Error while reading file = {}. {}", locationPath, ex);
			throw new FailedToRunIndex(ex);
		}
	}
	
	/**
	 * ReadByteValues reads a byte array representation of file containing two columns separated by
	 * either a tab or comma, storing each column into its own string list.
	 * 
	 * @param fileBytes
	 * @param ColumnOne
	 * @param ColumnTwo
	 * @param firstLine
	 * @param update
	 * @throws FailedToRunIndex
	 */
	public static void ReadByteValues(final byte[] fileBytes, final List<String> ColumnOne, final List<String> ColumnTwo,
			final boolean firstLine, final boolean update) throws FailedToRunIndex {
		try {
			final String fileAsString = new String(fileBytes);
			final String[] lines = fileAsString.split("\\r?\\n");
			int index = 0;
			for (final String line : lines) {
				// check for header
				if (index == 0 && firstLine) {
					index++;
					continue;
				}
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
				index++;
			}
		} catch (final Exception ex) {
			Logs.myLogger.error("Error while reading bytes of file.", ex);
			throw new FailedToRunIndex(ex);
		}
	}

	/**
	 * WriteCSV writes an output file
	 * 
	 * @param savePath
	 * @param Results
	 * @param PERIODS
	 * @param FileName
	 * @param dailyPriceDate
	 * @param UPDATE
	 * @throws IOException
	 */
	public static void WriteCSV(final String savePath, final List<Double> Results, final int PERIODS,
			final String FileName, final List<String> dailyPriceDate, final boolean UPDATE) throws IOException {

		FileWriter writer = null;

		try {
			writer = new FileWriter(savePath + File.separator + FileName, UPDATE);

			if (!UPDATE) {
				addHeader(writer);
			}

			for (int i = 0; i < Results.size(); i++) {

				writer.append(Integer.toString(PERIODS - Results.size() + i + 1));
				writer.append(',');
				writer.append(String.valueOf(Results.get(i)));
				writer.append(',');
				writer.append(dailyPriceDate.get(dailyPriceDate.size() - Results.size() + i));
				writer.append('\n');
			}

			writer.flush();
			writer.close();

		} catch (final IOException ex) {
			Logs.myLogger.error("save path = {}. {}", savePath, ex);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	/**
	 * addHeader writes header of output file
	 * 
	 * @param writer
	 * @throws IOException
	 */
	private static void addHeader(final FileWriter writer) throws IOException {
		writer.append("Period Number");
		writer.append(',');
		writer.append("Value");
		writer.append(',');
		writer.append("Date");
		writer.append('\n');
	}
}
