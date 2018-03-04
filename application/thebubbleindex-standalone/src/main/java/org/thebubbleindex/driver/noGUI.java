package org.thebubbleindex.driver;

import com.nativelibs4java.util.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.ThreadContext;
import org.thebubbleindex.computegrid.BubbleIndexComputeGrid;
import org.thebubbleindex.computegrid.IgniteBubbleIndexComputeGrid;
import org.thebubbleindex.data.UpdateData;
import org.thebubbleindex.exception.FailedToRunIndex;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.inputs.InputCategory;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.runnable.RunIndex;
import org.thebubbleindex.swing.ComputeGridGUI;
import org.thebubbleindex.swing.GUI;
import org.thebubbleindex.util.Utilities;

/**
 * the noGUI class is the entry point of the application. The Bubble Index can
 * either run in a Graphical User Interface (GUI) mode or a non-GUI mode via
 * command line arguments.
 * 
 * @author thebubbleindex
 */
public class noGUI {

	public enum RunType {
		Single, Category, All, Update
	}

	private static final String computeGridShortOption = "g";
	private static final String guiShortOption = "u";
	private static final String categoryShortOption = "c";
	private static final String selectionShortOption = "s";
	private static final String windowsShortOption = "w";
	private static final String threadsShortOption = "t";
	private static final String daysShortOption = "d";
	private static final String mcoeffShortOption = "m";
	private static final String omegaShortOption = "o";
	private static final String runTypeShortOption = "r";
	private static final String quandlKeyShortOption = "q";

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
		String openCLSrc = null;

		final Date startTime = new Date();
		ThreadContext.put("StartTime", startTime.toString());

		Logs.myLogger.info("Starting The Bubble Index");

		if (args.length > 0) {
			Logs.myLogger.info("Found command line arguments.");

			for (final String arg : args) {
				Logs.myLogger.info("{}", arg);
			}

			final Options options = new Options();
			parseCLIArgs(args, options);

			final CommandLineParser parser = new DefaultParser();
			final HelpFormatter formatter = new HelpFormatter();
			CommandLine cmd;

			try {
				cmd = parser.parse(options, args);
			} catch (ParseException ex) {
				Logs.myLogger.error(ex);
				formatter.printHelp("utility-name", options);

				return;
			}

			if (cmd.hasOption(computeGridShortOption)) {
				if (cmd.hasOption(guiShortOption)) {
					final BubbleIndexComputeGrid bubbleIndexComputeGrid = new IgniteBubbleIndexComputeGrid();
					final RunContext runContext = new RunContext();
					runContext.setGUI(true);
					bubbleIndexComputeGrid.setRunContext(runContext);

					ComputeGridGUI.GUImain(runContext, bubbleIndexComputeGrid);
				} else {
					final BubbleIndexComputeGrid bubbleIndexComputeGrid = new IgniteBubbleIndexComputeGrid();
					final DailyDataCache dailyDataCache = new DailyDataCache();
					final Indices indices = new Indices();
					final RunContext runContext = new RunContext();

					Logs.myLogger.info("Running non-GUI mode");
					runContext.setGUI(false);
					indices.initialize();

					try {
						Logs.myLogger.info("Reading OpenCL source file.");
						openCLSrc = IOUtils.readText(RunIndex.class.getClassLoader().getResource("GPUKernel.cl"));
					} catch (final IOException ex) {
						Logs.myLogger.error("IOException Exception. Failed to read OpenCL source file. {}", ex);
						Utilities.displayOutput(runContext, "Error. OpenCL source file missing.", false);
					}
					Utilities.displayOutput(runContext, "Working Dir: " + indices.getUserDir(), false);

					final RunType type = RunType.valueOf(cmd.getOptionValue(runTypeShortOption));

					if (type == RunType.Single) {
						final String categoryName = cmd.getOptionValue(categoryShortOption);
						final String selectionName = cmd.getOptionValue(selectionShortOption);
						final String windows = cmd.getOptionValue(windowsShortOption);
						final int threads = Integer.parseInt(cmd.getOptionValue(threadsShortOption));
						final float tCrit = Float.parseFloat(cmd.getOptionValue(daysShortOption));
						final float mCoeff = Float.parseFloat(cmd.getOptionValue(mcoeffShortOption));
						final float omega = Float.parseFloat(cmd.getOptionValue(omegaShortOption));

						runContext.setThreadNumber(threads);
						bubbleIndexComputeGrid.setDailyDataCache(dailyDataCache);
						bubbleIndexComputeGrid.setIndices(indices);
						bubbleIndexComputeGrid.setRunContext(runContext);

						Logs.myLogger.info("Running single selection. Category Name = {}, Selection Name = {}",
								categoryName, selectionName);

						final String[] windowArray = windows.split(",");

						for (final String window : windowArray) {
							final BubbleIndex bubbleIndex = new BubbleIndex(omega, mCoeff, tCrit,
									Integer.parseInt(window.trim()), categoryName, selectionName, dailyDataCache,
									indices, openCLSrc, runContext);
							bubbleIndexComputeGrid.addBubbleIndexTask(bubbleIndex.hashCode(), bubbleIndex);
						}

						bubbleIndexComputeGrid.deployTasks();

						final List<BubbleIndex> results = bubbleIndexComputeGrid.executeBubbleIndexTasks();
						for (final BubbleIndex result : results) {
							result.outputResults(null);
						}

						bubbleIndexComputeGrid.shutdownGrid();
					} else if (type == RunType.Category) {
						final String categoryName = cmd.getOptionValue(categoryShortOption);
						final String windows = cmd.getOptionValue(windowsShortOption);
						final int threads = Integer.parseInt(cmd.getOptionValue(threadsShortOption));
						final float tCrit = Float.parseFloat(cmd.getOptionValue(daysShortOption));
						final float mCoeff = Float.parseFloat(cmd.getOptionValue(mcoeffShortOption));
						final float omega = Float.parseFloat(cmd.getOptionValue(omegaShortOption));

						runContext.setThreadNumber(threads);
						bubbleIndexComputeGrid.setDailyDataCache(dailyDataCache);
						bubbleIndexComputeGrid.setIndices(indices);
						bubbleIndexComputeGrid.setRunContext(runContext);

						Logs.myLogger.info("Running entire category. Category Name = {}", categoryName);

						final ArrayList<String> updateNames = indices.getCategoriesAndComponents().get(categoryName)
								.getComponents();
						final String[] windowArray = windows.split(",");

						for (final String updateName : updateNames) {
							for (final String window : windowArray) {
								final BubbleIndex bubbleIndex = new BubbleIndex(omega, mCoeff, tCrit,
										Integer.parseInt(window.trim()), categoryName, updateName, dailyDataCache,
										indices, openCLSrc, runContext);
								bubbleIndexComputeGrid.addBubbleIndexTask(bubbleIndex.hashCode(), bubbleIndex);
							}
						}

						bubbleIndexComputeGrid.deployTasks();

						final List<BubbleIndex> results = bubbleIndexComputeGrid.executeBubbleIndexTasks();
						for (final BubbleIndex result : results) {
							result.outputResults(null);
						}

						bubbleIndexComputeGrid.shutdownGrid();
					} else if (type == RunType.All) {
						final String windows = cmd.getOptionValue(windowsShortOption);
						final int threads = Integer.parseInt(cmd.getOptionValue(threadsShortOption));
						final float tCrit = Float.parseFloat(cmd.getOptionValue(daysShortOption));
						final float mCoeff = Float.parseFloat(cmd.getOptionValue(mcoeffShortOption));
						final float omega = Float.parseFloat(cmd.getOptionValue(omegaShortOption));

						runContext.setThreadNumber(threads);
						bubbleIndexComputeGrid.setDailyDataCache(dailyDataCache);
						bubbleIndexComputeGrid.setIndices(indices);
						bubbleIndexComputeGrid.setRunContext(runContext);

						Logs.myLogger.info("Running all categories and selections.");
						final String[] windowArray = windows.split(",");

						for (final Map.Entry<String, InputCategory> myEntry : indices.getCategoriesAndComponents()
								.entrySet()) {

							final String categoryName = myEntry.getKey();
							final ArrayList<String> updateNames = myEntry.getValue().getComponents();

							for (final String updateName : updateNames) {
								for (final String window : windowArray) {
									final BubbleIndex bubbleIndex = new BubbleIndex(omega, mCoeff, tCrit,
											Integer.parseInt(window.trim()), categoryName, updateName, dailyDataCache,
											indices, openCLSrc, runContext);
									bubbleIndexComputeGrid.addBubbleIndexTask(bubbleIndex.hashCode(), bubbleIndex);
								}
							}
						}

						bubbleIndexComputeGrid.deployTasks();

						final List<BubbleIndex> results = bubbleIndexComputeGrid.executeBubbleIndexTasks();
						for (final BubbleIndex result : results) {
							result.outputResults(null);
						}

						bubbleIndexComputeGrid.shutdownGrid();
					}
				}
			} else {
				if (cmd.hasOption(guiShortOption)) {
					final RunContext runContext = new RunContext();
					runContext.setGUI(true);
					GUI.GUImain(runContext);
				} else {
					final DailyDataCache dailyDataCache = new DailyDataCache();
					final Indices indices = new Indices();
					final RunContext runContext = new RunContext();

					Logs.myLogger.info("Running non-GUI mode");
					runContext.setGUI(false);
					indices.initialize();

					try {
						Logs.myLogger.info("Reading OpenCL source file.");
						openCLSrc = IOUtils.readText(RunIndex.class.getClassLoader().getResource("GPUKernel.cl"));
					} catch (final IOException ex) {
						Logs.myLogger.error("IOException Exception. Failed to read OpenCL source file. {}", ex);
						Utilities.displayOutput(runContext, "Error. OpenCL source file missing.", false);
					}
					Utilities.displayOutput(runContext, "Working Dir: " + indices.getUserDir(), false);

					final RunType type = RunType.valueOf(cmd.getOptionValue(runTypeShortOption));

					if (type == RunType.Single) {
						final String categoryName = cmd.getOptionValue(categoryShortOption);
						final String selectionName = cmd.getOptionValue(selectionShortOption);
						final String windows = cmd.getOptionValue(windowsShortOption);
						final int threads = Integer.parseInt(cmd.getOptionValue(threadsShortOption));
						final float tCrit = Float.parseFloat(cmd.getOptionValue(daysShortOption));
						final float mCoeff = Float.parseFloat(cmd.getOptionValue(mcoeffShortOption));
						final float omega = Float.parseFloat(cmd.getOptionValue(omegaShortOption));

						runContext.setThreadNumber(threads);

						Logs.myLogger.info("Running single selection. Category Name = {}, Selection Name = {}",
								categoryName, selectionName);
						final String[] windowArray = windows.split(",");

						for (final String window : windowArray) {
							try {
								final BubbleIndex bubble = new BubbleIndex(omega, mCoeff, tCrit,
										Integer.parseInt(window.trim()), categoryName, selectionName, dailyDataCache,
										indices, openCLSrc, runContext);
								bubble.runBubbleIndex(null);
								bubble.outputResults(null);
							} catch (final FailedToRunIndex ex) {
								Logs.myLogger.error("Failed to Run Index. Category Name = {}, Selection Name = {}. {}",
										categoryName, selectionName, ex);
							}
						}
					} else if (type == RunType.Category) {
						final String categoryName = cmd.getOptionValue(categoryShortOption);
						final String windows = cmd.getOptionValue(windowsShortOption);
						final int threads = Integer.parseInt(cmd.getOptionValue(threadsShortOption));
						final float tCrit = Float.parseFloat(cmd.getOptionValue(daysShortOption));
						final float mCoeff = Float.parseFloat(cmd.getOptionValue(mcoeffShortOption));
						final float omega = Float.parseFloat(cmd.getOptionValue(omegaShortOption));

						runContext.setThreadNumber(threads);
						Logs.myLogger.info("Running entire category. Category Name = {}", categoryName);

						final ArrayList<String> updateNames = indices.getCategoriesAndComponents().get(categoryName)
								.getComponents();
						final String[] windowArray = windows.split(",");

						for (final String updateName : updateNames) {
							for (final String window : windowArray) {
								final BubbleIndex bubbleIndex = new BubbleIndex(omega, mCoeff, tCrit,
										Integer.parseInt(window.trim()), categoryName, updateName, dailyDataCache,
										indices, openCLSrc, runContext);
								bubbleIndex.runBubbleIndex(null);
								bubbleIndex.outputResults(null);
							}
						}
					} else if (type == RunType.All) {
						final String windows = cmd.getOptionValue(windowsShortOption);
						final int threads = Integer.parseInt(cmd.getOptionValue(threadsShortOption));
						final float tCrit = Float.parseFloat(cmd.getOptionValue(daysShortOption));
						final float mCoeff = Float.parseFloat(cmd.getOptionValue(mcoeffShortOption));
						final float omega = Float.parseFloat(cmd.getOptionValue(omegaShortOption));

						runContext.setThreadNumber(threads);
						Logs.myLogger.info("Running all categories and selections.");
						final String[] windowArray = windows.split(",");

						for (final Map.Entry<String, InputCategory> myEntry : indices.getCategoriesAndComponents()
								.entrySet()) {

							final String categoryName = myEntry.getKey();
							final ArrayList<String> updateNames = myEntry.getValue().getComponents();

							for (final String updateName : updateNames) {
								for (final String window : windowArray) {
									final BubbleIndex bubbleIndex = new BubbleIndex(omega, mCoeff, tCrit,
											Integer.parseInt(window.trim()), categoryName, updateName, dailyDataCache,
											indices, openCLSrc, runContext);
									bubbleIndex.runBubbleIndex(null);
									bubbleIndex.outputResults(null);
								}
							}
						}
					} else if (type == RunType.Update) {
						runContext.setThreadNumber(Runtime.getRuntime().availableProcessors());
						String quandlKey;
						try {
							quandlKey = cmd.getOptionValue(quandlKeyShortOption);
						} catch (final Exception ex) {
							quandlKey = "";
						}
						final UpdateData updateData = new UpdateData(null, quandlKey, indices, runContext);
						updateData.run();
					}
				}
			}
		} else {
			Logs.myLogger.info("No command line args found. Running GUI mode.");

			final RunContext runContext = new RunContext();
			runContext.setGUI(true);

			GUI.GUImain(runContext);
		}
	}

	private static void parseCLIArgs(final String[] args, final Options options) {
		final Option guiInputOption = new Option(guiShortOption, "gui", false, "GUI");
		guiInputOption.setRequired(false);
		options.addOption(guiInputOption);

		final Option computeGridInputOption = new Option(computeGridShortOption, "grid", false, "compute grid");
		computeGridInputOption.setRequired(false);
		options.addOption(computeGridInputOption);

		final Option categoryNameInputOption = new Option(categoryShortOption, "category", true, "category name");
		categoryNameInputOption.setRequired(false);
		options.addOption(categoryNameInputOption);

		final Option selectionNameInputOption = new Option(selectionShortOption, "selection", true, "selection name");
		selectionNameInputOption.setRequired(false);
		options.addOption(selectionNameInputOption);

		final Option windowsInputOption = new Option(windowsShortOption, "windows", true, "data windows");
		windowsInputOption.setRequired(false);
		options.addOption(windowsInputOption);

		final Option threadsInputOption = new Option(threadsShortOption, "threads", true, "num threads");
		threadsInputOption.setRequired(false);
		options.addOption(threadsInputOption);

		final Option daysInputOption = new Option(daysShortOption, "days", true, "days to critical time");
		daysInputOption.setRequired(false);
		options.addOption(daysInputOption);

		final Option mcoeffInputOption = new Option(mcoeffShortOption, "mcoeff", true, "M Coefficient");
		mcoeffInputOption.setRequired(false);
		options.addOption(mcoeffInputOption);

		final Option omegaInputOption = new Option(omegaShortOption, "omega", true, "omega");
		omegaInputOption.setRequired(false);
		options.addOption(omegaInputOption);

		final Option runTypeInputOption = new Option(runTypeShortOption, "type", true, "run type");
		runTypeInputOption.setRequired(false);
		options.addOption(runTypeInputOption);

		final Option quandlKeyInputOption = new Option(quandlKeyShortOption, "quandl", true, "quandl key");
		quandlKeyInputOption.setRequired(false);
		options.addOption(quandlKeyInputOption);
	}
}
