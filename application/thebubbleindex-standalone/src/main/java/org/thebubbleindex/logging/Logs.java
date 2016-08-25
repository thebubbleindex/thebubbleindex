package org.thebubbleindex.logging;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Logs contains the Log4j2 logger instance
 * 
 * @author bigttrott
 */
public class Logs {

	public static Logger myLogger;

	static {
		myLogger = LogManager.getLogger("mylogger");
	}
}
