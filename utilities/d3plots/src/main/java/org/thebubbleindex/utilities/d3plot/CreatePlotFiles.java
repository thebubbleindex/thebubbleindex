package org.thebubbleindex.utilities.d3plot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.thebubbleindex.utilities.d3plot.MyCallable;

/**
 *
 * @author thebubbleindex
 */
public final class CreatePlotFiles {

	static String userDir = System.getProperty("user.dir");
	static String filePathSymbol = File.separator;
	static String inputDir = "/home/green/Desktop/D3/";
	static String d3PlotHtmlTemplateFile = "/home/green/Desktop/d3PlotTemplate.html";
	static String d3FullPlotHtmlTemplateFile = "/home/green/Desktop/d3FullPlotTemplate.html";
	private static String d3PlotTemplate;
	private static String d3FullPlotTemplate;
	private static int threadNumber = Runtime.getRuntime().availableProcessors();

	static Properties properties = new Properties();

	public CreatePlotFiles() {

	}

	/**
	 * @param args
	 *            the command line arguments
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
		loadProperties(args);
		System.out.println("userDir: " + userDir);
		System.out.println("inputDir: " + inputDir);
		System.out.println("d3PlotHtmlTemplateFile: " + d3PlotHtmlTemplateFile);
		System.out.println("d3FullPlotHtmlTemplateFile: " + d3FullPlotHtmlTemplateFile);

		Velocity.init();

		new CreatePlotFiles(true);
	}

	public CreatePlotFiles(final boolean callablesBoolean)
			throws IOException, InterruptedException, ExecutionException {

		readD3PlotTemplate();
		readD3FullPlotTemplate();

		final File fileFolder = new File(inputDir);

		final File[] files = fileFolder.listFiles();

		if (callablesBoolean) {

			final ExecutorService executor = Executors.newFixedThreadPool(threadNumber);

			final List<Callable<Boolean>> callables = new ArrayList<Callable<Boolean>>();

			for (final File file : files) {
				if (file.isDirectory()) {
					for (final File subFile : file.listFiles()) {
						if (subFile.isDirectory()) {
							for (final File subSubFile : subFile.listFiles()) {
								callables.add(new MyCallable(subSubFile, new CreatePlotFiles()));
							}
						} else {
							callables.add(new MyCallable(subFile, new CreatePlotFiles()));
						}
					}
				} else {
					callables.add(new MyCallable(file, new CreatePlotFiles()));
				}
			}

			final List<Future<Boolean>> futures = executor.invokeAll(callables);

			for (final Future<Boolean> future : futures) {
				System.out.println("Results: " + (future.get() ? "Success" : "Failure"));
			}

			executor.shutdown();
		} else {
			loopThroughFiles(files);
		}
	}

	public void loopThroughFiles(final File[] files) {
		for (final File file : files) {
			if (file.isDirectory()) {
				System.out.println("Directory: " + file.getName());
				loopThroughFiles(file.listFiles());
			} else {
				try {
					if (file.getName().contains(".tsv")) {
						final String fileName = file.getName().replace(".tsv", "");
						final String fileParent = file.getParent();

						writeHTMLFiles(fileName, fileParent);
					}
				} catch (final Exception ex) {
					System.out.println("Error at file: " + file.getName() + ex);
				}
			}
		}
	}

	public void writeHTMLFiles(final String fileName, final String fileParent) throws IOException {

		final String filePDFString = "https://cdn.thebubbleindex.com/PDF/"
				+ fileParent.replace(inputDir, "") + File.separator + fileName + ".pdf";
		final String fileWebGLString = "https://cdn.thebubbleindex.com/WebGL/"
				+ fileParent.replace(inputDir, "") + File.separator + fileName + ".html";

		final File d3PlotFile = new File(fileParent + File.separator + "plot.html");
		final File d3FullPlotFile = new File(fileParent + File.separator + "fullPlot.html");
		System.out.println("D3 Plot File: " + d3PlotFile.getAbsolutePath());
		System.out.println("D3 Full Plot File: " + d3FullPlotFile.getAbsolutePath());

		if (d3PlotFile.exists()) {
			d3PlotFile.delete();
		}

		d3FullPlotFile.getParentFile().mkdirs();

		if (d3FullPlotFile.exists()) {
			d3FullPlotFile.delete();
		}

		d3FullPlotFile.getParentFile().mkdirs();

		final VelocityContext context = new VelocityContext();
		context.put("NAME", fileName);
		context.put("PDFLINK", filePDFString);
		context.put("WEBGLLINK", fileWebGLString);
		context.put("JSSCRIPT", D3PlotJavascript.jsScriptPartOne + fileName + D3PlotJavascript.jsScriptPartTwo);

		final StringWriter plotWriter = new StringWriter();

		Velocity.evaluate(context, plotWriter, "JSON D3 PLOT HTML", d3PlotTemplate);

		try (final FileWriter fw = new FileWriter(d3PlotFile)) {
			fw.write(plotWriter.toString());
			fw.close();
		}

		final StringWriter fullPlotWriter = new StringWriter();

		Velocity.evaluate(context, fullPlotWriter, "D3 FULL PLOT HTML", d3FullPlotTemplate);

		try (final FileWriter fw = new FileWriter(d3FullPlotFile)) {
			fw.write(fullPlotWriter.toString());
			fw.close();
		}

	}

	public void readD3PlotTemplate() throws IOException {
		final File file = new File(d3PlotHtmlTemplateFile);
		d3PlotTemplate = new String(Files.readAllBytes(file.toPath()));
	}

	public void readD3FullPlotTemplate() throws IOException {
		final File file = new File(d3FullPlotHtmlTemplateFile);
		d3FullPlotTemplate = new String(Files.readAllBytes(file.toPath()));
	}

	private static void loadProperties(final String[] args) {
		if (args != null && args.length > 0) {
			System.out.println("Loading properties from: " + args[0]);
			try {
				properties.load(new FileInputStream(args[0]));
			} catch (final IOException e) {
				System.out.println("Failed to load properties file: " + args[0]);
			}
		} else {
			try {
				System.out.println("Loading properties from: " + userDir + filePathSymbol + "json3d.properties");
				properties.load(new FileInputStream(userDir + filePathSymbol + "json3d.properties"));

			} catch (final IOException e) {
				System.out.println("Failed to load properties file.");
			}
		}
		if (properties.containsKey("d3PlotHtmlTemplateFile")) {
			d3PlotHtmlTemplateFile = properties.getProperty("d3PlotHtmlTemplateFile");
		}
		if (properties.containsKey("d3FullPlotHtmlTemplateFile")) {
			d3FullPlotHtmlTemplateFile = properties.getProperty("d3FullPlotHtmlTemplateFile");
		}
		if (properties.containsKey("inputDir")) {
			inputDir = properties.getProperty("inputDir");
		}
		if (properties.containsKey("userDir")) {
			userDir = properties.getProperty("userDir");
		}
		if (properties.containsKey("d3FullPlotTemplate")) {
			d3FullPlotTemplate = properties.getProperty("d3FullPlotTemplate");
		}
		if (properties.containsKey("d3PlotTemplate")) {
			d3PlotTemplate = properties.getProperty("d3PlotTemplate");
		}
		if (properties.containsKey("threadNumber")) {
			threadNumber = Integer.parseInt(properties.getProperty("threadNumber"));
		}
	}
}
