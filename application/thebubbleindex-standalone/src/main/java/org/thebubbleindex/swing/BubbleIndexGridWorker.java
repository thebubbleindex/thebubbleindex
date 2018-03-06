package org.thebubbleindex.swing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.thebubbleindex.computegrid.BubbleIndexComputeGrid;
import org.thebubbleindex.driver.BubbleIndex;
import org.thebubbleindex.driver.DailyDataCache;
import org.thebubbleindex.driver.noGUI.RunType;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.inputs.InputCategory;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.runnable.RunContext;

/**
 *
 * @author thebubbleindex
 */
public class BubbleIndexGridWorker extends BubbleIndexWorker {
	/**
	 * 
	 */
	private static final long serialVersionUID = 620182792287430840L;
	private final BubbleIndexComputeGrid bubbleIndexComputeGrid;

	public BubbleIndexGridWorker(final RunType type, final GUI gui, final String windowsInput, final Double omega,
			final Double mCoeff, final Double tCrit, final String categoryName, final String selectionName,
			final Date begDate, final Date endDate, final Boolean isCustomRange, final Boolean GRAPH_ON,
			final DailyDataCache dailyDataCache, final Indices indices, final String openCLSrc,
			final RunContext runContext, final BubbleIndexComputeGrid bubbleIndexComputeGrid) {
		super(type, gui, windowsInput, omega, mCoeff, tCrit, categoryName, selectionName, begDate, endDate,
				isCustomRange, GRAPH_ON, dailyDataCache, indices, openCLSrc, runContext);
		this.bubbleIndexComputeGrid = bubbleIndexComputeGrid;
	}

	@Override
	protected void runSingle() {
		Logs.myLogger.info("Run Single Selection Button Clicked.");
		publish("Running category: " + categoryName + " Name: " + selectionName);

		final String[] windowInputArray = windowsInput.split(",");
		for (final String window : windowInputArray) {
			final BubbleIndex bubbleIndex = new BubbleIndex(omega, mCoeff, tCrit, Integer.parseInt(window.trim()),
					categoryName, selectionName, dailyDataCache, indices, openCLSrc, runContext);
			bubbleIndexComputeGrid.addBubbleIndexTask(bubbleIndex.hashCode(), bubbleIndex);
		}

		bubbleIndexComputeGrid.deployTasks();

		final List<BubbleIndex> results = bubbleIndexComputeGrid.executeBubbleIndexTasks();
		for (final BubbleIndex result : results) {
			result.outputResults(this);
		}

		if (GRAPH_ON) {
			Logs.myLogger.info("Graph selection box checked. Plotting first four time windows.");
			publish("Plotting first four time windows...");

			final BubbleIndex bubbleIndex = new BubbleIndex(categoryName, selectionName, dailyDataCache, indices,
					openCLSrc, runContext);
			bubbleIndex.plot(this, windowsInput, begDate, endDate, isCustomRange);
		}
	}

	@Override
	protected void runAllNames() {
		Logs.myLogger.info("Run entire category button clicked");
		publish("Running category: " + categoryName);

		final ArrayList<String> updateNames = indices.getCategoriesAndComponents().get(categoryName).getComponents();
		final String[] windowInputArray = windowsInput.split(",");

		for (final String updateName : updateNames) {
			for (final String window : windowInputArray) {
				final BubbleIndex bubbleIndex = new BubbleIndex(omega, mCoeff, tCrit, Integer.parseInt(window.trim()),
						categoryName, updateName, dailyDataCache, indices, openCLSrc, runContext);
				bubbleIndexComputeGrid.addBubbleIndexTask(bubbleIndex.hashCode(), bubbleIndex);
			}
		}

		bubbleIndexComputeGrid.deployTasks();

		final List<BubbleIndex> results = bubbleIndexComputeGrid.executeBubbleIndexTasks();
		for (final BubbleIndex result : results) {
			result.outputResults(this);
		}
	}

	@Override
	protected void runAllTypes() {
		Logs.myLogger.info("Run all categories button clicked.");
		publish("Running all categories.");

		final String[] windowInputArray = windowsInput.split(",");

		for (final Map.Entry<String, InputCategory> myEntry : indices.getCategoriesAndComponents().entrySet()) {
			final String categoryName = myEntry.getKey();
			final ArrayList<String> updateNames = myEntry.getValue().getComponents();

			for (final String updateName : updateNames) {
				for (final String window : windowInputArray) {
					final BubbleIndex bubbleIndex = new BubbleIndex(omega, mCoeff, tCrit,
							Integer.parseInt(window.trim()), categoryName, updateName, dailyDataCache, indices,
							openCLSrc, runContext);
					bubbleIndexComputeGrid.addBubbleIndexTask(bubbleIndex.hashCode(), bubbleIndex);
				}
			}
		}

		bubbleIndexComputeGrid.deployTasks();

		final List<BubbleIndex> results = bubbleIndexComputeGrid.executeBubbleIndexTasks();
		for (final BubbleIndex result : results) {
			result.outputResults(this);
		}
	}
}
