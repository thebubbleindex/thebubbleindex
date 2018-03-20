package org.thebubbleindex.swing;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.thebubbleindex.computegrid.BubbleIndexComputeGrid;
import org.thebubbleindex.driver.BubbleIndex;
import org.thebubbleindex.driver.BubbleIndexGridTask;
import org.thebubbleindex.driver.DailyDataCache;
import org.thebubbleindex.driver.noGUI.RunType;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.inputs.InputCategory;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.runnable.RunContext;

import gnu.trove.map.hash.TIntObjectHashMap;

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
		final DailyDataCache localDailyDataCache = new DailyDataCache();

		for (final String window : windowInputArray) {
			BubbleIndexGridTask bubbleIndex = null;

			try {
				bubbleIndex = new BubbleIndexGridTask(omega, mCoeff, tCrit, Integer.parseInt(window.trim()),
						categoryName, selectionName, localDailyDataCache, indices, openCLSrc, runContext);
			} catch (final Exception ex) {
				publish(ex.getMessage());
			} finally {
				if (bubbleIndex != null) {
					bubbleIndexComputeGrid.addBubbleIndexTask(bubbleIndex.hashCode(), bubbleIndex);
				}
			}
		}

		bubbleIndexComputeGrid.executeBubbleIndexTasks();

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
			final DailyDataCache localDailyDataCache = new DailyDataCache();

			for (final String window : windowInputArray) {
				BubbleIndexGridTask bubbleIndex = null;

				try {
					bubbleIndex = new BubbleIndexGridTask(omega, mCoeff, tCrit, Integer.parseInt(window.trim()),
							categoryName, updateName, localDailyDataCache, indices, openCLSrc, runContext);
				} catch (final Exception ex) {
					publish(ex.getMessage());
				} finally {
					if (bubbleIndex != null) {
						bubbleIndexComputeGrid.addBubbleIndexTask(bubbleIndex.hashCode(), bubbleIndex);
					}
				}
			}
		}

		bubbleIndexComputeGrid.executeBubbleIndexTasks();
	}

	@Override
	protected void runAllTypes() {
		Logs.myLogger.info("Run all categories button clicked.");
		publish("Running all categories.");

		final String[] windowInputArray = windowsInput.split(",");

		for (final Map.Entry<String, InputCategory> myEntry : indices.getCategoriesAndComponents().entrySet()) {
			final String categoryName = myEntry.getKey();
			publish("Creating tasks for category: " + categoryName);

			final ArrayList<String> updateNames = myEntry.getValue().getComponents();
			final TIntObjectHashMap<BubbleIndexGridTask> tempBubbleIndexTasks = new TIntObjectHashMap<BubbleIndexGridTask>(
					updateNames.size() * windowInputArray.length);

			for (final String updateName : updateNames) {
				final DailyDataCache localDailyDataCache = new DailyDataCache();

				for (final String window : windowInputArray) {
					BubbleIndexGridTask bubbleIndex = null;

					try {
						bubbleIndex = new BubbleIndexGridTask(omega, mCoeff, tCrit, Integer.parseInt(window.trim()),
								categoryName, updateName, localDailyDataCache, indices, openCLSrc, runContext);
					} catch (final Exception ex) {
						publish(ex.getMessage());
					} finally {
						if (bubbleIndex != null) {
							tempBubbleIndexTasks.put(bubbleIndex.hashCode(), bubbleIndex);
						}
					}
				}
			}

			bubbleIndexComputeGrid.addAllBubbleIndexTasks(tempBubbleIndexTasks);
			publish("Added " + tempBubbleIndexTasks.size() + " tasks to cache.");
			tempBubbleIndexTasks.clear();
		}

		bubbleIndexComputeGrid.executeBubbleIndexTasks();
	}
}
