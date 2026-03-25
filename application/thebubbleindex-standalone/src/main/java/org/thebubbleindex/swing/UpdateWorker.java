package org.thebubbleindex.swing;

import java.util.List;
import javax.swing.SwingWorker;

import org.thebubbleindex.data.UpdateData;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.util.Utilities;

/**
 * UpdateWorker is a {@link SwingWorker} that downloads and updates the daily
 * price data for all configured selections on a background thread, keeping the
 * Swing GUI responsive during potentially long network operations.
 *
 * @author thebubbleindex
 */
public class UpdateWorker extends SwingWorker<Boolean, String> {

	final private GUI gui;
	final private String quandlKey;
	final private Indices indices;
	final private RunContext runContext;

	/**
	 * UpdateWorker constructor.
	 *
	 * @param gui        the parent GUI, used to reset controls when the worker
	 *                   finishes
	 * @param quandlKey  the Quandl API key used to download data from the
	 *                   Quandl service, or an empty string if Quandl is not
	 *                   used
	 * @param indices    application index configuration
	 * @param runContext shared run-time state (stop flag, GUI mode, etc.)
	 */
	public UpdateWorker(final GUI gui, final String quandlKey, final Indices indices, final RunContext runContext) {
		this.gui = gui;
		this.quandlKey = quandlKey;
		this.indices = indices;
		this.runContext = runContext;
	}

	/**
	 * doInBackground performs the data update on the Swing worker thread.
	 *
	 * @return {@code true} when the update completes
	 */
	@Override
	protected Boolean doInBackground() {
		Logs.myLogger.info("Update button clicked");
		final UpdateData updateData = new UpdateData(this, quandlKey, indices, runContext);
		updateData.run();
		return true;
	}

	/**
	 * done is called on the Event Dispatch Thread after
	 * {@link #doInBackground} completes. It resets the GUI controls to their
	 * idle state.
	 */
	@Override
	public void done() {
		gui.resetGUI();
	}

	/**
	 * publishText schedules the given text to be delivered to the
	 * {@link #process(List)} method on the Event Dispatch Thread so it can be
	 * displayed in the GUI output area.
	 *
	 * @param text the output message to display
	 */
	public void publishText(final String text) {
		publish(text);
	}

	/**
	 * process receives intermediate text chunks published by the worker thread
	 * and routes them to the GUI output area via
	 * {@link Utilities#displayOutput}.
	 *
	 * @param textList the list of text messages published since the last call
	 */
	@Override
	protected void process(final List<String> textList) {
		for (final String text : textList)
			Utilities.displayOutput(runContext, text, false);
	}

}
