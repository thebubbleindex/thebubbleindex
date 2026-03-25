package org.thebubbleindex.logging;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Logs contains the Log4j2 logger instance used throughout the application.
 * The logger is selected based on the operating system at class-load time so
 * that appropriate log appenders (file paths, etc.) are chosen for Windows and
 * Linux environments.
 *
 * @author thebubbleindex
 */
public class Logs {

	/** The application-wide Log4j2 logger instance. */
	public static Logger myLogger;

	static {
		final String osName = System.getProperty("os.name").toLowerCase();
		System.out.println("OS Name: " + osName);
		if (osName != null && osName.indexOf("win") >= 0) {
			myLogger = LogManager.getLogger("mylogger-windows");
		} else {
			myLogger = LogManager.getLogger("mylogger-linux");
		}
	}
}
