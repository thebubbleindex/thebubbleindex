package org.thebubbleindex.runnable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

import org.thebubbleindex.data.URLS;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.swing.UpdateWorker;

/**
 *
 * @author t
 */
public class UpdateRunnable implements Callable<Integer> {

	private final String Category;
	private final String Selections;
	private final String Sources;
	private final String quandlDataSet;
	private final String quandlDataName;
	private final int quandlColumn;
	private final String isYahooIndex;
	private final UpdateWorker updateWorker;
	private final String quandlKey;

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
			final String isYahooIndex, final String quandlKey) {

		this.updateWorker = updateWorker;
		this.Category = Category;
		this.Selections = Selections;
		this.Sources = Sources;
		this.quandlDataSet = quandlDataSet;
		this.quandlDataName = quandlDataName;
		this.quandlColumn = quandlColumn;
		this.isYahooIndex = isYahooIndex;
		this.quandlKey = quandlKey;
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
			selection.setDataName(Selections);

			if (isYahooIndex.equals("1")) {
				selection.isSpecial();
			}
			selection.setDataType(Category);
			selection.setSource(Sources);

			if (selection.getSource().equals("QUANDL")) {
				selection.setQuandlUrl(quandlDataSet, quandlDataName, quandlKey);
				selection.setQuandlColumn(quandlColumn);
			}

			else if (selection.getSource().equals("FED")) {
				selection.setFEDUrl();
			}

			else {

				selection.setYahooUrl();
			}

			final ByteArrayOutputStream outputstream = new ByteArrayOutputStream();

			if (!RunContext.Stop) {

				try {
					selection.readURL_file(outputstream);
				} catch (final IOException ex) {
					Logs.myLogger.error("Category = {}, Selections = {}. {}", Category, Selections, ex);
					return 1;
				} catch (final Throwable th) {
					Logs.myLogger.error("Category = {}, Selections = {}. {}", Category, Selections, th);
					return 1;
				}

			}

			if (!RunContext.Stop) {

				if (outputstream.size() > 1000) {
					try {
						selection.cleanData(outputstream);
					} catch (final IOException ex) {
						Logs.myLogger.error("Category = {}, Selections = {}. {}", Category, Selections, ex);
						return 1;
					} catch (final Throwable th) {
						Logs.myLogger.error("Category = {}, Selections = {}. {}", Category, Selections, th);
						return 1;
					}
				} else {
					Logs.myLogger.error("Category = {}, Selections = {}. {}", Category, Selections,
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
