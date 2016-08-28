package org.thebubbleindex.utilities.CreateD3Files;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 *
 * @author bigttrott
 */
public class MyCallable implements Callable<Boolean> {

	private final String category;
	private final InputCategory inputCategory;
	private final String outputFolder;
	private final int maxLength;
	private final int[] windows;

	public MyCallable(final String category, final InputCategory inputCategory, final int maxLength,
			final String outputFolder, final int[] windows) {

		this.category = category;
		this.inputCategory = inputCategory;
		this.maxLength = maxLength;
		this.outputFolder = outputFolder;
		this.windows = windows;
	}

	@Override
	public Boolean call() {

		try {
			System.out.println("Creating D3 files for category: " + category);
			inputCategory.createD3Files(outputFolder, maxLength, windows);
			return true;
		} catch (final IOException ex) {
			System.out.println("" + ex);
			return false;
		}
	}
}
