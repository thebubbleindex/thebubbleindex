package org.thebubbleindex.runnable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.thebubbleindex.data.URLS;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.swing.UpdateWorker;

/**
 *
 * @author bigttrott
 */
public class UpdateRunnable implements Callable<Integer> {

	private final String category;
	private final String selections;
	private final String sources;
	private final String quandlDataSet;
	private final String quandlDataName;
	private final int quandlColumn;
	private final Boolean isYahooIndex;
	private final UpdateWorker updateWorker;
	private final String quandlKey;
	private final Boolean overwrite;
	private final Indices indices;
	private final RunContext runContext;

	/**
	 * 
	 * @param updateWorker
	 * @param Category
	 * @param Selections
	 * @param Sources
	 * @param quandlDataSet
	 * @param quandlDataName
	 * @param quandlColumn
	 * @param isYahooIndex
	 * @param quandlKey
	 */
	public UpdateRunnable(final UpdateWorker updateWorker, final String Category, final String Selections,
			final String Sources, final String quandlDataSet, final String quandlDataName, final int quandlColumn,
			final Boolean isYahooIndex, final String quandlKey, final Boolean overwrite, final Indices indices,
			final RunContext runContext) {

		this.updateWorker = updateWorker;
		this.category = Category;
		this.selections = Selections;
		this.sources = Sources;
		this.quandlDataSet = quandlDataSet;
		this.quandlDataName = quandlDataName;
		this.quandlColumn = quandlColumn;
		this.isYahooIndex = isYahooIndex;
		this.quandlKey = quandlKey;
		this.overwrite = overwrite;
		this.indices = indices;
		this.runContext = runContext;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public Integer call() {
		if (!runContext.isStop()) {
			final URLS selection = new URLS(indices, runContext);
			selection.setUpdateWorker(updateWorker);
			selection.setDataName(selections);
			selection.setYahooIndex(isYahooIndex);
			selection.setDataType(category);
			selection.setSource(sources);

			if (selection.getSource().equalsIgnoreCase("QUANDL")) {
				selection.setQuandlUrl(quandlDataSet, quandlDataName, quandlKey);
				selection.setQuandlColumn(quandlColumn);
			} else if (selection.getSource().equalsIgnoreCase("FED")) {
				selection.setFEDUrl();
			} else {
				selection.setYahooUrl();
			}

			selection.setOverwrite(overwrite);

			Logs.myLogger.info("Url Get Entry Created: {}", selection.toString());

			final ByteArrayOutputStream outputstream = new ByteArrayOutputStream();

			if (!runContext.isStop()) {
				try {
					selection.readURL_file(outputstream);
				} catch (final IOException ex) {
					Logs.myLogger.error("Category = {}, Selections = {}. {}", category, selections, ex);
					return 1;
				} catch (final Throwable th) {
					Logs.myLogger.error("Category = {}, Selections = {}. {}", category, selections, th);
					return 1;
				}
			}

			if (!runContext.isStop()) {
				if (outputstream.size() > 0) {
					try {

						final List<String> dateData = new ArrayList<String>(1000);
						final List<String> priceData = new ArrayList<String>(1000);

						selection.parseAndCleanDataStream(outputstream, dateData, priceData);
						outputstream.close();
						selection.updateData(dateData, priceData);

					} catch (final IOException ex) {
						Logs.myLogger.error("Category = {}, Selections = {}. {}", category, selections, ex);
						return 1;
					} catch (final Throwable th) {
						Logs.myLogger.error("Category = {}, Selections = {}. {}", category, selections, th);
						return 1;
					}
				} else {
					Logs.myLogger.error("Category = {}, Selections = {}. {}", category, selections,
							"Outputstream size = 0.");
					return 1;
				}
			}
			return 0;
		} else {
			return 0;
		}
	}
}
