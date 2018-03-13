package org.thebubbleindex.computegrid;

import java.util.List;
import java.util.Map;

import org.thebubbleindex.driver.BubbleIndexGridTask;

/**
 * 
 * @author thebubbleindex
 *
 */
public interface BubbleIndexComputeGrid {

	List<BubbleIndexGridTask> executeBubbleIndexTasks();

	void addBubbleIndexTask(final Integer key, final BubbleIndexGridTask bubbleIndexTask);

	void addAllBubbleIndexTasks(final Map<Integer, BubbleIndexGridTask> bubbleIndexTasks);

	void shutdownGrid();

	String about();

	void triggerStopAllTasksMessage();

}
