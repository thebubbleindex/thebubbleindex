package org.thebubbleindex.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.runnable.UpdateRunnable;
import org.thebubbleindex.swing.UpdateWorker;

/**
 *
 * @author bigttrott
 */
public class UpdateData {

	private final List<String> Categories = new ArrayList<String>(45);
	private final UpdateWorker updateWorker;
	private String quandlKey = "";
	public final static String updateCategories = "UpdateCategories.csv";
	public final static String updateSelectionFile = "UpdateSelection.csv";

	public UpdateData(final UpdateWorker updateWorker, final String quandlKey) {
		this.updateWorker = updateWorker;
		this.quandlKey = quandlKey;
		Logs.myLogger.info("Initializing update.");
		init();
	}

	public void run() {
		Logs.myLogger.info("Running update.");
		
		final Map<String, Integer> errorsPerCategory = new HashMap<String, Integer>(50);

		for (final String Category : Categories) {
			int errors = 0;

			final List<String> Selections = new ArrayList<String>(500);
			final List<String> Sources = new ArrayList<String>(500);
			final List<String> quandlDataSet = new ArrayList<String>(500);
			final List<String> quandlDataName = new ArrayList<String>(500);
			final List<Integer> quandlColumn = new ArrayList<Integer>(500);
			final List<Boolean> isYahooIndex = new ArrayList<Boolean>(500);
			final List<Boolean> overwrite = new ArrayList<Boolean>(500);

			readCategoryList(Category, Selections, Sources, quandlDataSet, quandlDataName, quandlColumn, isYahooIndex,
					overwrite);

			final ExecutorService executor = Executors.newFixedThreadPool(RunContext.threadNumber);
			final List<Callable<Integer>> callables = new ArrayList<Callable<Integer>>(500);

			for (int j = 0; j < Selections.size(); j++) {
				callables.add(new UpdateRunnable(this.updateWorker, Category, Selections.get(j), Sources.get(j),
						quandlDataSet.get(j), quandlDataName.get(j), quandlColumn.get(j), isYahooIndex.get(j),
						quandlKey, overwrite.get(j)));
			}

			final List<Future<Integer>> results;
			try {
				results = executor.invokeAll(callables);

				for (final Future<Integer> futures : results) {
					try {
						errors = errors + futures.get();
						// updatelist.add(futures.get());
					} catch (final ExecutionException ex) {
						errors++;
						Logs.myLogger.error("Category Name = {}. {}", Category, ex);
					}
				}

			} catch (final InterruptedException ex) {
				Logs.myLogger.error("Category Name = {}. {}", Category, ex);
			}

			executor.shutdownNow();
			try {
				executor.awaitTermination(5, TimeUnit.SECONDS);
			} catch (final InterruptedException ex) {
				executor.shutdownNow();
				Logs.myLogger.error("While updating category {}. Await termination execution exception. {}", Category, ex);
			}

			final Integer finalErrorNumber = new Integer(errors);
			errorsPerCategory.put(Category, finalErrorNumber);
		}
		checkForErrors(errorsPerCategory);
		if (RunContext.isGUI) {
			updateWorker.publishText("Update Done!");
		} else {
			System.out.println("Update Done!");
		}
	}

	/**
	 * 
	 */
	private void init() {
		try {

			if (!new File(Indices.userDir + Indices.programDataFolder + Indices.filePathSymbol + updateCategories)
					.exists()) {
				Logs.myLogger.error("Unable to find update list... creating it.");
				try {
					new File(Indices.userDir + Indices.programDataFolder + Indices.filePathSymbol + updateCategories)
							.createNewFile();
				} catch (final IOException e) {
					Logs.myLogger.error("Unable to create update list file.");
				}
			}

			final Path filepath = new File(
					Indices.userDir + Indices.programDataFolder + Indices.filePathSymbol + updateCategories).toPath();
			Logs.myLogger.info("Filepath: {}", filepath);
			final BufferedReader reader = Files.newBufferedReader(filepath, Charset.defaultCharset());
			String line;
			while ((line = reader.readLine()) != null) {
				Categories.add(line);
			}

		} catch (final IOException ex) {
			Logs.myLogger.error("Failed to read UpdateCategories.csv... {}", ex);
			if (RunContext.isGUI) {
				updateWorker.publishText("Failed to Read File: UpdateCategories.csv");
			} else {
				System.out.println("Failed to Read File: UpdateCategories.csv");
			}
		}
	}

	/**
	 * 
	 * @param Category
	 * @param Selections
	 * @param DataTypes
	 * @param quandlDataSet
	 * @param quandlDataName
	 * @param quandlColumn
	 * @param isYahooIndex
	 */
	private void readCategoryList(final String Category, final List<String> Selections, final List<String> DataTypes,
			final List<String> quandlDataSet, final List<String> quandlDataName, final List<Integer> quandlColumn,
			final List<Boolean> isYahooIndex, final List<Boolean> overwrite) {
		try {
			final File updateFile = new File(Indices.userDir + Indices.programDataFolder + Indices.filePathSymbol
					+ Category + Indices.filePathSymbol + updateSelectionFile);
			if (!updateFile.exists()) {
				Logs.myLogger.error("Unable to find update selection list for {}... creating it.", Category);
				try {
					new File(Indices.userDir + Indices.programDataFolder + Indices.filePathSymbol + Category).mkdirs();
					updateFile.createNewFile();
					final Path tempFilePath = updateFile.toPath();
					final List<String> lines = new ArrayList<String>(1);
					lines.add(String.format(
							"Name,DataSource,QuandlDataset,QuandlName,QuandlColumn(startindex=1),isYahooIndex"));
					Files.write(tempFilePath, lines, Charset.defaultCharset());
				} catch (final IOException e) {
					Logs.myLogger.error("Unable to create update selection list for {}. Error: {}", Category, e);
				}
			}

			final Path filepath = updateFile.toPath();
			Logs.myLogger.info("Filepath: {}", filepath);

			final BufferedReader reader = Files.newBufferedReader(filepath, Charset.defaultCharset());

			// These files have headers
			String line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				final String[] splits = line.split(",");
				Selections.add(splits[0]);
				DataTypes.add(splits[1]);
				quandlDataSet.add(splits[2]);
				quandlDataName.add(splits[3]);
				quandlColumn.add(Integer.parseInt(splits[4]));
				isYahooIndex.add(Boolean.parseBoolean(splits[5]));
				if (splits.length > 6) {
					overwrite.add(Boolean.parseBoolean(splits[6]));
				} else {
					overwrite.add(new Boolean(false));
				}
			}

			Logs.myLogger.info("Found {} entries in {} update list.", Selections.size(), Category);
			if (RunContext.isGUI) {
				updateWorker.publishText("Found " + Selections.size() + " entries in " + Category + " update list.");
			} else {
				System.out.println("Found " + Selections.size() + " entries in " + Category + " update list.");
			}
		} catch (final IOException ex) {
			Logs.myLogger.error("Failed to get Name Selections from {}/UpdateSelection.csv... {}", Category, ex);
			final String cat = Category;
			if (RunContext.isGUI) {
				updateWorker.publishText("Failed to Get Selections From: " + cat + "/UpdateSelection.csv");

			} else {
				System.out.println("Failed to Get selections from: " + cat + "/UpdateSelection.csv");
			}
		}
	}

	/**
	 * 
	 */
	private void checkForErrors(final Map<String, Integer> errorsPerCategory) {
		for (final Map.Entry<String, Integer> errors : errorsPerCategory.entrySet()) {
			final String categoryName = errors.getKey();
			final int errorNumber = errors.getValue();
			if (RunContext.isGUI) {
				updateWorker.publishText(categoryName + ": Errors = " + errorNumber);
			} else {
				System.out.println(categoryName + ": Errors = " + errorNumber);
			}
		}
	}
}