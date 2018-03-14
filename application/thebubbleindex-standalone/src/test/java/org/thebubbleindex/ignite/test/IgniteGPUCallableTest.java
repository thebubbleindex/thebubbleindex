package org.thebubbleindex.ignite.test;

import org.cactoos.io.ResourceOf;
import org.cactoos.text.TextOf;
import org.junit.Before;
import org.junit.Test;
import org.thebubbleindex.computegrid.BubbleIndexComputeGrid;
import org.thebubbleindex.computegrid.IgniteBubbleIndexComputeGrid;
import org.thebubbleindex.driver.BubbleIndexGridTask;
import org.thebubbleindex.driver.DailyDataCache;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.runnable.RunContext;
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

/**
 * 
 * @author thebubbleindex
 *
 */
public class IgniteGPUCallableTest {

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
		final BubbleIndexComputeGrid bubbleIndexComputeGrid = new IgniteBubbleIndexComputeGrid();

		final RunContext runContext = new RunContext(false, true);
		runContext.setThreadNumber(4);

		final DailyDataCache dailyDataCache = new DailyDataCache();

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
		final List<String> dailyPriceData = new ArrayList<String>();

		parseDailyData(lines, dailyPriceData, dailyPriceDate);

		final int dataSize = dailyPriceDate.size();
		final double[] dailyPriceValues = new double[dataSize];
		for (int i = 0; i < dataSize; i++) {
			dailyPriceValues[i] = Double.parseDouble(dailyPriceData.get(i));
		}

		dailyDataCache.setDailyPriceData(dailyPriceData);
		dailyDataCache.setDailyPriceDate(dailyPriceDate);
		dailyDataCache.setDailyPriceDoubleValues(dailyPriceValues);
		dailyDataCache.setSelectionName(selectionName);

		final String openCLSrc = new TextOf(new ResourceOf("GPUKernel.cl")).asString();

		assert openCLSrc != null;

		Logs.myLogger.info("Running single selection. Category Name = {}, Selection Name = {}", folderType,
				selectionName);

		final String[] windowArray = new String[] { "52", "104", "153", "256", "512" };

		for (final String window : windowArray) {
			final BubbleIndexGridTask bubbleIndex = new BubbleIndexGridTask(omegaDouble, mCoeffDouble, tCritDouble,
					Integer.parseInt(window.trim()), folderType, selectionName, dailyDataCache, indices, openCLSrc,
					runContext);
			bubbleIndexComputeGrid.addBubbleIndexTask(bubbleIndex.hashCode(), bubbleIndex);
		}

		bubbleIndexComputeGrid.executeBubbleIndexTasks();
		bubbleIndexComputeGrid.shutdownGrid();

		// TODO : compare results
	}

	@Test
	public void resultsShouldMatchTSLA() throws IOException, URISyntaxException {
		final BubbleIndexComputeGrid bubbleIndexComputeGrid = new IgniteBubbleIndexComputeGrid();

		final RunContext runContext = new RunContext(false, true);
		runContext.setThreadNumber(4);

		final DailyDataCache dailyDataCache = new DailyDataCache();

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
		final List<String> dailyPriceData = new ArrayList<String>();

		parseDailyData(lines, dailyPriceData, dailyPriceDate);

		final int dataSize = dailyPriceDate.size();
		final double[] dailyPriceValues = new double[dataSize];
		for (int i = 0; i < dataSize; i++) {
			dailyPriceValues[i] = Double.parseDouble(dailyPriceData.get(i));
		}

		dailyDataCache.setDailyPriceData(dailyPriceData);
		dailyDataCache.setDailyPriceDate(dailyPriceDate);
		dailyDataCache.setDailyPriceDoubleValues(dailyPriceValues);
		dailyDataCache.setSelectionName(selectionName);

		final String openCLSrc = new TextOf(new ResourceOf("GPUKernel.cl")).asString();

		assert openCLSrc != null;

		Logs.myLogger.info("Running single selection. Category Name = {}, Selection Name = {}", folderType,
				selectionName);

		final String[] windowArray = new String[] { "52", "104", "153", "256", "512" };

		for (final String window : windowArray) {
			final BubbleIndexGridTask bubbleIndex = new BubbleIndexGridTask(omegaDouble, mCoeffDouble, tCritDouble,
					Integer.parseInt(window.trim()), folderType, selectionName, dailyDataCache, indices, openCLSrc,
					runContext);
			bubbleIndexComputeGrid.addBubbleIndexTask(bubbleIndex.hashCode(), bubbleIndex);
		}

		bubbleIndexComputeGrid.executeBubbleIndexTasks();
		bubbleIndexComputeGrid.shutdownGrid();

		// TODO : compare results
	}

	private void parseDailyData(final List<String> lines, final List<String> priceValues,
			final List<String> dailyPriceDate) {
		for (final String line : lines) {
			final Scanner lineScan = new Scanner(line);
			lineScan.useDelimiter(",|\t");
			dailyPriceDate.add(lineScan.next());
			priceValues.add(lineScan.next());
			lineScan.close();
		}
	}
}
