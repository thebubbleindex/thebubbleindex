package org.thebubbleindex.callable.test;

import org.junit.Test;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.runnable.RunIndex;
import org.thebubbleindex.testutil.TestUtil;

import com.nativelibs4java.util.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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

		TestUtil.parseDailyDataDoubles(lines, priceValues, dailyPriceDate);

		final int dataSize = dailyPriceDate.size();
		final double[] dailyPriceValues = new double[dataSize];
		for (int i = 0; i < dataSize; i++) {
			dailyPriceValues[i] = priceValues.get(i);
		}

		RunIndex.src = IOUtils.readText(RunIndex.class.getResource("/GPUKernel.cl"));
		RunContext.threadNumber = 4;
		RunContext.isGUI = false;
		RunContext.forceCPU = false;

		final List<Integer> testWindows = new ArrayList<Integer>(5);
		testWindows.add(52);
		testWindows.add(104);
		testWindows.add(153);
		testWindows.add(256);
		testWindows.add(512);

		for (final Integer window : testWindows) {
			testWindow(pathRoot, dailyPriceValues, dataSize, window, results, dailyPriceDate, selectionName,
					omegaDouble, mCoeffDouble, tCritDouble);
		}

	}

	@Test
	public void resultsShouldMatchTSLA() throws IOException, URISyntaxException {
		final String selectionName = "TSLA";
		final String folder = "ProgramData";
		final String folderType = "Stocks";
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

		TestUtil.parseDailyDataDoubles(lines, priceValues, dailyPriceDate);

		final int dataSize = dailyPriceDate.size();
		final double[] dailyPriceValues = new double[dataSize];
		for (int i = 0; i < dataSize; i++) {
			dailyPriceValues[i] = priceValues.get(i);
		}

		RunIndex.src = IOUtils.readText(RunIndex.class.getResource("/GPUKernel.cl"));
		RunContext.threadNumber = 4;
		RunContext.isGUI = false;
		RunContext.forceCPU = false;

		final List<Integer> testWindows = new ArrayList<Integer>(5);
		testWindows.add(52);
		testWindows.add(104);
		testWindows.add(153);
		testWindows.add(256);
		testWindows.add(512);

		for (final Integer window : testWindows) {
			testWindow(pathRoot, dailyPriceValues, dataSize, window, results, dailyPriceDate, selectionName,
					omegaDouble, mCoeffDouble, tCritDouble);
		}
	}

	@Test
	public void resultsShouldMatchDTWEXM() throws IOException, URISyntaxException {
		final String selectionName = "DTWEXM";
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

		TestUtil.parseDailyDataDoubles(lines, priceValues, dailyPriceDate);

		final int dataSize = dailyPriceDate.size();
		final double[] dailyPriceValues = new double[dataSize];
		for (int i = 0; i < dataSize; i++) {
			dailyPriceValues[i] = priceValues.get(i);
		}

		RunIndex.src = IOUtils.readText(RunIndex.class.getResource("/GPUKernel.cl"));
		RunContext.threadNumber = 4;
		RunContext.isGUI = false;
		RunContext.forceCPU = false;

		final List<Integer> testWindows = new ArrayList<Integer>(5);
		testWindows.add(52);
		testWindows.add(104);
		testWindows.add(153);
		testWindows.add(256);
		testWindows.add(512);

		for (final Integer window : testWindows) {
			testWindow(pathRoot, dailyPriceValues, dataSize, window, results, dailyPriceDate, selectionName,
					omegaDouble, mCoeffDouble, tCritDouble);
		}
	}

	@Test
	public void shouldUpdateExistingDataCorrectly() throws IOException, URISyntaxException {

		Indices.initialize();
		final List<String> dailyPriceData = new ArrayList<String>();
		final List<String> tempList = new ArrayList<String>();

		final String categoryName = "Currencies";
		final String selectionName = "BITSTAMPUSD";
		final String windowsString = "52,104,153,256";
		final String testFolder = "sample-results";

		final String dailyDataPricePathRoot = testFolder + fileSep + selectionName + fileSep + selectionName;
		final URL dailyDataUrl = getClass().getClassLoader()
				.getResource(dailyDataPricePathRoot + "dailydata-UPDATE.csv");
		final Path dailyDataPath = new File(dailyDataUrl.toURI()).toPath();
		final List<String> lines = Files.readAllLines(dailyDataPath, Charset.defaultCharset());

		TestUtil.parseDailyData(lines, dailyPriceData, tempList);
		moveFiles(dailyDataPricePathRoot, windowsString, categoryName, selectionName);

		final double omegaDouble = 6.28;
		final double mCoeffDouble = 0.38;
		final double tCritDouble = 21.0;
		final String pathRoot = Indices.userDir + Indices.programDataFolder + fileSep + categoryName + fileSep
				+ selectionName + fileSep + selectionName;

		final List<Double> priceValues = new ArrayList<Double>();
		final List<String> dailyPriceDate = new ArrayList<String>();
		final List<Double> results = new ArrayList<Double>();

		TestUtil.parseDailyDataDoubles(lines, priceValues, dailyPriceDate);

		final int dataSize = dailyPriceDate.size();
		final double[] dailyPriceValues = new double[dataSize];
		for (int i = 0; i < dataSize; i++) {
			dailyPriceValues[i] = priceValues.get(i);
		}

		RunIndex.src = IOUtils.readText(RunIndex.class.getResource("/GPUKernel.cl"));
		RunContext.threadNumber = 4;
		RunContext.isGUI = false;
		RunContext.forceCPU = false;

		final List<Integer> testWindows = new ArrayList<Integer>(5);
		testWindows.add(52);
		testWindows.add(104);
		testWindows.add(153);
		testWindows.add(256);

		for (final Integer window : testWindows) {
			testWindowUpdate(pathRoot, dailyPriceValues, dataSize, window, results, dailyPriceDate, selectionName,
					omegaDouble, mCoeffDouble, tCritDouble);
		}
	}

	private void testWindow(final String pathRoot, double[] dailyPriceValues, int dataSize, int window,
			List<Double> results, List<String> dailyPriceDate, String selectionName, double omegaDouble,
			double mCoeffDouble, double tCritDouble) throws IOException, URISyntaxException {

		final String previousFilePath = pathRoot + String.valueOf(window) + "days.csv";
		RunIndex runIndex = new RunIndex(null, dailyPriceValues, dataSize, window, results, dailyPriceDate,
				previousFilePath, selectionName, omegaDouble, mCoeffDouble, tCritDouble);
		runIndex.execIndexWithGPU();
		compareResults(selectionName, window, results);
		results.clear();
	}

	private void testWindowUpdate(final String pathRoot, double[] dailyPriceValues, int dataSize, int window,
			List<Double> results, List<String> dailyPriceDate, String selectionName, double omegaDouble,
			double mCoeffDouble, double tCritDouble) throws IOException, URISyntaxException {

		final String previousFilePath = pathRoot + String.valueOf(window) + "days.csv";
		RunIndex runIndex = new RunIndex(null, dailyPriceValues, dataSize, window, results, dailyPriceDate,
				previousFilePath, selectionName, omegaDouble, mCoeffDouble, tCritDouble);
		runIndex.execIndexWithGPU();
		compareResultsUpdate(selectionName, window, results);
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
			System.out.println(results.get(index - 1));
			assertEquals(expected, results.get(index - 1), epsilon * expected);

			index++;
		}
	}

	private void compareResultsUpdate(final String selectionName, final int window, final List<Double> results) {

		assertEquals(6, results.size());
		if (window == 52) {
			assertEquals(4.341765880584717, results.get(0), epsilon);
			assertEquals(10.438558578491211, results.get(1), epsilon);
			assertEquals(99.56694030761719, results.get(2), epsilon);
			assertEquals(184.25173950195312, results.get(3), epsilon);
			assertEquals(234.9745330810547, results.get(4), epsilon);
			assertEquals(288.6654052734375, results.get(5), epsilon);
		} else if (window == 104) {
			assertEquals(4849.6337890625, results.get(0), epsilon);
			assertEquals(4636.3193359375, results.get(1), epsilon);
			assertEquals(4476.1884765625, results.get(2), epsilon);
			assertEquals(4271.99169921875, results.get(3), epsilon);
			assertEquals(4004.52001953125, results.get(4), epsilon);
			assertEquals(3736.92529296875, results.get(5), epsilon);
		} else if (window == 153) {
			assertEquals(21157.720703125, results.get(0), epsilon);
			assertEquals(20955.75390625, results.get(1), epsilon);
			assertEquals(20505.515625, results.get(2), epsilon);
			assertEquals(20068.205078125, results.get(3), epsilon);
			assertEquals(19626.08984375, results.get(4), epsilon);
			assertEquals(19126.939453125, results.get(5), epsilon);
		} else if (window == 256) {
			assertEquals(70685.09375, results.get(0), epsilon);
			assertEquals(71127.5546875, results.get(1), epsilon);
			assertEquals(70851.3125, results.get(2), epsilon);
			assertEquals(70712.703125, results.get(3), epsilon);
			assertEquals(70699.453125, results.get(4), epsilon);
			assertEquals(70579.2890625, results.get(5), epsilon);
		}
	}

	private void moveFiles(final String dailyDataPricePathRoot, final String windowsString, final String categoryName,
			final String selectionName) throws IOException {
		final String targetPathRoot = getClass().getProtectionDomain().getCodeSource().getLocation().getPath()
				.replaceFirst("test-classes/", "") + "ProgramData" + fileSep + categoryName + fileSep + selectionName;
		new File(targetPathRoot).mkdirs();
		final String[] windows = windowsString.split(",");
		for (final String window : windows) {
			final String sourceURL = getClass().getProtectionDomain().getCodeSource().getLocation().getPath()
					+ dailyDataPricePathRoot + String.valueOf(window) + "days.csv";
			final Path source = new File(sourceURL).toPath();
			final Path target = new File(targetPathRoot + fileSep + selectionName + String.valueOf(window) + "days.csv")
					.toPath();
			Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
		}
	}
}
