package org.thebubbleindex.logging;

import org.apache.logging.log4j.Logger;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;

/**
 * Logs contains the Log4j2 logger instance
 * 
 * @author thebubbleindex
 */
public class Logs implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3261003642653463279L;
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
