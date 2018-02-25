package org.thebubbleindex.computegrid;

import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;

import org.apache.ignite.cache.CacheEntryProcessor;
import org.thebubbleindex.driver.BubbleIndex;

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

	@Override
	public BubbleIndex process(final MutableEntry<Integer, BubbleIndex> mutableEntry, final Object... args)
			throws EntryProcessorException {
		final BubbleIndex bubbleIndex = mutableEntry.getValue();
		bubbleIndex.runBubbleIndex(null);
		
		return bubbleIndex;
	}
}
