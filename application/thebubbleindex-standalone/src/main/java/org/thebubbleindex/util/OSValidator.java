package org.thebubbleindex.util;

import org.thebubbleindex.logging.Logs;

/**
 * OSValidator provides a simple way to find which operating system is running
 * the application.
 * 
 * @author unknown
 */
public class OSValidator {

	private static String OS;

	static {
		OS = System.getProperty("os.name").toLowerCase();
		Logs.myLogger.info("Operating system: {}", OS);
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isSolaris() {
		return (OS.indexOf("sunos") >= 0);
	}
}