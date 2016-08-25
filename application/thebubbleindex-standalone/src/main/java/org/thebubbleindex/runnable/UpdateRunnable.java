package org.thebubbleindex.runnable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

import org.thebubbleindex.data.URLS;
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
			final Boolean isYahooIndex, final String quandlKey, final Boolean overwrite) {

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
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public Integer call() {
		if (!RunContext.Stop) {
			final URLS selection = new URLS();
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

			if (!RunContext.Stop) {
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

			if (!RunContext.Stop) {
				if (outputstream.size() > 0) {
					try {
						selection.cleanData(outputstream);
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
