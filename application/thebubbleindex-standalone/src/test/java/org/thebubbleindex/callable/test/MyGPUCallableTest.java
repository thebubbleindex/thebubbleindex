package org.thebubbleindex.callable.test;

import org.junit.Test;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.runnable.RunIndex;
import com.nativelibs4java.util.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static org.junit.Assert.assertEquals;

public class MyGPUCallableTest {

	final double epsilon = 0.01;
	final String fileSep = File.separator;

	@Test
	public void resultsShouldMatchBITSTAMPUSD() throws IOException, URISyntaxException {
		final String selectionName = "BITSTAMPUSD";
		final String folder = "ProgramData";
		final String folderType = "Currencies";
		final double omegaDouble = 6.28;
		final double mCoeffDouble = 0.38;
		final double tCritDouble = 21.0;
		final String pathRoot = folder + fileSep + folderType + fileSep + selectionName + fileSep + selectionName;

		final URL dailyDataUrl = getClass().getClassLoader().getResource(pathRoot + "dailydata.csv");
		final Path dailyDataPath = new File(dailyDataUrl.toURI()).toPath();

		final List<String> lines = Files.readAllLines(dailyDataPath, Charset.defaultCharset());
		final List<String> dailyPriceDate = new ArrayList<String>();
		final List<Double> priceValues = new ArrayList<Double>();
		final List<Double> results = new ArrayList<Double>();

		parseDailyData(lines, priceValues, dailyPriceDate);

		final int dataSize = dailyPriceDate.size();
		final double[] dailyPriceValues = new double[dataSize];
		for (int i = 0; i < dataSize; i++) {
			dailyPriceValues[i] = priceValues.get(i);
		}

		RunIndex.src = IOUtils.readText(RunIndex.class.getResource("/GPUKernel.cl"));
		RunContext.threadNumber = 4;

		// 52 day window test
		int window = 52;
		String previousFilePath = pathRoot + String.valueOf(window) + "days.csv";
		RunIndex runIndex = new RunIndex(null, dailyPriceValues, dataSize, window, results, dailyPriceDate,
				previousFilePath, selectionName, omegaDouble, mCoeffDouble, tCritDouble);
		runIndex.execIndexWithGPU();
		compareResults(selectionName, window, results);
		results.clear();

		// 104 day window test
		window = 104;
		previousFilePath = pathRoot + String.valueOf(window) + "days.csv";
		runIndex = new RunIndex(null, dailyPriceValues, dataSize, window, results, dailyPriceDate, previousFilePath,
				selectionName, omegaDouble, mCoeffDouble, tCritDouble);
		runIndex.execIndexWithGPU();
		compareResults(selectionName, window, results);
		results.clear();

		// 153 day window test
		window = 153;
		previousFilePath = pathRoot + String.valueOf(window) + "days.csv";
		runIndex = new RunIndex(null, dailyPriceValues, dataSize, window, results, dailyPriceDate, previousFilePath,
				selectionName, omegaDouble, mCoeffDouble, tCritDouble);
		runIndex.execIndexWithGPU();
		compareResults(selectionName, window, results);
		results.clear();

		// 256 day window test
		window = 256;
		previousFilePath = pathRoot + String.valueOf(window) + "days.csv";
		runIndex = new RunIndex(null, dailyPriceValues, dataSize, window, results, dailyPriceDate, previousFilePath,
				selectionName, omegaDouble, mCoeffDouble, tCritDouble);
		runIndex.execIndexWithGPU();
		compareResults(selectionName, window, results);
		results.clear();

		// 512 day window test
		window = 512;
		previousFilePath = pathRoot + String.valueOf(window) + "days.csv";
		runIndex = new RunIndex(null, dailyPriceValues, dataSize, window, results, dailyPriceDate, previousFilePath,
				selectionName, omegaDouble, mCoeffDouble, tCritDouble);
		runIndex.execIndexWithGPU();
		compareResults(selectionName, window, results);
		results.clear();

	}

	private void compareResults(final String selectionName, final int window, final List<Double> results)
			throws IOException, URISyntaxException {
		final URL resultURL = getClass().getClassLoader().getResource("sample-results" + fileSep + selectionName
				+ fileSep + selectionName + String.valueOf(window) + "days.csv");
		final Path resultPath = new File(resultURL.toURI()).toPath();
		final List<String> lines = Files.readAllLines(resultPath, Charset.defaultCharset());

		int index = 0;
		for (final String line : lines) {
			if (index == 0) {
				index++;
				continue;// header
			}
			final Scanner lineScan = new Scanner(line);
			lineScan.useDelimiter(",|\t");
			lineScan.next();// index
			final Double expected = Double.parseDouble(lineScan.next());// value
			lineScan.next();// date
			lineScan.close();

			assertEquals(expected, results.get(index - 1), epsilon * expected);

			index++;
		}
	}

	private void parseDailyData(final List<String> lines, final List<Double> priceValues,
			final List<String> dailyPriceDate) {
		for (final String line : lines) {
			final Scanner lineScan = new Scanner(line);
			lineScan.useDelimiter(",|\t");
			dailyPriceDate.add(lineScan.next());
			priceValues.add(Double.parseDouble(lineScan.next()));
			lineScan.close();
		}
	}

}
