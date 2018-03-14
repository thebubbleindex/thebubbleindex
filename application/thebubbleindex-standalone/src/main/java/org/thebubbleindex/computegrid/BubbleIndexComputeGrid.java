package org.thebubbleindex.computegrid;

import java.util.Map;

import org.thebubbleindex.driver.BubbleIndexGridTask;

/**
 * 
 * @author thebubbleindex
 *
 */
public interface BubbleIndexComputeGrid {

	void executeBubbleIndexTasks();

	void addBubbleIndexTask(final Integer key, final BubbleIndexGridTask bubbleIndexTask);

	void addAllBubbleIndexTasks(final Map<Integer, BubbleIndexGridTask> bubbleIndexTasks);

	void shutdownGrid();

	String about();

	void triggerStopAllTasksMessage();

}
