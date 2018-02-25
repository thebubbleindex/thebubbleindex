package org.thebubbleindex.computegrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.cache.processor.EntryProcessorResult;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.logging.log4j.Level;
import org.thebubbleindex.driver.BubbleIndex;
import org.thebubbleindex.driver.DailyDataCache;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.runnable.RunContext;

/**
 * To set up ignite: 1. Configure xml 2. Copy Bubble_Index.jar into the ignite
 * libs folder 3. Make sure JAVA_HOME is set, i.e. set JAVA_HOME=C:\Program
 * Files\Java\jre1.8.0_161 on WindowsOS 4. run multiple servers
 * 
 * @author thebubbleindex
 *
 */
public class IgniteBubbleIndexComputeGrid implements BubbleIndexComputeGrid {
	private final String BUBBLE_INDEX_TASK_CACHE_NAME = "bubbleIndexTaskCache";
	private DailyDataCache dailyDataCache;
	private Indices indices;
	private RunContext runContext;
	private Map<Integer, BubbleIndex> bubbleIndexTasks = new TreeMap<Integer, BubbleIndex>();
	private final Ignite ignite;
	private final IgniteCache<Integer, BubbleIndex> bubbleIndexTaskCache;

	public IgniteBubbleIndexComputeGrid() {
		final TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
		ipFinder.setAddresses(Collections.singleton(new String("127.0.0.1:47500..47509")));

		final TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();

		final IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
		igniteConfiguration.setClientMode(true);
		igniteConfiguration.setPeerClassLoadingEnabled(true);
		igniteConfiguration.setDiscoverySpi(discoverySpi);

		ignite = Ignition.start(igniteConfiguration);
		bubbleIndexTaskCache = ignite.getOrCreateCache(BUBBLE_INDEX_TASK_CACHE_NAME);
	}

	@Override
	public void setDailyDataCache(final DailyDataCache dailyDataCache) {
		this.dailyDataCache = dailyDataCache;
	}

	@Override
	public void setIndices(final Indices indices) {
		this.indices = indices;
	}

	@Override
	public void setRunContext(final RunContext runContext) {
		this.runContext = runContext;
	}

	@Override
	public void addBubbleIndexTask(final int key, final BubbleIndex bubbleIndexTask) {
		bubbleIndexTasks.put(new Integer(key), bubbleIndexTask);
	}

	@Override
	public void deployTasks() {
		bubbleIndexTaskCache.putAll(bubbleIndexTasks);
	}

	@Override
	public List<BubbleIndex> executeBubbleIndexTasks() {
		final List<BubbleIndex> bubbleIndexResults = new ArrayList<BubbleIndex>(bubbleIndexTasks.size());

		Logs.myLogger.log(Level.INFO, "Cache contains {} tasks.", bubbleIndexTaskCache.size(CachePeekMode.PRIMARY));

		final Set<Integer> keys = bubbleIndexTasks.keySet();
		final Map<Integer, EntryProcessorResult<BubbleIndex>> results = bubbleIndexTaskCache.invokeAll(keys,
				new IgniteBubbleIndexCacheEntryProcessor());

		for (final EntryProcessorResult<BubbleIndex> bubbleIndex : results.values()) {
			try {
				final BubbleIndex bubbleIndexResult = bubbleIndex.get();
				bubbleIndexResults.add(bubbleIndexResult);
			} catch (final Exception ex) {
				Logs.myLogger.log(Level.ERROR, ex);
			}
		}

		bubbleIndexTaskCache.clearAll(keys);
		Logs.myLogger.log(Level.INFO, "Cleared tasks from cache. Cache now contains {} tasks.",
				bubbleIndexTaskCache.size(CachePeekMode.PRIMARY));

		return bubbleIndexResults;
	}

	@Override
	public void shutdownGrid() {
		Ignition.stop(true);
	}
}
