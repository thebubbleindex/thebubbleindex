package org.thebubbleindex.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.thebubbleindex.logging.Logs;

/**
 *
 * @author bigttrott
 */
public class ExportData {

	/**
	 * WriteCSV writes an output file
	 * 
	 * @param savePath
	 * @param Results
	 * @param PERIODS
	 * @param FileName
	 * @param dailyPriceDate
	 * @param UPDATE
	 * @throws IOException
	 */
	public static void WriteCSV(final String savePath, final List<Double> Results, final int PERIODS,
			final String FileName, final List<String> dailyPriceDate, final boolean UPDATE) throws IOException {

		FileWriter writer = null;

		try {
			writer = new FileWriter(savePath + File.separator + FileName, UPDATE);

			if (!UPDATE) {
				addHeader(writer);
			}

			for (int i = 0; i < Results.size(); i++) {

				writer.append(Integer.toString(PERIODS - Results.size() + i + 1));
				writer.append(',');
				writer.append(String.valueOf(Results.get(i)));
				writer.append(',');
				writer.append(dailyPriceDate.get(dailyPriceDate.size() - Results.size() + i));
				writer.append('\n');
			}

			writer.flush();
			writer.close();

		} catch (final IOException ex) {
			Logs.myLogger.error("save path = {}. {}", savePath, ex);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * addHeader writes header of output file
	 * 
	 * @param writer
	 * @throws IOException
	 */
	private static void addHeader(final FileWriter writer) throws IOException {
		writer.append("Period Number");
		writer.append(',');
		writer.append("Value");
		writer.append(',');
		writer.append("Date");
		writer.append('\n');
	}
}