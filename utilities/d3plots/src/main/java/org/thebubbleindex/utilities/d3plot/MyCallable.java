package org.thebubbleindex.utilities.d3plot;

import java.io.File;
import java.util.concurrent.Callable;

import org.thebubbleindex.utilities.d3plot.CreatePlotFiles;

/**
 *
 * @author bigttrott
 */
public class MyCallable implements Callable<Boolean> {

	private final File file;
	private final CreatePlotFiles cassVar;

	public MyCallable(final File file, final CreatePlotFiles cassVar) {
		this.cassVar = cassVar;
		this.file = file;
	}

	@Override
	public Boolean call() {

		try {
			if (file.getName().contains(".tsv")) {
				final String fileName = file.getName().replace(".tsv", "");
				final String fileParent = file.getParent();

				cassVar.writeHTMLFiles(fileName, fileParent);
			}

			return true;
		} catch (final Exception ex) {
			System.out.println("" + ex);
			return false;
		}
	}
}
