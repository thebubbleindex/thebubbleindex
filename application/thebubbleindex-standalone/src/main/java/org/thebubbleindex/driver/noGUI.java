package org.thebubbleindex.driver;

import com.nativelibs4java.util.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import org.apache.logging.log4j.ThreadContext;
import org.thebubbleindex.data.UpdateData;
import org.thebubbleindex.exception.FailedToRunIndex;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.inputs.InputCategory;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.runnable.RunIndex;
import org.thebubbleindex.swing.GUI;
import org.thebubbleindex.util.Utilities;

/**
 * the noGUI class is the entry point of the application. The Bubble Index can
 * either run in a Graphical User Interface (GUI) mode or a non-GUI mode via
 * command line arguments.
 * 
 * @author bigttrott
 */
public class noGUI {

	public enum RunType {
		Single, Category, All, Update
	}

	public static String windows;
	public static int threads;
	public static float tCrit;
	public static float mCoeff;
	public static float omega;
	public static Boolean forcedCPU;
	private static String categoryName;
	private static String selectionName;

	/**
	 * main The entry point of the application.
	 * <p>
	 * Accepts command line arguments. The order of the arguments is important.
	 * "" indicates a string input.
	 * <p>
	 * <ol>
	 * <li>Input either "noGUI" - runs in terminal, "GUI" - runs GUI JSwing</li>
	 * <li>RunType Enumerator: input one of these strings: {"Single",
	 * "Category", "All", "Update"}</li>
	 * <li>Category name: input the name of category. Example: "Currencies"</li>
	 * <li>Windows: input the integer value of windows to run, separated by
	 * comma</li>
	 * <li>Threads: input the integer value of threads. Recommend number of CPU
	 * threads.</li>
	 * <li>Critical Time: input the double value of the critical time</li>
	 * <li>M coefficient: input the double value of the M coefficient</li>
	 * <li>Omega: input the double value of the omega variable</li>
	 * <li>Force CPU processing: input either "true" or "false". True will not
	 * run GPU contexts.</li>
	 * </ol>
	 * <p>
	 * Command line example:
	 * <p>
	 * java -jar Bubble_Index.jar noGUI Category Currencies
	 * 700,800,900,1000,1500,2000,2200,2700 100 21.0 0.38 6.28 true
	 * 
	 * @param args
	 *            Command line arguments
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * 
	 */
	public static void main(final String[] args) throws ClassNotFoundException, IOException {

		final Date startTime = new Date();
		ThreadContext.put("StartTime", startTime.toString());

		Logs.myLogger.info("Starting The Bubble Index");

		if (args.length > 0) {
			Logs.myLogger.info("Found command line arguments.");

			for (final String s : args) {
				Logs.myLogger.info("{}", s);
			}

			int i = 0;

			if (args[i].equalsIgnoreCase("noGUI")) {

				Logs.myLogger.info("Running non-GUI mode");
				RunContext.isGUI = false;
				Indices.initialize();

				try {
					Logs.myLogger.info("Reading OpenCL source file.");
					RunIndex.src = IOUtils.readText(RunIndex.class.getResource("GPUKernel.cl"));
				} catch (final IOException ex) {
					Logs.myLogger.error("IOException Exception. Failed to read OpenCL source file. {}", ex);
					Utilities.displayOutput("Error. OpenCL source file missing.", false);
				}
				Utilities.displayOutput("Working Dir: " + Indices.userDir, false);

				final RunType type = RunType.valueOf(args[++i]);

				if (type == RunType.Single) {
					categoryName = args[++i];
					selectionName = args[++i];
					windows = args[++i];
					threads = Integer.parseInt(args[++i]);
					tCrit = Float.parseFloat(args[++i]);
					mCoeff = Float.parseFloat(args[++i]);
					omega = Float.parseFloat(args[++i]);
					forcedCPU = Boolean.parseBoolean(args[++i]);
					RunContext.threadNumber = threads;
					Logs.myLogger.info("Running single selection. Category Name = {}, Selection Name = {}",
							categoryName, selectionName);
					final String[] windowArray = windows.split(",");

					for (final String window : windowArray) {
						try {
							final BubbleIndex bubble = new BubbleIndex(omega, mCoeff, tCrit,
									Integer.parseInt(window.trim()), categoryName, selectionName);
							bubble.runBubbleIndex(null);
							bubble.outputResults(null);
						} catch (final FailedToRunIndex ex) {
							Logs.myLogger.error("Failed to Run Index. Category Name = {}, Selection Name = {}. {}",
									categoryName, selectionName, ex);
						}
					}
				} else if (type == RunType.Category) {
					categoryName = args[++i];
					windows = args[++i];
					threads = Integer.parseInt(args[++i]);
					tCrit = Float.parseFloat(args[++i]);
					mCoeff = Float.parseFloat(args[++i]);
					omega = Float.parseFloat(args[++i]);
					forcedCPU = Boolean.parseBoolean(args[++i]);

					RunContext.threadNumber = threads;
					Logs.myLogger.info("Running entire category. Category Name = {}", categoryName);

					final ArrayList<String> updateNames = Indices.categoriesAndComponents.get(categoryName)
							.getComponents();
					final String[] windowArray = windows.split(",");

					for (final String updateName : updateNames) {
						selectionName = updateName;
						for (final String window : windowArray) {
							final BubbleIndex bubbleIndex = new BubbleIndex(omega, mCoeff, tCrit,
									Integer.parseInt(window.trim()), categoryName, updateName);
							bubbleIndex.runBubbleIndex(null);
							bubbleIndex.outputResults(null);
						}
					}
				} else if (type == RunType.All) {
					windows = args[++i];
					threads = Integer.parseInt(args[++i]);
					tCrit = Float.parseFloat(args[++i]);
					mCoeff = Float.parseFloat(args[++i]);
					omega = Float.parseFloat(args[++i]);
					forcedCPU = Boolean.parseBoolean(args[++i]);

					RunContext.threadNumber = threads;
					Logs.myLogger.info("Running all categories and selections.");
					final String[] windowArray = windows.split(",");

					for (final Map.Entry<String, InputCategory> myEntry : Indices.categoriesAndComponents.entrySet()) {

						categoryName = myEntry.getKey();
						final ArrayList<String> updateNames = myEntry.getValue().getComponents();

						for (final String updateName : updateNames) {
							selectionName = updateName;
							for (final String window : windowArray) {
								final BubbleIndex bubbleIndex = new BubbleIndex(omega, mCoeff, tCrit,
										Integer.parseInt(window.trim()), categoryName, updateName);
								bubbleIndex.runBubbleIndex(null);
								bubbleIndex.outputResults(null);
							}
						}
					}
				} else if (type == RunType.Update) {
					RunContext.threadNumber = Runtime.getRuntime().availableProcessors();
					String quandlKey;
					try {
						quandlKey = args[++i];
					} catch (final Exception ex) {
						quandlKey = "";
					}
					final UpdateData updateData = new UpdateData(null, quandlKey);
					updateData.run();
				} else {
					RunContext.isGUI = true;
					GUI.GUImain();
				}
			} else {
				RunContext.isGUI = true;
				omega = Float.parseFloat(args[++i]);
				tCrit = Float.parseFloat(args[++i]);
				mCoeff = Float.parseFloat(args[++i]);
				GUI.GUImain(omega, tCrit, mCoeff);
			}
		} else {
			Logs.myLogger.info("No command line args found. Running GUI mode.");

			RunContext.isGUI = true;
			GUI.GUImain();
		}
	}
}
