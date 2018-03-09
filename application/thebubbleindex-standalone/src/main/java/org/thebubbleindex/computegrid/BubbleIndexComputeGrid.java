package org.thebubbleindex.computegrid;

import java.util.List;

import org.thebubbleindex.driver.BubbleIndex;

/**
 * 
 * @author thebubbleindex
 *
 */
public interface BubbleIndexComputeGrid {

	List<BubbleIndex> executeBubbleIndexTasks();

	void addBubbleIndexTask(final Integer key, final BubbleIndex bubbleIndexTask);

	void deployTasks();

	void shutdownGrid();

	String about();

	void triggerStopAllTasksMessage();
}
