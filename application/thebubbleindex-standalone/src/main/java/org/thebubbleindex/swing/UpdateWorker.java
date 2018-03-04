package org.thebubbleindex.swing;

import java.util.List;
import javax.swing.SwingWorker;

import org.thebubbleindex.data.UpdateData;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.util.Utilities;

/**
 *
 * @author thebubbleindex
 */
public class UpdateWorker extends SwingWorker<Boolean, String> {

	final private GUI gui;
	final private String quandlKey;
	final private Indices indices;
	final private RunContext runContext;

	public UpdateWorker(final GUI gui, final String quandlKey, final Indices indices, final RunContext runContext) {
		this.gui = gui;
		this.quandlKey = quandlKey;
		this.indices = indices;
		this.runContext = runContext;
	}

	@Override
	protected Boolean doInBackground() {
		Logs.myLogger.info("Update button clicked");
		final UpdateData updateData = new UpdateData(this, quandlKey, indices, runContext);
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
			Utilities.displayOutput(runContext, text, false);
	}

}
