package org.thebubbleindex.utilities.createXYZFiles;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 *
 * @author thebubbleindex
 */
public class MyCallable implements Callable<Boolean> {

	private final String category;
	private final InputCategory inputCategory;
	private final String outputFolder;
	private final Integer[] windows;

	public MyCallable(final String category, final InputCategory inputCategory, final String outputFolder,
			final Integer[] windows) {

		this.category = category;
		this.inputCategory = inputCategory;
		this.outputFolder = outputFolder;
		this.windows = windows;
	}

	@Override
	public Boolean call() {

		try {
			System.out.println("Creating XYZ files for category: " + category);
			inputCategory.createXYZfiles(outputFolder, windows);
			return true;
		} catch (final IOException ex) {
			System.out.println("" + ex);
			return false;
		}
	}
}
