package org.thebubbleindex.utilities.jsonhtml;

import de.raida.cad.dex.CADExportContainer;
import de.raida.cad.dex.cadexport.json.JSONExporterImplementation;
import de.raida.cad.dex.cadimport.dxf.loader.DXFImporterImplementation;
import de.raida.cad.dex.interfaces.CADExportInterface;
import de.raida.cad.dex.interfaces.CADImportInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import java.text.SimpleDateFormat;

/**
 *
 * @author bigttrott
 */
public final class CreateJSON3DFiles {

	public static final String inputDir = "/home/green/Desktop/XYZdxfFiles/";
	public static final String outputDir = "/home/green/Desktop/HTMLContours/";
	public static final String htmlTemplateFile = "/home/green/Desktop/template.html";
	public static final String dailyDataFolder = "/media/green/Data/Dropbox/BubbleIndex/Version4/ProgramData/";
	public static final double dailyDataConstant = 30.0;
	private static String template;
	private static int threadNumber = 4;
	private String begDateString;
	private String begDateStringStd;
	private String endDateString;
	private String endDateStringStd;
	private int maxWindow;
	private double maxValue;

	/**
	 * @param args
	 *            the command line arguments
	 * @throws java.io.IOException
	 */
	public static void main(final String[] args) throws IOException, InterruptedException, ExecutionException {
		Velocity.init();

		new CreateJSON3DFiles(true);
	}

	private int getMaxWinow(final File dataParentDir, final String indexName) {
		final File[] files = dataParentDir.listFiles();
		int maxWindow = 0;
		for (final File file : files) {
			if (file.getName().contains("days.csv")) {
				final String cleanName = file.getName().replace(indexName, "").replace("days.csv", "");

				try {
					if (Integer.parseInt(cleanName) > maxWindow) {
						maxWindow = Integer.parseInt(cleanName);
					}
				} catch (final Exception ex) {

				}
			}
		}
		return maxWindow;
	}

	private double getMaxValue(final String newFileName, final String indexName) {
		final String xyzDataFileName = newFileName.replace("XYZdxfFiles", "XYZConversion").replace("values.json",
				"maxValue.csv");
		final File xyzFile = new File(xyzDataFileName);
		double maxValue = 0.0;
		if (xyzFile.exists()) {
			try {
				final String fileContents = new String(Files.readAllBytes(xyzFile.toPath()));
				maxValue = Double.parseDouble(fileContents);

			} catch (Exception ex) {
			}
		}
		return maxValue;
	}

	public CreateJSON3DFiles() {

	}

	public CreateJSON3DFiles(final boolean callablesBoolean)
			throws IOException, InterruptedException, ExecutionException {

		readTemplate();

		final File fileFolder = new File(inputDir);

		final File[] files = fileFolder.listFiles();

		if (callablesBoolean) {

			final ExecutorService executor = Executors.newFixedThreadPool(threadNumber);

			final List<Callable<Boolean>> callables = new ArrayList<>();

			for (final File file : files) {
				if (file.isDirectory()) {
					for (final File subFile : file.listFiles()) {
						if (subFile.isDirectory()) {
							for (final File subSubFile : subFile.listFiles()) {
								callables.add(new MyCallable(subSubFile, new CreateJSON3DFiles()));
							}
						} else {
							callables.add(new MyCallable(subFile, new CreateJSON3DFiles()));
						}
					}
				} else {
					callables.add(new MyCallable(file, new CreateJSON3DFiles()));
				}
			}

			final List<Future<Boolean>> futures = executor.invokeAll(callables);

			for (final Future<Boolean> future : futures) {
				System.out.println("Results: " + (future.get() ? "Success" : "Failure"));
			}

			executor.shutdown();
		} else {
			loopThroughFilesConvertToJSON(files);
		}
	}

	public void loopThroughFilesConvertToJSON(final File[] files) {
		for (final File file : files) {
			if (file.isDirectory()) {
				System.out.println("Directory: " + file.getName());
				loopThroughFilesConvertToJSON(file.listFiles());
			} else {
				try {
					final String fileName = file.getName();
					final String filePath = file.getAbsolutePath();

					if (fileName.contains(".dxf")) {
						System.out.println("Path: " + file.getAbsolutePath());
						final CADImportInterface cadImportInterface = new DXFImporterImplementation();
						cadImportInterface.importFile(filePath, null);

						// Write the JSON file
						final String editFileName = filePath.replace(".dxf", "");// remove
																					// dxf
																					// extension
																					// from
																					// file
																					// name
						final String newFilePath = editFileName + ".json";

						final CADExportInterface cadExportInterface = new JSONExporterImplementation();
						cadExportInterface.exportFile(newFilePath,
								new CADExportContainer(cadImportInterface, newFilePath), null);

						editJSONFile(newFilePath, file.getParentFile().getName());
						int maxWindow = 0;
						double maxValue = 0.0;
						writeJSONDailyDataDir(newFilePath, file.getParentFile().getName());
						writeHTMLFile(newFilePath, file.getParentFile().getName());
					}
				} catch (final Exception ex) {
					System.out.println("Error at file: " + file.getName() + ex);
				}
			}
		}
	}

	public static void editJSONFile(final String jsonFolderDir, final String folderName)
			throws FileNotFoundException, IOException {
		final String jsonFilePath = jsonFolderDir + System.getProperty("file.separator") + "1.json";
		final String jsonFileOutput = jsonFilePath.replace("XYZdxfFiles", "HTMLContours")
				.replace("values.json" + System.getProperty("file.separator") + "1.json", folderName + ".json");

		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		File inputFile = null;
		File outputFile = null;

		Scanner sc = null;
		try {
			inputFile = new File(jsonFilePath);
			outputFile = new File(jsonFileOutput);

			if (!outputFile.exists()) {
				outputFile.getParentFile().mkdirs();
				outputFile.createNewFile();
			}

			inputStream = new FileInputStream(inputFile);
			outputStream = new FileOutputStream(outputFile);

			sc = new Scanner(inputStream, "UTF-8");
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				line = line.replace("\n", "").replace("\r", "").replace("\t", "").trim();
				// get the content in bytes
				final byte[] contentInBytes = line.getBytes();

				outputStream.write(contentInBytes);
			}
			outputStream.flush();
			outputStream.close();
			// note that Scanner suppresses exceptions
			if (sc.ioException() != null) {
				throw sc.ioException();
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (sc != null) {
				sc.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	public void writeHTMLFile(final String newFileName, final String folderName) throws IOException {

		final String outputFile = newFileName.replace("XYZdxfFiles", "HTMLContours").replace("values.json",
				folderName + ".html");

		final File file = new File(outputFile);

		if (file.exists()) {
			file.delete();
		}

		file.getParentFile().mkdirs(); // If the directory containing the file
										// and/or its parent(s) does not exist

		final File fileJSModels = new File(newFileName + System.getProperty("file.separator") + "models.js");
		final String modelsJSFile = new String(Files.readAllBytes(fileJSModels.toPath()));
		final String[] lines = modelsJSFile.split(System.getProperty("line.separator"));

		final VelocityContext context = new VelocityContext();
		context.put("NAME", folderName);
		context.put("BOUNDINGBOXMAXIMUM", lines[4]);
		context.put("SCALECONSTANT", "30");
		context.put("JSONFILE", "'" + folderName + ".json'");
		context.put("DIRECTIONJSON", "'" + folderName + "dir.json'");
		context.put("SIZECONSTANT", "30");
		context.put("XTEXTPOSITION", "10");
		context.put("YTEXTPOSITION", "10");
		context.put("ZTEXTPOSITION", "10");
		context.put("BEGDATE", (begDateString != null) ? begDateString : " ");
		context.put("ENDDATE", (endDateString != null) ? endDateString : " ");
		context.put("BEGDATESTD", (begDateStringStd != null) ? begDateStringStd : " ");
		context.put("ENDDATESTD", (endDateStringStd != null) ? endDateStringStd : " ");
		context.put("MAXWINDOW", String.valueOf(maxWindow));
		context.put("MAXVALUE", String.valueOf(maxValue));

		final StringWriter writer = new StringWriter();

		Velocity.evaluate(context, writer, "JSON HTML", template);

		try (final FileWriter fw = new FileWriter(file)) {
			fw.write(writer.toString());
			fw.close();
		}
	}

	public void readTemplate() throws IOException {
		final File file = new File(htmlTemplateFile);
		template = new String(Files.readAllBytes(file.toPath()));
	}

	public void writeJSONDailyDataDir(final String newFileName, final String folderName) throws IOException {
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		final String outputFile = newFileName.replace("XYZdxfFiles", "HTMLContours").replace("values.json",
				folderName + "dir.json");

		final String dailyDataFile = newFileName.replace(inputDir, dailyDataFolder).replace("values.json",
				folderName + "dailydata.csv");

		final File dailyData = new File(dailyDataFile);
		final File dataParentDir = dailyData.getParentFile();
		maxValue = getMaxValue(newFileName, folderName);
		maxWindow = getMaxWinow(dataParentDir, folderName);
		final String dailyDataContents = new String(Files.readAllBytes(dailyData.toPath()));
		final String[] dailyDataLines = dailyDataContents.split(System.getProperty("line.separator"));

		begDateString = dailyDataLines[0].split("\\s+")[0];
		try {
			begDateStringStd = new SimpleDateFormat("MM/dd/yyyy")
					.format(formatter.parse(dailyDataLines[0].split("\\s+")[0]));
		} catch (final ParseException ex) {
		}

		endDateString = dailyDataLines[dailyDataLines.length - 1].split("\\s+")[0];
		try {
			endDateStringStd = new SimpleDateFormat("MM/dd/yyyy")
					.format(formatter.parse(dailyDataLines[dailyDataLines.length - 1].split("\\s+")[0]));
		} catch (final ParseException ex) {
		}

		final double[] logPrices = new double[dailyDataLines.length];
		double maxLog = 0.0;
		double minLog = 100000000000.0;

		for (int i = 0; i < dailyDataLines.length; i++) {
			String[] line = dailyDataLines[i].split("\\s+");

			try {
				logPrices[i] = Math.log(Double.parseDouble(line[1]));
			} catch (final NumberFormatException | ArrayIndexOutOfBoundsException ex) {
				logPrices[i] = 0.0;
			}
			if (logPrices[i] > maxLog)
				maxLog = logPrices[i];
			if (logPrices[i] < minLog)
				minLog = logPrices[i];
		}

		final double[] delta = new double[dailyDataLines.length];
		final double[] results = new double[dailyDataLines.length - 1];

		for (int i = 0; i < dailyDataLines.length; i++) {
			if (i == 0) {
				delta[i] = 0.0;
			} else {
				final double deltaNoScale = (logPrices[i] - minLog) * 1.0 / (maxLog - minLog);
				delta[i] = deltaNoScale * dailyDataConstant;
			}
		}

		for (int i = 0; i < dailyDataLines.length - 1; i++) {
			results[i] = delta[i + 1] - delta[i];
		}
		final File file = new File(outputFile);

		if (file.exists()) {
			file.delete();
		}

		file.getParentFile().mkdirs();

		int size = results.length;

		try (final FileWriter fw = new FileWriter(file)) {
			fw.write("{" + '"' + "size" + '"' + ":" + String.valueOf(size) + "," + '"' + "vertices" + '"' + ":["
					+ String.valueOf(results[0]));

			for (int i = 1; i < results.length; i++) {
				fw.write("," + String.valueOf(results[i]));
			}

			fw.write("]}");
			fw.close();
		}
	}
}
