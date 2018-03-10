package org.thebubbleindex.callable.test;

import org.cactoos.io.ResourceOf;
import org.cactoos.text.TextOf;
import org.junit.Before;
import org.junit.Test;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.runnable.RunIndex;
import org.thebubbleindex.testutil.TestUtil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static org.junit.Assert.assertEquals;

/**
 * 
 * @author thebubbleindex
 *
 */
public class MyGPUCallableTest {

	private final double epsilon = 0.01;
	private final String fileSep = File.separator;
	private Indices indices;
	
	@Before
	public void cleanSlate() throws IOException {
		final String targetPathRoot = getClass().getProtectionDomain().getCodeSource().getLocation().getPath()
				.replaceFirst("test-classes/", "").replaceFirst("classes/", "") + "ProgramData";
		
		final File targetDir = new File(targetPathRoot);
		if (targetDir.exists()) {
			Files.walkFileTree(targetDir.toPath(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
			});
		}
		
		indices = new Indices();
		indices.initialize();
	}

	@Test
	public void resultsShouldMatchBITSTAMPUSD() throws IOException, URISyntaxException {
		final RunContext runContext = new RunContext(false, false);

		final String selectionName = "BITSTAMPUSD";
		final String folder = "ProgramData";
		final String folderType = "Currencies";
		final double omegaDouble = 6.28;
		final double mCoeffDouble = 0.38;
		final double tCritDouble = 21.0;

		final String pathRoot = folder + fileSep + folderType + fileSep + selectionName + fileSep + selectionName;
		final ResourceOf dailyDataResource = new ResourceOf(pathRoot + "dailydata.csv");

		final List<String> lines = TestUtil.getLines(new TextOf(dailyDataResource).asString());
		final List<String> dailyPriceDate = new ArrayList<String>();
		final List<Double> priceValues = new ArrayList<Double>();
		final List<Double> results = new ArrayList<Double>();

		TestUtil.parseDailyDataDoubles(lines, priceValues, dailyPriceDate);

		final int dataSize = dailyPriceDate.size();
		final double[] dailyPriceValues = new double[dataSize];
		for (int i = 0; i < dataSize; i++) {
			dailyPriceValues[i] = priceValues.get(i);
		}

		final String openCLSrc = new TextOf(new ResourceOf("GPUKernel.cl")).asString();

		runContext.setThreadNumber(4);
		runContext.setForceCPU(false);

		final List<Integer> testWindows = new ArrayList<Integer>(5);
		testWindows.add(52);
		testWindows.add(104);
		testWindows.add(153);
		testWindows.add(256);
		testWindows.add(512);

		for (final Integer window : testWindows) {
			testWindow(pathRoot, dailyPriceValues, dataSize, window, results, dailyPriceDate, selectionName,
					omegaDouble, mCoeffDouble, tCritDouble, indices, openCLSrc, runContext);
		}
	}

	@Test
	public void resultsShouldMatchTSLA() throws IOException, URISyntaxException {
		final RunContext runContext = new RunContext(false, false);

		final String selectionName = "TSLA";
		final String folder = "ProgramData";
		final String folderType = "Stocks";
		final double omegaDouble = 6.28;
		final double mCoeffDouble = 0.38;
		final double tCritDouble = 21.0;

		final String pathRoot = folder + fileSep + folderType + fileSep + selectionName + fileSep + selectionName;
		final ResourceOf dailyDataResource = new ResourceOf(pathRoot + "dailydata.csv");

		final List<String> lines = TestUtil.getLines(new TextOf(dailyDataResource).asString());
		final List<String> dailyPriceDate = new ArrayList<String>();
		final List<Double> priceValues = new ArrayList<Double>();
		final List<Double> results = new ArrayList<Double>();

		TestUtil.parseDailyDataDoubles(lines, priceValues, dailyPriceDate);

		final int dataSize = dailyPriceDate.size();
		final double[] dailyPriceValues = new double[dataSize];
		for (int i = 0; i < dataSize; i++) {
			dailyPriceValues[i] = priceValues.get(i);
		}

		final String openCLSrc = new TextOf(new ResourceOf("GPUKernel.cl")).asString();

		runContext.setThreadNumber(4);
		runContext.setForceCPU(false);

		final List<Integer> testWindows = new ArrayList<Integer>(5);
		testWindows.add(52);
		testWindows.add(104);
		testWindows.add(153);
		testWindows.add(256);
		testWindows.add(512);

		for (final Integer window : testWindows) {
			testWindow(pathRoot, dailyPriceValues, dataSize, window, results, dailyPriceDate, selectionName,
					omegaDouble, mCoeffDouble, tCritDouble, indices, openCLSrc, runContext);
		}
	}

	@Test
	public void resultsShouldMatchDTWEXM() throws IOException, URISyntaxException {
		final Indices indices = new Indices();
		final RunContext runContext = new RunContext(false, false);

		final String selectionName = "DTWEXM";
		final String folder = "ProgramData";
		final String folderType = "Currencies";
		final double omegaDouble = 6.28;
		final double mCoeffDouble = 0.38;
		final double tCritDouble = 21.0;

		final String pathRoot = folder + fileSep + folderType + fileSep + selectionName + fileSep + selectionName;
		final ResourceOf dailyDataResource = new ResourceOf(pathRoot + "dailydata.csv");

		final List<String> lines = TestUtil.getLines(new TextOf(dailyDataResource).asString());
		final List<String> dailyPriceDate = new ArrayList<String>();
		final List<Double> priceValues = new ArrayList<Double>();
		final List<Double> results = new ArrayList<Double>();

		TestUtil.parseDailyDataDoubles(lines, priceValues, dailyPriceDate);

		final int dataSize = dailyPriceDate.size();
		final double[] dailyPriceValues = new double[dataSize];
		for (int i = 0; i < dataSize; i++) {
			dailyPriceValues[i] = priceValues.get(i);
		}

		final String openCLSrc = new TextOf(new ResourceOf("GPUKernel.cl")).asString();

		runContext.setThreadNumber(4);
		runContext.setForceCPU(false);

		final List<Integer> testWindows = new ArrayList<Integer>(5);
		testWindows.add(52);
		testWindows.add(104);
		testWindows.add(153);
		testWindows.add(256);
		testWindows.add(512);

		for (final Integer window : testWindows) {
			testWindow(pathRoot, dailyPriceValues, dataSize, window, results, dailyPriceDate, selectionName,
					omegaDouble, mCoeffDouble, tCritDouble, indices, openCLSrc, runContext);
		}
	}

	@Test
	public void shouldUpdateExistingDataCorrectly() throws IOException, URISyntaxException {
		final RunContext runContext = new RunContext(false, false);

		final List<String> dailyPriceData = new ArrayList<String>();
		final List<String> tempList = new ArrayList<String>();

		final String categoryName = "Currencies";
		final String selectionName = "BITSTAMPUSD";
		final String windowsString = "52,104,153,256";
		final String testFolder = "sample-results";

		final String dailyDataPricePathRoot = testFolder + fileSep + selectionName + fileSep + selectionName;
		final List<String> lines = TestUtil
				.getLines(new TextOf(new ResourceOf(dailyDataPricePathRoot + "dailydata-UPDATE.csv")).asString());

		TestUtil.parseDailyData(lines, dailyPriceData, tempList);
		TestUtil.moveFiles(dailyDataPricePathRoot, windowsString, categoryName, selectionName);

		final double omegaDouble = 6.28;
		final double mCoeffDouble = 0.38;
		final double tCritDouble = 21.0;
		final String pathRoot = indices.getUserDir() + indices.getProgramDataFolder() + fileSep + categoryName + fileSep
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

		final String openCLSrc = new TextOf(new ResourceOf("GPUKernel.cl")).asString();

		runContext.setThreadNumber(4);
		runContext.setForceCPU(false);

		final List<Integer> testWindows = new ArrayList<Integer>(5);
		testWindows.add(52);
		testWindows.add(104);
		testWindows.add(153);
		testWindows.add(256);

		for (final Integer window : testWindows) {
			testWindowUpdate(pathRoot, dailyPriceValues, dataSize, window, results, dailyPriceDate, selectionName,
					omegaDouble, mCoeffDouble, tCritDouble, indices, openCLSrc, runContext);
		}
	}

	private void testWindow(final String pathRoot, final double[] dailyPriceValues, final int dataSize,
			final int window, final List<Double> results, final List<String> dailyPriceDate, final String selectionName,
			final double omegaDouble, final double mCoeffDouble, final double tCritDouble, final Indices indices,
			final String openCLSrc, final RunContext runContext) throws IOException, URISyntaxException {

		final String previousFilePath = pathRoot + String.valueOf(window) + "days.csv";
		byte[] previousFileBytes = null;

		try {
			previousFileBytes = new TextOf(new ResourceOf(previousFilePath)).asString().getBytes();
		} catch (final Exception ex) {
		}

		final RunIndex runIndex = new RunIndex(null, dailyPriceValues, dataSize, window, results, dailyPriceDate,
				previousFileBytes, selectionName, omegaDouble, mCoeffDouble, tCritDouble, indices, openCLSrc,
				runContext);

		runIndex.execIndexWithGPU();
		compareResults(selectionName, window, results);
		results.clear();
	}

	private void testWindowUpdate(final String pathRoot, final double[] dailyPriceValues, final int dataSize,
			final int window, final List<Double> results, final List<String> dailyPriceDate, final String selectionName,
			final double omegaDouble, final double mCoeffDouble, final double tCritDouble, final Indices indices,
			final String openCLSrc, final RunContext runContext) throws IOException, URISyntaxException {

		final String previousFilePath = pathRoot + String.valueOf(window) + "days.csv";
		final byte[] previousFileBytes = Files.readAllBytes(new File(previousFilePath).toPath());

		final RunIndex runIndex = new RunIndex(null, dailyPriceValues, dataSize, window, results, dailyPriceDate,
				previousFileBytes, selectionName, omegaDouble, mCoeffDouble, tCritDouble, indices, openCLSrc,
				runContext);

		runIndex.execIndexWithGPU();
		compareResultsUpdate(selectionName, window, results);
		results.clear();
	}

	private void compareResults(final String selectionName, final int window, final List<Double> results)
			throws IOException, URISyntaxException {
		final ResourceOf resultResource = new ResourceOf("sample-results" + fileSep + selectionName + fileSep
				+ selectionName + String.valueOf(window) + "days.csv");
		final List<String> lines = TestUtil.getLines(new TextOf(resultResource).asString());

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
}
