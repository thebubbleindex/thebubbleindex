package org.thebubbleindex.swing;

import java.util.List;
import javax.swing.SwingWorker;

import org.thebubbleindex.data.UpdateData;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.util.Utilities;

/**
 *
 * @author bigttrott
 */
public class UpdateWorker extends SwingWorker<Boolean, String> {

	final private GUI gui;
	final private String quandlKey;
	final private Indices indices;

	public UpdateWorker(final GUI gui, final String quandlKey, final Indices indices) {
		this.gui = gui;
		this.quandlKey = quandlKey;
		this.indices = indices;
	}

	@Override
	protected Boolean doInBackground() {
		Logs.myLogger.info("Update button clicked");
		final UpdateData updateData = new UpdateData(this, quandlKey, indices);
		updateData.run();
		return true;
	}

	@Override
	public void done() {
		gui.resetGUI();
	}

	public void publishText(final String text) {
		publish(text);
	}

	@Override
	protected void process(final List<String> textList) {
		for (final String text : textList)
			Utilities.displayOutput(text, false);
	}

}
