package org.thebubbleindex.computegrid;

import org.thebubbleindex.driver.BubbleIndexGridTask;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * 
 * @author thebubbleindex
 *
 */
public interface BubbleIndexComputeGrid {

	void executeBubbleIndexTasks();

	void addBubbleIndexTask(final Integer key, final BubbleIndexGridTask bubbleIndexTask);

	void addAllBubbleIndexTasks(final TIntObjectHashMap<BubbleIndexGridTask> bubbleIndexTasks);

	void shutdownGrid();

	String about();

	void triggerStopAllTasksMessage();

}
