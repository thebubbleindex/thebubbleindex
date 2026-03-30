package org.thebubbleindex.data.test;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.thebubbleindex.data.UpdateData;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.runnable.RunContext;

/**
 * Tests that verify {@link UpdateData} correctly initialises from configuration
 * files and completes its run when no categories are listed (i.e. the update
 * list is empty).
 */
public class UpdateDataTest2 {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private Indices buildIndices() throws Exception {
		final File programDataDir = tempFolder.newFolder("ProgramData");
		// Create an empty UpdateCategories.csv so init() succeeds without network access
		new File(programDataDir, UpdateData.updateCategories).createNewFile();

		final Indices indices = new Indices();
		indices.setUserDir(tempFolder.getRoot().getAbsolutePath() + File.separator);
		return indices;
	}

	@Test
	public void constructorShouldInitialiseWithEmptyUpdateFile() throws Exception {
		final Indices indices = buildIndices();
		final RunContext runContext = new RunContext();
		runContext.setGUI(false);

		// Should not throw even when UpdateCategories.csv is empty
		final UpdateData updateData = new UpdateData(null, "", indices, runContext);
		assertTrue("UpdateData should be constructed successfully", updateData != null);
	}

	@Test
	public void runShouldCompleteWithNoCategoriesConfigured() throws Exception {
		final Indices indices = buildIndices();
		final RunContext runContext = new RunContext();
		runContext.setGUI(false);
		runContext.setThreadNumber(1);

		final UpdateData updateData = new UpdateData(null, "", indices, runContext);
		// run() with empty categories list should complete without network calls
		updateData.run();
	}
}
