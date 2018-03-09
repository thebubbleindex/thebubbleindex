package org.thebubbleindex.computegrid;

import java.util.List;

import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteMessaging;
import org.apache.ignite.cache.CacheEntryProcessor;
import org.apache.ignite.cluster.ClusterGroup;
import org.thebubbleindex.driver.BubbleIndex;
import org.thebubbleindex.runnable.RunContext;

/**
 * 
 * @author thebubbleindex
 *
 */
public class IgniteBubbleIndexCacheEntryProcessor implements CacheEntryProcessor<Integer, BubbleIndex, BubbleIndex> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1558176408168336173L;
	private static volatile boolean processTasks = true;
	private RunContext runContext;
	private boolean isResetTask;
	
	public IgniteBubbleIndexCacheEntryProcessor(final boolean isResetTask) {
		this.isResetTask = isResetTask;
	}

	@Override
	public BubbleIndex process(final MutableEntry<Integer, BubbleIndex> mutableEntry, final Object... args)
			throws EntryProcessorException {
		if (isResetTask) {
			processTasks = true;
			return null;
		}
		
		if (args == null || args.length < 3 || args[0] == null || args[1] == null || args[2] == null) {
			return null;
		}

		final BubbleIndex bubbleIndex = mutableEntry.getValue();
		runContext = bubbleIndex.getRunContext();

		final Ignite ignite = (Ignite) args[1];
		final String terminationTopicName = (String) args[2];
		final IgniteMessaging terminationMessaging = ignite.message(ignite.cluster().forLocal());

		terminationMessaging.localListen(terminationTopicName, (nodeId, msg) -> {
			System.out.println("[msg: " + msg + ", nodeId: " + nodeId);

			if (msg.toString().equals("STOP_ALL_TASKS")) {
				runContext.setStop(true);
				processTasks = false;
			}

			return true;
		});

		String errorMessage = null;

		try {
			if (processTasks)
				bubbleIndex.runBubbleIndex(null);
		} catch (final Exception ex) {
			errorMessage = ex.getMessage();
		}

		final String rmtMsgTopicName = (String) args[0];
		final ClusterGroup rmtGrp = ignite.cluster().forRemotes();
		final List<String> textOutputs = bubbleIndex.getGUITextOutputsFromComputeGrid();

		if (errorMessage != null) {
			textOutputs.add(errorMessage);
		}

		for (final String textOutput : textOutputs) {
			ignite.message(rmtGrp).send(rmtMsgTopicName, textOutput);
		}

		return bubbleIndex;
	}
}
