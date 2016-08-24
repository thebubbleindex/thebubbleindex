package org.thebubbleindex.data.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.thebubbleindex.data.UpdateData;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.runnable.RunContext;

public class UpdateDataTest {

	@Test
	public void yahooDataShouldBeDownloadedCorrectly() throws IOException {
		final String category = "Stocks";
		final String entry = "TSLA";
		final String dailyDataFile = entry + "dailydata.csv";
		RunContext.threadNumber = 1;
		Indices.initialize();

		final List<String> updateCategorylines = new ArrayList<String>();
		updateCategorylines.add(String.format(category));

		new File(Indices.getFilePath() + Indices.programDataFolder).mkdirs();

		final File tempUpdateCategoriesFile = new File(Indices.getFilePath() + Indices.programDataFolder
				+ Indices.filePathSymbol + UpdateData.updateCategories);
		final Path tempFilePath = tempUpdateCategoriesFile.toPath();
		Files.write(tempFilePath, updateCategorylines, Charset.defaultCharset());

		final List<String> updateSelectionlines = new ArrayList<String>();
		updateSelectionlines.add(String
				.format("Name,DataSource,QuandlDataset,QuandlName,QuandlColumn(startindex=1),isYahooIndex,Overwrite"));
		updateSelectionlines.add(String.format("%s,Yahoo,NA,NA,-1,false,true", entry));

		new File(Indices.getFilePath() + Indices.programDataFolder + Indices.filePathSymbol + category).mkdirs();

		final File tempUpdateSelectionFile = new File(Indices.getFilePath() + Indices.programDataFolder
				+ Indices.filePathSymbol + category + Indices.filePathSymbol + UpdateData.updateSelectionFile);
		final Path tempSelectionFilePath = tempUpdateSelectionFile.toPath();
		Files.write(tempSelectionFilePath, updateSelectionlines, Charset.defaultCharset());

		final UpdateData updateData = new UpdateData(null, "");
		updateData.run();

		final File entryFile = new File(Indices.getFilePath() + Indices.programDataFolder + Indices.filePathSymbol
				+ category + Indices.filePathSymbol + entry + Indices.filePathSymbol + dailyDataFile);
		final List<String> lines = Files.readAllLines(entryFile.toPath());
		assertTrue(lines.size() > 0);
		assertEquals(lines.get(0), String.format("2010-06-29\t23.889999"));
	}
	
	@Test
	public void yahooIndexDataShouldBeDownloadedCorrectly() throws IOException {
		final String category = "Indices";
		final String entry = "IXIC";
		final String dailyDataFile = entry + "dailydata.csv";
		RunContext.threadNumber = 1;
		Indices.initialize();

		final List<String> updateCategorylines = new ArrayList<String>();
		updateCategorylines.add(String.format(category));

		new File(Indices.getFilePath() + Indices.programDataFolder).mkdirs();

		final File tempUpdateCategoriesFile = new File(Indices.getFilePath() + Indices.programDataFolder
				+ Indices.filePathSymbol + UpdateData.updateCategories);
		final Path tempFilePath = tempUpdateCategoriesFile.toPath();
		Files.write(tempFilePath, updateCategorylines, Charset.defaultCharset());

		final List<String> updateSelectionlines = new ArrayList<String>();
		updateSelectionlines.add(String
				.format("Name,DataSource,QuandlDataset,QuandlName,QuandlColumn(startindex=1),isYahooIndex,Overwrite"));
		updateSelectionlines.add(String.format("%s,Yahoo,NA,NA,-1,true,true", entry));

		new File(Indices.getFilePath() + Indices.programDataFolder + Indices.filePathSymbol + category).mkdirs();

		final File tempUpdateSelectionFile = new File(Indices.getFilePath() + Indices.programDataFolder
				+ Indices.filePathSymbol + category + Indices.filePathSymbol + UpdateData.updateSelectionFile);
		final Path tempSelectionFilePath = tempUpdateSelectionFile.toPath();
		Files.write(tempSelectionFilePath, updateSelectionlines, Charset.defaultCharset());

		final UpdateData updateData = new UpdateData(null, "");
		updateData.run();

		final File entryFile = new File(Indices.getFilePath() + Indices.programDataFolder + Indices.filePathSymbol
				+ category + Indices.filePathSymbol + entry + Indices.filePathSymbol + dailyDataFile);
		final List<String> lines = Files.readAllLines(entryFile.toPath());
		assertTrue(lines.size() > 0);
		assertEquals(lines.get(0), String.format("1971-02-05\t100.0"));
	}

	@Test
	public void fedDataShouldBeDownloadedCorrectly() throws IOException {
		final String category = "Currencies";
		final String entry = "DTWEXM";
		final String dailyDataFile = entry + "dailydata.csv";
		RunContext.threadNumber = 1;
		Indices.initialize();

		final List<String> updateCategorylines = new ArrayList<String>();
		updateCategorylines.add(String.format(category));

		new File(Indices.getFilePath() + Indices.programDataFolder).mkdirs();

		final File tempUpdateCategoriesFile = new File(Indices.getFilePath() + Indices.programDataFolder
				+ Indices.filePathSymbol + UpdateData.updateCategories);
		final Path tempFilePath = tempUpdateCategoriesFile.toPath();
		Files.write(tempFilePath, updateCategorylines, Charset.defaultCharset());

		final List<String> updateSelectionlines = new ArrayList<String>();
		updateSelectionlines.add(String
				.format("Name,DataSource,QuandlDataset,QuandlName,QuandlColumn(startindex=1),isYahooIndex,Overwrite"));
		updateSelectionlines.add(String.format("%s,FED,NA,NA,-1,false,false", entry));

		new File(Indices.getFilePath() + Indices.programDataFolder + Indices.filePathSymbol + category).mkdirs();

		final File tempUpdateSelectionFile = new File(Indices.getFilePath() + Indices.programDataFolder
				+ Indices.filePathSymbol + category + Indices.filePathSymbol + UpdateData.updateSelectionFile);
		final Path tempSelectionFilePath = tempUpdateSelectionFile.toPath();
		Files.write(tempSelectionFilePath, updateSelectionlines, Charset.defaultCharset());

		final UpdateData updateData = new UpdateData(null, "");
		updateData.run();

		final File entryFile = new File(Indices.getFilePath() + Indices.programDataFolder + Indices.filePathSymbol
				+ category + Indices.filePathSymbol + entry + Indices.filePathSymbol + dailyDataFile);
		final List<String> lines = Files.readAllLines(entryFile.toPath());
		assertTrue(lines.size() > 0);
		assertEquals(lines.get(0), String.format("1973-01-02\t108.2242"));
	}

	@Test
	public void quandlDataShouldBeDownloadedCorrectly() throws IOException {
		final String category = "Indices";
		final String entry = "DJIA";
		final String quandlDataset = "BCB";
		final String quandlName = "UDJIAD1";
		final String quandlCol = "2";
		final String dailyDataFile = entry + "dailydata.csv";
		RunContext.threadNumber = 1;
		Indices.initialize();

		final List<String> updateCategorylines = new ArrayList<String>();
		updateCategorylines.add(String.format(category));

		new File(Indices.getFilePath() + Indices.programDataFolder).mkdirs();

		final File tempUpdateCategoriesFile = new File(Indices.getFilePath() + Indices.programDataFolder
				+ Indices.filePathSymbol + UpdateData.updateCategories);
		final Path tempFilePath = tempUpdateCategoriesFile.toPath();
		Files.write(tempFilePath, updateCategorylines, Charset.defaultCharset());

		final List<String> updateSelectionlines = new ArrayList<String>();
		updateSelectionlines.add(String
				.format("Name,DataSource,QuandlDataset,QuandlName,QuandlColumn(startindex=1),isYahooIndex,Overwrite"));
		updateSelectionlines
				.add(String.format("%s,QUANDL,%s,%s,%s,false,false", entry, quandlDataset, quandlName, quandlCol));

		new File(Indices.getFilePath() + Indices.programDataFolder + Indices.filePathSymbol + category).mkdirs();

		final File tempUpdateSelectionFile = new File(Indices.getFilePath() + Indices.programDataFolder
				+ Indices.filePathSymbol + category + Indices.filePathSymbol + UpdateData.updateSelectionFile);
		final Path tempSelectionFilePath = tempUpdateSelectionFile.toPath();
		Files.write(tempSelectionFilePath, updateSelectionlines, Charset.defaultCharset());

		final UpdateData updateData = new UpdateData(null, "");
		updateData.run();

		final File entryFile = new File(Indices.getFilePath() + Indices.programDataFolder + Indices.filePathSymbol
				+ category + Indices.filePathSymbol + entry + Indices.filePathSymbol + dailyDataFile);
		final List<String> lines = Files.readAllLines(entryFile.toPath());
		assertTrue(lines.size() > 0);
		assertEquals(lines.get(0), String.format("1896-07-14\t33.43"));
	}
}
