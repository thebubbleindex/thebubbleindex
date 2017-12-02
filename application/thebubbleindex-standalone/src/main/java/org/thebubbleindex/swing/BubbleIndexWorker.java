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
 *
 * @author bigttrott
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

	public BubbleIndexWorker(final RunType type, final GUI gui, final String windowsInput, final Double omega,
			final Double mCoeff, final Double tCrit, final String categoryName, final String selectionName,
			final Date begDate, final Date endDate, final Boolean isCustomRange, final Boolean GRAPH_ON,
			final DailyDataCache dailyDataCache, final Indices indices, final String openCLSrc) {
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
	}

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

	public void publishText(final String text) {
		publish(text);
	}

	@Override
	protected void process(final List<String> textList) {
		for (final String text : textList)
			Utilities.displayOutput(text, false);
	}

	@Override
	public void done() {
		gui.resetGUI();
		dailyDataCache.reset();
	}

	private void runSingle() {
		Logs.myLogger.info("Run Single Selection Button Clicked.");
		publish("Running category: " + categoryName + " Name: " + selectionName);
		final String[] windowInputArray = windowsInput.split(",");
		for (final String windowString : windowInputArray) {
			final BubbleIndex bubbleIndex = new BubbleIndex(omega, mCoeff, tCrit, Integer.parseInt(windowString.trim()),
					categoryName, selectionName, dailyDataCache, indices, openCLSrc);
			if (!RunContext.Stop)
				bubbleIndex.runBubbleIndex(this);
			if (!RunContext.Stop)
				bubbleIndex.outputResults(this);
		}

		if (GRAPH_ON) {
			Logs.myLogger.info("Graph selection box checked. Plotting first four time windows.");
			publish("Plotting first four time windows...");

			final BubbleIndex bubbleIndex = new BubbleIndex(categoryName, selectionName, dailyDataCache, indices, openCLSrc);
			bubbleIndex.plot(this, windowsInput, begDate, endDate, isCustomRange);
		}
	}

	private void runAllNames() {
		Logs.myLogger.info("Run entire category button clicked");
		publish("Running category: " + categoryName);
		// categoryName = (String)DropDownCategory.getSelectedItem();
		final ArrayList<String> updateNames = indices.getCategoriesAndComponents().get(categoryName).getComponents();
		final String[] windowInputArray = windowsInput.split(",");

		for (final String updateName : updateNames) {
			for (final String windowString : windowInputArray) {
				final BubbleIndex bubbleIndex = new BubbleIndex(omega, mCoeff, tCrit,
						Integer.parseInt(windowString.trim()), categoryName, updateName, dailyDataCache, indices, openCLSrc);
				if (!RunContext.Stop)
					bubbleIndex.runBubbleIndex(this);
				if (!RunContext.Stop)
					bubbleIndex.outputResults(this);
				if (RunContext.Stop)
					break;
			}
			if (RunContext.Stop)
				break;
		}
	}

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
							Integer.parseInt(windowString.trim()), category, updateName, dailyDataCache, indices, openCLSrc);
					if (!RunContext.Stop)
						bubbleIndex.runBubbleIndex(this);
					if (!RunContext.Stop)
						bubbleIndex.outputResults(this);
					if (RunContext.Stop)
						break;
				}
				if (RunContext.Stop)
					break;
			}
			if (RunContext.Stop)
				break;
		}
	}
}
