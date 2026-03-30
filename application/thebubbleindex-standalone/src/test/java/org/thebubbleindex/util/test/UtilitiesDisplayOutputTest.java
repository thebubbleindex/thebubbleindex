package org.thebubbleindex.util.test;

import org.junit.Test;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.util.Utilities;

/**
 * Tests that verify {@link Utilities#displayOutput} behaves correctly in
 * headless (non-GUI) mode by routing messages to the logger without throwing
 * any exceptions.
 */
public class UtilitiesDisplayOutputTest {

	@Test
	public void displayOutputShouldNotThrowInHeadlessMode() {
		final RunContext runContext = new RunContext();
		runContext.setGUI(false);

		// Should route to logger; no exception expected
		Utilities.displayOutput(runContext, "Test message", false);
		Utilities.displayOutput(runContext, "Another message", true);
	}

	@Test
	public void displayOutputShouldNotModifyLineCountInHeadlessMode() {
		// In headless mode, displayOutput routes to the logger only;
		// the line counter is not incremented.
		final RunContext runContext = new RunContext();
		runContext.setGUI(false);
		Utilities.displayOutput(runContext, "line 1", false);
		Utilities.displayOutput(runContext, "line 2", false);
		// Line count is unchanged in headless mode
		org.junit.Assert.assertEquals(0, runContext.getNumberOfLines());
	}
}
