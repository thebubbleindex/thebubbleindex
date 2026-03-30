package org.thebubbleindex.runnable.test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.runnable.UpdateRunnable;

/**
 * Tests that verify {@link UpdateRunnable} returns 0 when the stop flag is set
 * (no network access required) and when called with a stopped context.
 */
public class UpdateRunnableTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private Indices buildIndices() throws Exception {
		final File programDataDir = new File(tempFolder.getRoot(), "ProgramData");
		programDataDir.mkdirs();
		final Indices indices = new Indices();
		indices.setUserDir(tempFolder.getRoot().getAbsolutePath() + File.separator);
		return indices;
	}

	@Test
	public void callShouldReturnZeroImmediatelyWhenStopFlagIsSet() throws Exception {
		final Indices indices = buildIndices();
		final RunContext runContext = new RunContext();
		runContext.setStop(true);
		runContext.setGUI(false);

		final UpdateRunnable runnable = new UpdateRunnable(
				null, "Stocks", "TEST", "YAHOO", "", "", 0,
				false, "", false, indices, runContext);

		final Integer result = runnable.call();
		// Stop flag is set: should exit early with 0
		assertEquals(Integer.valueOf(0), result);
	}

	@Test
	public void callShouldReturnZeroWhenStopFlagIsSetWithFEDSource() throws Exception {
		final Indices indices = buildIndices();
		final RunContext runContext = new RunContext();
		runContext.setStop(true);
		runContext.setGUI(false);

		final UpdateRunnable runnable = new UpdateRunnable(
				null, "Currencies", "DTWEXM", "FED", "", "", 0,
				false, "", false, indices, runContext);

		assertEquals(Integer.valueOf(0), runnable.call());
	}

	@Test
	public void callShouldReturnZeroWhenStopFlagIsSetWithQUANDLSource() throws Exception {
		final Indices indices = buildIndices();
		final RunContext runContext = new RunContext();
		runContext.setStop(true);
		runContext.setGUI(false);

		final UpdateRunnable runnable = new UpdateRunnable(
				null, "Indices", "DJIA", "QUANDL", "WIKI", "DJIA", 2,
				false, "", false, indices, runContext);

		assertEquals(Integer.valueOf(0), runnable.call());
	}
}
