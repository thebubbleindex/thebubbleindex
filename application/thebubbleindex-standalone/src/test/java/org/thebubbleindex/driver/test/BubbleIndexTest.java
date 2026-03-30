package org.thebubbleindex.driver.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.thebubbleindex.driver.BubbleIndex;
import org.thebubbleindex.driver.DailyDataCache;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.runnable.RunContext;

/**
 * Tests that verify {@link BubbleIndex} correctly reads a daily-data CSV file,
 * runs the CPU calculation, and writes results to disk.
 */
public class BubbleIndexTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	/** Creates a tiny synthetic daily-data CSV with 80 rows. */
	private File createDailyDataFile(final File dir, final String selectionName) throws IOException {
		final File subDir = new File(dir, selectionName);
		subDir.mkdirs();
		final File csv = new File(subDir, selectionName + "dailydata.csv");
		final StringBuilder sb = new StringBuilder();
		double price = 100.0;
		for (int i = 0; i < 80; i++) {
			sb.append(String.format("2023-01-%02d\t", i + 1));
			sb.append(String.format("%.4f%n", price));
			price = price * 1.01;
		}
		Files.write(csv.toPath(), sb.toString().getBytes(StandardCharsets.UTF_8));
		return csv;
	}

	private Indices buildIndices(final File userDir) {
		final Indices indices = new Indices();
		indices.setUserDir(userDir.getAbsolutePath() + File.separator);
		return indices;
	}

	@Test
	public void runBubbleIndexShouldProduceResultsWithCPU() throws IOException {
		final File programDataDir = tempFolder.newFolder("ProgramData");
		final File categoryDir = new File(programDataDir, "Stocks");
		categoryDir.mkdirs();
		createDailyDataFile(categoryDir, "TEST");

		final Indices indices = buildIndices(tempFolder.getRoot());
		final RunContext runContext = new RunContext();
		runContext.setGUI(false);
		runContext.setForceCPU(true);
		runContext.setThreadNumber(2);
		final DailyDataCache cache = new DailyDataCache();

		final BubbleIndex bi = new BubbleIndex(6.28, 0.38, 21.0, 52,
				"Stocks", "TEST", cache, indices, null, runContext);
		bi.runBubbleIndex(null);
		bi.outputResults(null);

		// Results file should have been created
		final File resultFile = new File(categoryDir, "TEST" + File.separator + "TEST52days.csv");
		assertTrue("Result CSV should be created", resultFile.exists());
	}

	@Test
	public void runBubbleIndexShouldHandleStopFlag() {
		final Indices indices = new Indices();
		indices.setUserDir(System.getProperty("java.io.tmpdir") + File.separator);
		final RunContext runContext = new RunContext();
		runContext.setStop(true);
		runContext.setGUI(false);
		final DailyDataCache cache = new DailyDataCache();

		// Stop flag set: constructor should skip file reading; no exception expected
		final BubbleIndex bi = new BubbleIndex(6.28, 0.38, 21.0, 52,
				"Stocks", "TEST", cache, indices, null, runContext);
		bi.runBubbleIndex(null);
		// No assertion needed: test passes if no exception is thrown
	}

	@Test
	public void bubbleIndexPlotConstructorShouldNotThrow() {
		// Use the simpler constructor (for plot use-case) with stop flag set
		final Indices indices = new Indices();
		indices.setUserDir(System.getProperty("java.io.tmpdir") + File.separator);
		final RunContext runContext = new RunContext();
		runContext.setStop(true);
		runContext.setGUI(false);
		final DailyDataCache cache = new DailyDataCache();

		final BubbleIndex bi = new BubbleIndex("Stocks", "TEST", cache, indices, null, runContext);
		final List<String> empty = new ArrayList<String>();
		// outputResults with empty results should do nothing
		bi.outputResults(null);
	}
}
