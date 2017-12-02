package org.thebubbleindex.plot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.thebubbleindex.exception.FailedToRunIndex;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.swing.BubbleIndexWorker;
import org.thebubbleindex.util.Utilities;

/**
 * Plots the derivatives of The Bubble Index with JFreeChart
 *
 * @author bigttrott
 */
public class DerivativePlot {
	private final int maxWindows = 4;
	private final List<Integer> backtestDayLengths = new ArrayList<Integer>(maxWindows);
	private final List<String> dailyPriceData;
	private final Date begDate;
	private final Date endDate;
	private final Indices indices;
	private final ThreadLocal<SimpleDateFormat> dateformat = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	/** MouseEvent X & Y. */
	private int m_iX, m_iY;
	private double m_dX, m_dXX;
	/**
	 * @param index0Value
	 *            stores the mouse position relative to price data
	 * @param indexValue
	 *            stores the mouse position relative to deriv data
	 */
	private double index0Value = 0.0;
	private final double indexValue[] = { 0.0, 0.0, 0.0, 0.0 };

	public final ChartPanel chartPanel;
	private final String selectionName;
	private final List<String> dailyPriceDate;
	private final String categoryName;
	private final boolean isCustomRange;
	private int dailyPriceDataSize;
	private final BubbleIndexWorker bubbleIndexWorker;

	static {
		// set a theme using the new shadow generator feature available in
		// 1.0.14 - for backwards compatibility it is not enabled by default
		ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow", true));
	}

	public DerivativePlot(final BubbleIndexWorker bubbleIndexWorker, final String categoryName,
			final String selectionName, final String windowsString, final Date begDate, Date endDate,
			final boolean isCustomRange, final List<String> dailyPriceData, final List<String> dailyPriceDate,
			final Indices indices) {
		Logs.myLogger.info("Initializing Bubble Index Derivative Plot.");

		this.bubbleIndexWorker = bubbleIndexWorker;
		this.selectionName = selectionName;
		this.dailyPriceDate = dailyPriceDate;
		this.categoryName = categoryName;
		this.isCustomRange = isCustomRange;
		this.dailyPriceData = dailyPriceData;
		this.begDate = begDate;
		this.endDate = endDate;
		this.indices = indices;

		dailyPriceDataSize = dailyPriceData.size();
		final String windowsStringArray[] = windowsString.split(",");
		final Set<Integer> windowSet = new HashSet<Integer>(maxWindows);
		for (int i = 0; i < windowsStringArray.length; i++) {
			if (backtestDayLengths.size() >= maxWindows)
				break;
			final Integer window = Integer.parseInt(windowsStringArray[i]);
			if (!windowSet.contains(window)) {
				backtestDayLengths.add(window);
				windowSet.add(window);
			}
		}
		final String title = "The Bubble Index\u2122: " + this.selectionName + " (Derivatives)";
		chartPanel = createChart();

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				drawDerivativePlot(title);
			}
		});
	}

	/**
	 * DerivativePlot manages the display settings of the JFreeChart
	 * 
	 * @param title
	 */
	public void drawDerivativePlot(final String title) {
		// get the screen size as a java dimension
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = screenSize.height * 9 / 10;
		int width = screenSize.width * 9 / 10;

		// adjust several settings of the frame and display
		final JFrame f = new JFrame(title);
		f.setPreferredSize(new Dimension(width, height));
		f.setTitle(title);
		f.setLayout(new BorderLayout(0, 5));
		f.add(chartPanel, BorderLayout.CENTER);
		chartPanel.setMouseWheelEnabled(true);
		chartPanel.setHorizontalAxisTrace(true);
		chartPanel.setVerticalAxisTrace(true);

		// add a mouse listener
		chartPanel.addChartMouseListener(new ChartMouseListener() {
			@Override
			public void chartMouseClicked(final ChartMouseEvent event) {
				// Do nothing
			}

			@Override
			public void chartMouseMoved(final ChartMouseEvent e) {
				int iX = e.getTrigger().getX();
				m_iX = iX;
				m_dX = (double) iX;
				drawRTInfo();
			}
		});

		final JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		f.add(panel, BorderLayout.SOUTH);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				final int result = JOptionPane.showConfirmDialog(f, "Are you sure?");
				if (result == JOptionPane.OK_OPTION) {
					f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					f.setVisible(false);
					f.dispose();
				}
			}
		});

		f.setVisible(true);
	}

	/**
	 * Creates a chart.
	 * 
	 * @return A chart.
	 */
	private ChartPanel createChart() {
		// Create datasets
		final XYDataset dataset = createDerivDataset();
		final XYDataset dataset2 = createDataset();

		final JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"The Bubble Index\u2122: " + selectionName + " (Derivatives)", // title
				"Date", // x-axis label
				"Derivative Value (Relative)", // y-axis label
				dataset, // data
				true, // create legend?
				false, // generate tooltips?
				false // generate URLs?
		);

		chart.setBackgroundPaint(Color.white);

		// Create deriv dataset and add to the plot
		final XYPlot plot = (XYPlot) chart.getPlot();

		final NumberAxis axis2 = new NumberAxis("Price");
		axis2.setAutoRangeIncludesZero(false);

		plot.setRangeAxis(1, axis2);
		plot.setDataset(1, dataset2);
		plot.mapDatasetToRangeAxis(1, 1);
		plot.setBackgroundPaint(Color.BLACK);
		plot.setDomainGridlinesVisible(false);
		plot.setRangeGridlinesVisible(false);
		// plot.setDomainGridlinePaint(Color.white);
		// plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));

		// draw lines
		final XYItemRenderer r = plot.getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			final XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(false);
			renderer.setBaseShapesFilled(false);
			renderer.setDrawSeriesLineAsPath(true);
			renderer.setSeriesPaint(0, Color.WHITE);
			renderer.setSeriesPaint(1, Color.GREEN);
			renderer.setSeriesPaint(2, Color.BLUE);
			renderer.setSeriesPaint(3, Color.RED);
		}

		final XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
		renderer2.setBaseShapesVisible(false);
		renderer2.setBaseShapesFilled(false);
		renderer2.setDrawSeriesLineAsPath(true);
		renderer2.setSeriesPaint(0, Color.YELLOW);
		plot.setRenderer(1, renderer2);

		// init date axis
		final DateAxis axis = (DateAxis) plot.getDomainAxis();
		if (isCustomRange) {
			// Set the horizontal range
			axis.setRange(begDate, endDate);
		}
		axis.setTimeline(SegmentedTimeline.newMondayThroughFridayTimeline());

		return new ChartPanel(chart);
	}

	/*
	 * createDataset the Price Data time series
	 * 
	 * @return dataset containing the Price time series
	 */
	private XYDataset createDataset() {
		Logs.myLogger.info("Creating data set for the price data.");

		final TimeSeriesCollection dataset = new TimeSeriesCollection();
		final TimeSeries s1 = new TimeSeries(selectionName + " Price Data");

		final List<Double> DoubleValues = new ArrayList<Double>(dailyPriceDataSize);
		listToDouble(dailyPriceData, DoubleValues);

		for (int i = 0; i < dailyPriceDataSize; i++) {
			if (isCustomRange) {
				Date tempDate = null;
				try {
					tempDate = dateformat.get().parse(dailyPriceDate.get(i));
				} catch (final ParseException e) {
				}
				final int compareBegValue = tempDate.compareTo(begDate);
				final int compareEndValue = tempDate.compareTo(endDate);
				if (compareBegValue < 0 || compareEndValue > 0) {
					continue;
				}
			}
			final int[] DateValuesArray = new int[3];
			getDateValues(DateValuesArray, dailyPriceDate.get(i));

			s1.add(new Day(DateValuesArray[2], DateValuesArray[1], DateValuesArray[0]), DoubleValues.get(i));
		}

		dataset.addSeries(s1);
		return dataset;
	}

	/**
	 * createDerivDataset create the dataset containing derivative values
	 * 
	 * @return
	 */
	private XYDataset createDerivDataset() {
		Logs.myLogger.info("Creating derivative data set for each window.");
		if (RunContext.isGUI) {
			bubbleIndexWorker.publishText("Creating derivative data set for each window.");
		} else {
			System.out.println("Creating derivative data set for each window.");
		}
		final TimeSeriesCollection dataset = new TimeSeriesCollection();
		final TimeSeries[] TimeSeriesArray = new TimeSeries[4];

		for (int i = 0; i < backtestDayLengths.size(); i++) {

			TimeSeriesArray[i] = new TimeSeries(
					selectionName + " " + Integer.toString(backtestDayLengths.get(i)) + " Deriv.");

			final List<String> DateList = new ArrayList<String>(dailyPriceDataSize);
			final List<String> DataListString = new ArrayList<String>(dailyPriceDataSize);
			final List<Double> DataListDouble = new ArrayList<Double>(dailyPriceDataSize);
			final List<Double> DataListDeriv = new ArrayList<Double>(dailyPriceDataSize);

			final String previousFilePath = indices.getUserDir() + "ProgramData" + indices.getFilePathSymbol()
					+ categoryName + indices.getFilePathSymbol() + selectionName + indices.getFilePathSymbol()
					+ selectionName + Integer.toString(backtestDayLengths.get(i)) + "days.csv";

			if (new File(previousFilePath).exists()) {
				Logs.myLogger.info("Found previous file = {}", previousFilePath);

				try {
					Utilities.ReadValues(previousFilePath, DataListString, DateList, true, true);
				} catch (final FailedToRunIndex ex) {
					if (RunContext.isGUI) {
						bubbleIndexWorker.publishText("Failed to read previous file: " + previousFilePath);
					} else {
						System.out.println("Failed to read previous file: " + previousFilePath);
					}
				}
				DataListDeriv.add(0.0);
				listToDouble(DataListString, DataListDouble);

				for (int j = 1; j < DataListDouble.size(); j++) {
					DataListDeriv.add(DataListDouble.get(j) / DataListDouble.get(j - 1) - 1.0);
				}

				// Limit the deriv values to [-0.5, 0.5] for display
				for (int j = 1; j < DataListDeriv.size(); j++) {

					if (DataListDeriv.get(j) > 0.5) {
						DataListDeriv.set(j, 0.5);
					}

					if (DataListDeriv.get(j) < -0.5) {
						DataListDeriv.set(j, -0.5);
					}
				}

				for (int j = 0; j < DataListDeriv.size(); j++) {
					if (isCustomRange) {
						Date tempDate = null;
						try {
							tempDate = dateformat.get().parse(DateList.get(j));
						} catch (final ParseException e) {
						}
						final int compareBegValue = tempDate.compareTo(begDate);
						final int compareEndValue = tempDate.compareTo(endDate);
						if (compareBegValue < 0 || compareEndValue > 0) {
							continue;
						}
					}

					final int[] DateValuesArray = new int[3];
					getDateValues(DateValuesArray, DateList.get(j));

					TimeSeriesArray[i].add(new Day(DateValuesArray[2], DateValuesArray[1], DateValuesArray[0]),
							DataListDeriv.get(j));
				}
			}

			dataset.addSeries(TimeSeriesArray[i]);
		}
		return dataset;
	}

	/**
	 * getDateValues
	 * 
	 * @param DateValues
	 * @param DateString
	 */
	public void getDateValues(final int[] DateValues, final String DateString) {
		// format of string is YYYY-MM-DD
		final String[] temp = DateString.split("-");
		if (temp.length == DateValues.length) {
			for (int i = 0; i < temp.length; i++) {
				DateValues[i] = Integer.parseInt(temp[i]);
			}
		}

		else {
			Logs.myLogger.error("Invalid input. temp.lenght = {}, DateValues.lenght = {}.", temp.length,
					DateValues.length);
		}
	}

	/**
	 * listToDouble
	 * 
	 * @param DataListString
	 * @param DataListDouble
	 */
	private void listToDouble(final List<String> DataListString, final List<Double> DataListDouble) {
		for (String DataListString1 : DataListString) {
			DataListDouble.add(Double.parseDouble(DataListString1));
		}
	}

	/**
	 * WZW override Draw a realtime tooltip at top banner(store X,Y,... value)
	 */
	private void drawRTInfo() {
		final Rectangle2D screenDataArea = chartPanel.getScreenDataArea(this.m_iX, this.m_iY);

		if (screenDataArea == null)
			return;

		final Graphics2D g2 = (Graphics2D) chartPanel.getGraphics();
		final int iDAMinX = (int) screenDataArea.getMinX();
		final int iDAMinY = (int) screenDataArea.getMinY();

		final XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
		final DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
		final RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();

		final double dXX = dateAxis.java2DToValue(this.m_dX, screenDataArea, domainAxisEdge);
		this.m_dXX = dXX;
		// ^Get title and data
		final ArrayList<String> alInfo = getInfo();
		final int iLenInfo = alInfo.size();
		// ^Customize dynamic tooltip display
		String[] asV;
		String sT, sV;
		final FontMetrics fontMetrics = g2.getFontMetrics();
		final int iFontHgt = fontMetrics.getHeight();

		g2.setColor(Color.BLACK);
		g2.fillRect(iDAMinX + 1, iDAMinY, 100, 18 * iFontHgt); // +1/-1 = offset

		g2.setColor(Color.WHITE);

		final TimeSeriesCollection collect2 = (TimeSeriesCollection) plot.getDataset(1);

		if (this.m_dXX < collect2.getXValue(0, 0)) {
			this.index0Value = 0.0;
		}

		else {
			final int[] closeValues = collect2.getSurroundingItems(0, (long) this.m_dXX);
			this.index0Value = collect2.getYValue(0, closeValues[0]);
		}

		final TimeSeriesCollection collect = (TimeSeriesCollection) plot.getDataset(0);

		for (int i = 0; i < collect.getSeriesCount(); i++) {
			if (this.m_dXX < collect.getXValue(i, 0)) {
				this.indexValue[i] = 0.0;
			}

			else {
				final int[] closeValues = collect.getSurroundingItems(i, (long) this.m_dXX);
				this.indexValue[i] = collect.getYValue(i, closeValues[0]);
			}
		}

		for (int i = iLenInfo - 1; i >= 0; i--) {
			asV = alInfo.get(i).split("\\|");
			sT = asV[0];
			sV = asV[1];
			g2.drawString(sT, iDAMinX + 20, iDAMinY + (i * 3 + 1) * iFontHgt);

			g2.drawString(sV, iDAMinX + 20, iDAMinY + (i * 3 + 2) * iFontHgt);
		}

		g2.dispose();
	}

	/*
	 * WZW override get Info
	 */
	private ArrayList<String> getInfo() {
		final DecimalFormat dfV = new DecimalFormat("#,###,###,##0.0000");
		final String[] asT = new String[6];
		final int size = backtestDayLengths.size();
		final List<Integer> tempList = new ArrayList<Integer>(4);
		for (int i = 0; i < 4; i++) {
			if (i < size) {
				tempList.add(backtestDayLengths.get(i));
			} else {
				tempList.add(backtestDayLengths.get(size - 1));
			}
		}

		for (int i = 0; i < 4; i++) {
			// only display the first four
			final String windowIntString = Integer.toString(tempList.get(3 - i));
			asT[i] = selectionName + windowIntString + ":";
		}
		asT[4] = "Price:";
		asT[5] = "Date:";
		final int iLenT = asT.length;
		final ArrayList<String> alV = new ArrayList<String>(iLenT);
		String sV = "";
		// ^Binding
		for (int i = iLenT - 1; i >= 0; i--) {
			switch (i) {
			case 0:
				sV = String.valueOf(dfV.format(this.indexValue[3]));
				break;
			case 1:
				sV = String.valueOf(dfV.format(this.indexValue[2]));
				break;
			case 2:
				sV = String.valueOf(dfV.format(this.indexValue[1]));
				break;
			case 3:
				sV = String.valueOf(dfV.format(this.indexValue[0]));
				break;
			case 4:
				sV = String.valueOf(dfV.format(this.index0Value));
				break;
			case 5:
				sV = getHMS();
				break; // ^Customize display for timeseries(intraday) only
			}
			alV.add(asT[i] + "|" + sV);
		}
		return alV;
	}

	/*
	 * WZW override get Hour Minute Seconds
	 */
	private String getHMS() {
		final long lDte = (long) this.m_dXX;
		final Date dtXX = new Date(lDte);
		final String date = dateformat.get().format(dtXX);

		return date;
	}
}