package org.thebubbleindex.data.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.thebubbleindex.data.URLS;
import org.thebubbleindex.data.UpdateData;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.runnable.RunContext;

/**
 * 
 * @author bigttrott
 *
 */
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
	public void existingYahooDataShouldBeUpdatedCorrectly() throws IOException {
		final String category = "Indices";
		final String entry = "IXIC";
		final String dailyDataFile = entry + "dailydata.csv";
		RunContext.threadNumber = 1;
		Indices.initialize();

		final List<String> previousFileLines = new ArrayList<String>();
		previousFileLines.add("2016-08-05\t5221.120117");
		previousFileLines.add("2016-08-08\t5213.140137");
		previousFileLines.add("2016-08-09\t5225.47998");
		previousFileLines.add("2016-08-10\t5204.580078");
		previousFileLines.add("2016-08-11\t5228.399902");
		previousFileLines.add("2016-08-12\t5232.890137");
		previousFileLines.add("2016-08-15\t5262.02002");
		previousFileLines.add("2016-08-16\t5227.109863");

		new File(Indices.getFilePath() + Indices.programDataFolder + Indices.filePathSymbol + category
				+ Indices.filePathSymbol + entry).mkdirs();
		final File previousDailyData = new File(
				Indices.getFilePath() + Indices.programDataFolder + Indices.filePathSymbol + category
						+ Indices.filePathSymbol + entry + Indices.filePathSymbol + dailyDataFile);
		final Path previousDailyDataPath = previousDailyData.toPath();
		Files.write(previousDailyDataPath, previousFileLines, Charset.defaultCharset(),
				StandardOpenOption.TRUNCATE_EXISTING);

		final URLS selection = new URLS();
		selection.setUpdateWorker(null);
		selection.setDataName(entry);
		selection.setYahooIndex(true);
		selection.setDataType(category);
		selection.setSource("Yahoo");
		selection.setYahooUrl();
		selection.setOverwrite(false);

		// Yahoo data is standard with most recent at "top" and has 7 columns,
		// and has a header
		String inputString = String.format("Date,Low,High,Avg,Vol,Price,AdjPrice%n");
		inputString += String.format("2016-08-24,0,0,0,0,0\t5217.689941%n");
		inputString += String.format("2016-08-23,0,0,0,0,0\t5260.080078%n");
		inputString += String.format("2016-08-22,0,0,0,0,0\t5244.600098%n");
		inputString += String.format("2016-08-19,0,0,0,0,0\t5238.379883%n");
		inputString += String.format("2016-08-18,0,0,0,0,0\t5240.149902%n");
		inputString += String.format("2016-08-17,0,0,0,0,0\t5228.660156%n");
		inputString += String.format("2016-08-16,0,0,0,0,0\t5555555555555.109863%n");// deliberate
		inputString += String.format("2016-08-15,0,0,0,0,0\t5262.02002%n");
		inputString += String.format("2016-08-12,0,0,0,0,0\t5232.890137%n");
		inputString += String.format("2016-08-11,0,0,0,0,0\t5228.399902%n");
		inputString += String.format("2016-08-10,0,0,0,0,0\t5204.580078%n");
		inputString += String.format("2016-08-09,0,0,0,0,0\t5225.47998%n");
		inputString += String.format("2016-08-08,0,0,0,0,0\t5213.140137%n");
		inputString += String.format("2016-08-05,0,0,0,0,0\t5221.120117%n");
		inputString += String.format("2016-08-04,0,0,0,0,0\t5166.25%n");
		inputString += String.format("2016-08-03,0,0,0,0,0\t5159.740234%n");
		inputString += String.format("2016-08-02,0,0,0,0,0\t5137.72998%n");
		inputString += String.format("2016-08-01,0,0,0,0,0\t5184.200195%n");

		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputString.getBytes());
		final ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
		final ReadableByteChannel rbc = Channels.newChannel(byteArrayInputStream);
		final WritableByteChannel outputChannel = Channels.newChannel(outputstream);
		URLS.fastChannelCopy(rbc, outputChannel);

		final List<String> dateData = new ArrayList<String>(1000);
		final List<String> priceData = new ArrayList<String>(1000);

		selection.parseAndCleanDataStream(outputstream, dateData, priceData);
		outputstream.close();
		selection.updateData(dateData, priceData);

		final List<String> updatedFileLines = Files.readAllLines(previousDailyData.toPath());
		assertEquals(14, updatedFileLines.size());
		assertEquals("2016-08-05\t5221.120117", updatedFileLines.get(0));
		assertEquals("2016-08-16\t5227.109863", updatedFileLines.get(7));
		assertEquals("2016-08-24\t5217.689941", updatedFileLines.get(13));

	}

	@Test
	public void yahooDataShouldBeCleanedCorrectly() throws IOException {
		final String category = "Indices";
		final String entry = "IXIC";
		final String dailyDataFile = entry + "dailydata.csv";
		RunContext.threadNumber = 1;
		Indices.initialize();

		final URLS selection = new URLS();
		selection.setUpdateWorker(null);
		selection.setDataName(entry);
		selection.setYahooIndex(true);
		selection.setDataType(category);
		selection.setSource("Yahoo");
		selection.setYahooUrl();
		selection.setOverwrite(false);

		final File previousDailyData = new File(
				Indices.getFilePath() + Indices.programDataFolder + Indices.filePathSymbol + category
						+ Indices.filePathSymbol + entry + Indices.filePathSymbol + dailyDataFile);

		Files.deleteIfExists(previousDailyData.toPath());

		// Yahoo data is standard with most recent at "top" and has 7 columns,
		// and has a header
		String inputString = String.format("Date,Low,High,Avg,Vol,Price,AdjPrice%n");
		inputString += String.format("2016-08-24,0,0,0,0,0\t5217.689941%n");
		inputString += String.format("2016-08-23,0,0,0,0,0\t5260.080078%n");
		inputString += String.format("2016-08-22,0,0,0,0,0\t5244.600098%n");
		inputString += String.format("2016-08-19,0,0,0,0,0\t5238.379883%n");
		inputString += String.format("2016-08-18,0,0,0,0,0\t5240.149902%n");
		inputString += String.format("2016-08-17,0,0,0,0,0\t5228.660156%n");
		inputString += String.format("2016-08-16,0,0,0,0,0\t5227.109863%n");// deliberate
		inputString += String.format("2016-08-15,0,0,0,0,0\t5262.02002%n");
		inputString += String.format("2016-08-12,0,0,0,0,0\t5232.890137%n");
		inputString += String.format("2016-08-11,0,0,0,0,0\t5228.399902%n");
		inputString += String.format("2016-08-10,0,0,0,0,0\t5204.580078%n");
		inputString += String.format("2016-08-09,0,0,0,0,0\t5225.47998%n");
		inputString += String.format("2016-08-08,0,0,0,0,0\t5213.140137%n");
		inputString += String.format("2016-08-05,0,0,0,0,0\t5221.120117%n");
		inputString += String.format("2016-08-04,0,0,0,0,0\t5166.25%n");
		inputString += String.format("2016-08-03,0,0,0,0,0\t5159.740234%n");
		inputString += String.format("2016-08-02,0,0,0,0,0\t5137.72998%n");
		inputString += String.format("2016-08-01,0,0,0,0,0\t5184.200195%n");
		// what I have seen is that some yahoo data has entire chucks repeated
		inputString += String.format("2016-08-24,0,0,0,0,0\t5217.689941%n");
		inputString += String.format("2016-08-23,0,0,0,0,0\t5260.080078%n");
		inputString += String.format("2016-08-22,0,0,0,0,0\t5244.600098%n");
		inputString += String.format("2016-08-19,0,0,0,0,0\t5238.379883%n");
		inputString += String.format("2016-08-18,0,0,0,0,0\t5240.149902%n");
		inputString += String.format("2016-08-17,0,0,0,0,0\t5228.660156%n");
		inputString += String.format("2016-08-16,0,0,0,0,0\t565418681681227.109863%n");// deliberate
		inputString += String.format("2016-08-15,0,0,0,0,0\t5262.02002%n");
		inputString += String.format("2016-08-12,0,0,0,0,0\t5232.890137%n");
		inputString += String.format("2016-08-11,0,0,0,0,0\t5228.399902%n");
		inputString += String.format("2016-08-10,0,0,0,0,0\t5204.580078%n");
		inputString += String.format("2016-08-09,0,0,0,0,0\t5225.47998%n");
		inputString += String.format("2016-08-08,0,0,0,0,0\t5213.140137%n");
		inputString += String.format("2016-08-05,0,0,0,0,0\t5221.120117%n");
		inputString += String.format("2016-08-04,0,0,0,0,0\t5166.25%n");
		inputString += String.format("2016-08-03,0,0,0,0,0\t5159.740234%n");
		inputString += String.format("2016-08-02,0,0,0,0,0\t5137.72998%n");
		inputString += String.format("2016-08-01,0,0,0,0,0\t5184.200195%n");
		inputString += String.format("2016-07-29,0,0,0,0,0\t5000.195%n");
		inputString += String.format("2016-07-28,0,0,0,0,0\t5100.295%n");

		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputString.getBytes());
		final ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
		final ReadableByteChannel rbc = Channels.newChannel(byteArrayInputStream);
		final WritableByteChannel outputChannel = Channels.newChannel(outputstream);
		URLS.fastChannelCopy(rbc, outputChannel);

		final List<String> dateData = new ArrayList<String>(1000);
		final List<String> priceData = new ArrayList<String>(1000);

		selection.parseAndCleanDataStream(outputstream, dateData, priceData);
		outputstream.close();
		selection.updateData(dateData, priceData);

		final List<String> updatedFileLines = Files.readAllLines(previousDailyData.toPath());

		assertEquals(20, updatedFileLines.size());
		assertEquals("2016-07-28\t5100.295", updatedFileLines.get(0));
		assertEquals("2016-08-16\t5227.109863", updatedFileLines.get(13));
		assertEquals("2016-08-24\t5217.689941", updatedFileLines.get(19));

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
