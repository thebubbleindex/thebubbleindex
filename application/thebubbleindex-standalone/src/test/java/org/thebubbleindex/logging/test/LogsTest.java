package org.thebubbleindex.logging.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.thebubbleindex.logging.Logs;

/**
 * Tests that verify {@link Logs} correctly initialises the application-wide
 * Log4j2 logger at class-load time.
 */
public class LogsTest {

	@Test
	public void myLoggerShouldNotBeNull() {
		assertNotNull("Logs.myLogger should be initialised at class-load time", Logs.myLogger);
	}

	@Test
	public void myLoggerNameShouldStartWithMylogger() {
		final String name = Logs.myLogger.getName();
		assertNotNull("Logger name should not be null", name);
		assertTrue("Logger name should start with 'mylogger'", name.startsWith("mylogger"));
	}
}
