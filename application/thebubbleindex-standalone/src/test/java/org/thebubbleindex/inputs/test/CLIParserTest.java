package org.thebubbleindex.inputs.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

/**
 * 
 * @author thebubbleindex
 *
 */
public class CLIParserTest {

	private static final String computeGridShortOption = "g";
	private static final String guiShortOption = "u";
	private static final String categoryShortOption = "c";
	private static final String selectionShortOption = "s";
	private static final String windowsShortOption = "w";
	private static final String threadsShortOption = "t";
	private static final String daysShortOption = "d";
	private static final String mcoeffShortOption = "m";
	private static final String omegaShortOption = "o";
	private static final String runTypeShortOption = "r";
	private static final String quandlKeyShortOption = "q";
	
	@Test
	public void testCLILong() throws ParseException {
		
		final String rawArgs = "-gui -grid -category Currencies -selection BITCOIN -windows 52,104,253 -threads 4 -days 21 -mcoeff 0.37 -omega 6.28 -type Single -quandl kdlfji";
		final String[] args = rawArgs.split("\\s+");
		
		final Options options = new Options();
		parseCLIArgs(args, options);

		final CommandLineParser parser = new DefaultParser();
		final CommandLine cmd = parser.parse(options, args);
		
		final boolean hasGUIOption = cmd.hasOption(guiShortOption);
		final boolean hasComputeGridOption = cmd.hasOption(computeGridShortOption);
		final String categoryName = cmd.getOptionValue(categoryShortOption);
		final String selectionName = cmd.getOptionValue(selectionShortOption);
		final String windows = cmd.getOptionValue(windowsShortOption);
		final int threads = Integer.parseInt(cmd.getOptionValue(threadsShortOption));
		final float tCrit = Float.parseFloat(cmd.getOptionValue(daysShortOption));
		final float mCoeff = Float.parseFloat(cmd.getOptionValue(mcoeffShortOption));
		final float omega = Float.parseFloat(cmd.getOptionValue(omegaShortOption));
		final String quandlKey = cmd.getOptionValue(quandlKeyShortOption);
		
		assert hasGUIOption;
		assert hasComputeGridOption;
		assert categoryName.equals("Currencies");
		assert selectionName.equals("BITCOIN");
		assert windows.equals("52,104,253");
		assert threads == 4;
		assert tCrit == 21f;
		assert mCoeff == 0.37f;
		assert omega == 6.28f;
		assert quandlKey.equals("kdlfji");
	}
	
	@Test
	public void testCLIShort() throws ParseException {
		
		final String rawArgs = "-u -g -c Currencies -s BITCOIN -w 52,104,253 -t 4 -d 21 -m 0.37 -o 6.28 -t Single -q kdlfji";
		final String[] args = rawArgs.split("\\s+");
		
		final Options options = new Options();
		parseCLIArgs(args, options);

		final CommandLineParser parser = new DefaultParser();
		final CommandLine cmd = parser.parse(options, args);
		
		final boolean hasGUIOption = cmd.hasOption(guiShortOption);
		final boolean hasComputeGridOption = cmd.hasOption(computeGridShortOption);
		final String categoryName = cmd.getOptionValue(categoryShortOption);
		final String selectionName = cmd.getOptionValue(selectionShortOption);
		final String windows = cmd.getOptionValue(windowsShortOption);
		final int threads = Integer.parseInt(cmd.getOptionValue(threadsShortOption));
		final float tCrit = Float.parseFloat(cmd.getOptionValue(daysShortOption));
		final float mCoeff = Float.parseFloat(cmd.getOptionValue(mcoeffShortOption));
		final float omega = Float.parseFloat(cmd.getOptionValue(omegaShortOption));
		final String quandlKey = cmd.getOptionValue(quandlKeyShortOption);
		
		assert hasGUIOption;
		assert hasComputeGridOption;
		assert categoryName.equals("Currencies");
		assert selectionName.equals("BITCOIN");
		assert windows.equals("52,104,253");
		assert threads == 4;
		assert tCrit == 21f;
		assert mCoeff == 0.37f;
		assert omega == 6.28f;
		assert quandlKey.equals("kdlfji");
	}
	
	@Test
	public void testCLICombined() throws ParseException {
		
		final String rawArgs = "-gui -g -c Currencies -selection BITCOIN -w 52,104,253 -t 4 -d 21 -mcoeff 0.37 -o 6.28 -t Single -q kdlfji";
		final String[] args = rawArgs.split("\\s+");
		
		final Options options = new Options();
		parseCLIArgs(args, options);

		final CommandLineParser parser = new DefaultParser();
		final CommandLine cmd = parser.parse(options, args);
		
		final boolean hasGUIOption = cmd.hasOption(guiShortOption);
		final boolean hasComputeGridOption = cmd.hasOption(computeGridShortOption);
		final String categoryName = cmd.getOptionValue(categoryShortOption);
		final String selectionName = cmd.getOptionValue(selectionShortOption);
		final String windows = cmd.getOptionValue(windowsShortOption);
		final int threads = Integer.parseInt(cmd.getOptionValue(threadsShortOption));
		final float tCrit = Float.parseFloat(cmd.getOptionValue(daysShortOption));
		final float mCoeff = Float.parseFloat(cmd.getOptionValue(mcoeffShortOption));
		final float omega = Float.parseFloat(cmd.getOptionValue(omegaShortOption));
		final String quandlKey = cmd.getOptionValue(quandlKeyShortOption);
		
		assert hasGUIOption;
		assert hasComputeGridOption;
		assert categoryName.equals("Currencies");
		assert selectionName.equals("BITCOIN");
		assert windows.equals("52,104,253");
		assert threads == 4;
		assert tCrit == 21f;
		assert mCoeff == 0.37f;
		assert omega == 6.28f;
		assert quandlKey.equals("kdlfji");
	}
	
	private void parseCLIArgs(final String[] args, final Options options) {
		final Option guiInputOption = new Option(guiShortOption, "gui", false, "GUI");
		guiInputOption.setRequired(false);
		options.addOption(guiInputOption);

		final Option computeGridInputOption = new Option(computeGridShortOption, "grid", false, "compute grid");
		computeGridInputOption.setRequired(false);
		options.addOption(computeGridInputOption);

		final Option categoryNameInputOption = new Option(categoryShortOption, "category", true, "category name");
		categoryNameInputOption.setRequired(false);
		options.addOption(categoryNameInputOption);

		final Option selectionNameInputOption = new Option(selectionShortOption, "selection", true, "selection name");
		selectionNameInputOption.setRequired(false);
		options.addOption(selectionNameInputOption);

		final Option windowsInputOption = new Option(windowsShortOption, "windows", true, "data windows");
		windowsInputOption.setRequired(false);
		options.addOption(windowsInputOption);

		final Option threadsInputOption = new Option(threadsShortOption, "threads", true, "num threads");
		threadsInputOption.setRequired(false);
		options.addOption(threadsInputOption);

		final Option daysInputOption = new Option(daysShortOption, "days", true, "days to critical time");
		daysInputOption.setRequired(false);
		options.addOption(daysInputOption);

		final Option mcoeffInputOption = new Option(mcoeffShortOption, "mcoeff", true, "M Coefficient");
		mcoeffInputOption.setRequired(false);
		options.addOption(mcoeffInputOption);

		final Option omegaInputOption = new Option(omegaShortOption, "omega", true, "omega");
		omegaInputOption.setRequired(false);
		options.addOption(omegaInputOption);

		final Option runTypeInputOption = new Option(runTypeShortOption, "type", true, "run type");
		runTypeInputOption.setRequired(false);
		options.addOption(runTypeInputOption);

		final Option quandlKeyInputOption = new Option(quandlKeyShortOption, "quandl", true, "quandl key");
		quandlKeyInputOption.setRequired(false);
		options.addOption(quandlKeyInputOption);
	}
}
