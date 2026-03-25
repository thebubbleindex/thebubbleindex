package org.thebubbleindex.swing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.SwingWorker;

import org.thebubbleindex.driver.BubbleIndex;
import org.thebubbleindex.driver.DailyDataCache;
import org.thebubbleindex.driver.noGUI.RunType;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.inputs.InputCategory;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.util.Utilities;

/**
 * BubbleIndexWorker is a {@link SwingWorker} that executes the core Bubble
 * Index computation on a background thread so that the Swing GUI remains
 * responsive during long-running calculations. It supports three run modes
 * (Single, Category, All) and optionally produces a chart after a successful
 * single-selection run.
 *
 * @author thebubbleindex
 */
public class BubbleIndexWorker extends SwingWorker<Boolean, String> {

	final private RunType type;
	final private GUI gui;
	final private String windowsInput;
	final private Double omega;
	final private Double tCrit;
	final private Double mCoeff;
	final private String categoryName;
	final private String selectionName;
	final private Date begDate;
	final private Date endDate;
	final private Boolean isCustomRange;
	final private Boolean GRAPH_ON;
	final private DailyDataCache dailyDataCache;
	final private Indices indices;
	final private String openCLSrc;
	final private RunContext runContext;

	/**
	 * BubbleIndexWorker constructor.
	 *
	 * @param type           the run type (Single, Category, or All)
	 * @param gui            the parent GUI, used to reset controls when the
	 *                       worker finishes
	 * @param windowsInput   comma-separated list of window sizes (in days)
	 * @param omega          the angular frequency parameter (&omega;) for the
	 *                       log-periodic power law fit
	 * @param mCoeff         the power-law exponent (m) for the fit
	 * @param tCrit          the critical time (t<sub>c</sub>) offset
	 * @param categoryName   the name of the category to process
	 * @param selectionName  the name of the individual selection to process
	 *                       (used only for Single run type)
	 * @param begDate        the start date for a custom date-range plot
	 * @param endDate        the end date for a custom date-range plot
	 * @param isCustomRange  {@code true} to restrict the chart to the
	 *                       specified date range
	 * @param GRAPH_ON       {@code true} to display a chart after a Single run
	 * @param dailyDataCache shared cache that avoids re-reading price data for
	 *                       the same selection across multiple windows
	 * @param indices        application index configuration
	 * @param openCLSrc      source code of the OpenCL GPU kernel
	 * @param runContext     shared run-time state (stop flag, thread count,
	 *                       GUI mode, etc.)
	 */
	public BubbleIndexWorker(final RunType type, final GUI gui, final String windowsInput, final Double omega,
			final Double mCoeff, final Double tCrit, final String categoryName, final String selectionName,
			final Date begDate, final Date endDate, final Boolean isCustomRange, final Boolean GRAPH_ON,
			final DailyDataCache dailyDataCache, final Indices indices, final String openCLSrc,
			final RunContext runContext) {
		this.type = type;
		this.gui = gui;
		this.windowsInput = windowsInput;
		this.omega = omega;
		this.tCrit = tCrit;
		this.mCoeff = mCoeff;
		this.categoryName = categoryName;
		this.selectionName = selectionName;
		this.begDate = begDate;
		this.endDate = endDate;
		this.isCustomRange = isCustomRange;
		this.GRAPH_ON = GRAPH_ON;
		this.dailyDataCache = dailyDataCache;
		this.indices = indices;
		this.openCLSrc = openCLSrc;
		this.runContext = runContext;
	}

	/**
	 * doInBackground performs the selected run type on the Swing worker thread.
	 *
	 * @return {@code true} when execution completes normally
	 * @throws Exception if an unexpected error occurs during the run
	 */
	@Override
	protected Boolean doInBackground() throws Exception {
		switch (type) {
		case Single:
			runSingle();
			break;
		case Category:
			runAllNames();
			break;
		case All:
			runAllTypes();
			break;
		default:
			break;
		}

		return true;
	}

	/**
	 * publishText schedules the given text to be delivered to the
	 * {@link #process(List)} method on the Event Dispatch Thread so it can be
	 * appended to the GUI output area.
	 *
	 * @param text the output message to display
	 */
	public void publishText(final String text) {
		publish(text);
	}

	/**
	 * process receives intermediate text chunks published by the worker thread
	 * and routes them to the GUI output area via
	 * {@link Utilities#displayOutput}.
	 *
	 * @param textList the list of text messages published since the last call
	 */
	@Override
	protected void process(final List<String> textList) {
		for (final String text : textList)
			Utilities.displayOutput(runContext, text, false);
	}

	/**
	 * done is called on the Event Dispatch Thread after {@link #doInBackground}
	 * completes. It resets the GUI controls to their idle state and clears the
	 * daily data cache.
	 */
	@Override
	public void done() {
		gui.resetGUI();
		dailyDataCache.reset();
	}

	/**
	 * runSingle runs the Bubble Index calculation for a single category and
	 * selection across all specified time windows. If graph mode is enabled, a
	 * plot is also displayed after the run completes.
	 */
	private void runSingle() {
		Logs.myLogger.info("Run Single Selection Button Clicked.");
		publish("Running category: " + categoryName + " Name: " + selectionName);
		final String[] windowInputArray = windowsInput.split(",");
		for (final String windowString : windowInputArray) {
			final BubbleIndex bubbleIndex = new BubbleIndex(omega, mCoeff, tCrit, Integer.parseInt(windowString.trim()),
					categoryName, selectionName, dailyDataCache, indices, openCLSrc, runContext);
			if (!runContext.isStop())
				bubbleIndex.runBubbleIndex(this);
			if (!runContext.isStop())
				bubbleIndex.outputResults(this);
		}

		if (GRAPH_ON) {
			Logs.myLogger.info("Graph selection box checked. Plotting first four time windows.");
			publish("Plotting first four time windows...");

			final BubbleIndex bubbleIndex = new BubbleIndex(categoryName, selectionName, dailyDataCache, indices,
					openCLSrc, runContext);
			bubbleIndex.plot(this, windowsInput, begDate, endDate, isCustomRange);
		}
	}

	/**
	 * runAllNames runs the Bubble Index calculation for every selection in the
	 * chosen category across all specified time windows.
	 */
	private void runAllNames() {
		Logs.myLogger.info("Run entire category button clicked");
		publish("Running category: " + categoryName);
		// categoryName = (String)DropDownCategory.getSelectedItem();
		final ArrayList<String> updateNames = indices.getCategoriesAndComponents().get(categoryName).getComponents();
		final String[] windowInputArray = windowsInput.split(",");

		for (final String updateName : updateNames) {
			for (final String windowString : windowInputArray) {
				final BubbleIndex bubbleIndex = new BubbleIndex(omega, mCoeff, tCrit,
						Integer.parseInt(windowString.trim()), categoryName, updateName, dailyDataCache, indices,
						openCLSrc, runContext);
				if (!runContext.isStop())
					bubbleIndex.runBubbleIndex(this);
				if (!runContext.isStop())
					bubbleIndex.outputResults(this);
				if (runContext.isStop())
					break;
			}
			if (runContext.isStop())
				break;
		}
	}

	/**
	 * runAllTypes runs the Bubble Index calculation for every selection in
	 * every category across all specified time windows.
	 */
	private void runAllTypes() {
		Logs.myLogger.info("Run all categories button clicked.");

		publish("Running all categories.");

		for (final Map.Entry<String, InputCategory> myEntry : indices.getCategoriesAndComponents().entrySet()) {

			final String category = myEntry.getKey();
			gui.updateDropDownSelection(category);
			final ArrayList<String> updateNames = myEntry.getValue().getComponents();
			final String[] windowInputArray = windowsInput.split(",");

			for (final String updateName : updateNames) {
				for (final String windowString : windowInputArray) {
					final BubbleIndex bubbleIndex = new BubbleIndex(omega, mCoeff, tCrit,
							Integer.parseInt(windowString.trim()), category, updateName, dailyDataCache, indices,
							openCLSrc, runContext);
					if (!runContext.isStop())
						bubbleIndex.runBubbleIndex(this);
					if (!runContext.isStop())
						bubbleIndex.outputResults(this);
					if (runContext.isStop())
						break;
				}
				if (runContext.isStop())
					break;
			}
			if (runContext.isStop())
				break;
		}
	}
}
