package org.thebubbleindex.computegrid;

import java.util.List;

import org.thebubbleindex.driver.BubbleIndex;
import org.thebubbleindex.driver.DailyDataCache;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.runnable.RunContext;

/**
 * 
 * @author thebubbleindex
 *
 */
public interface BubbleIndexComputeGrid {

	void setDailyDataCache(final DailyDataCache dailyDataCache);

	void setIndices(final Indices indices);

	void setRunContext(final RunContext runContext);

	List<BubbleIndex> executeBubbleIndexTasks();

	void addBubbleIndexTask(final int key, final BubbleIndex bubbleIndexTask);

	void deployTasks();

	void shutdownGrid();
}
