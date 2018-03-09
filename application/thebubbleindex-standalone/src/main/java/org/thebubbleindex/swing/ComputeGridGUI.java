package org.thebubbleindex.swing;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.UIManager;

import org.thebubbleindex.computegrid.BubbleIndexComputeGrid;
import org.thebubbleindex.driver.DailyDataCache;
import org.thebubbleindex.driver.noGUI.RunType;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.util.Utilities;

/**
 * GUI Creates, draws, and contains the input fields to run the GUI mode of The
 * Bubble Index application.
 * 
 * @author thebubbleindex
 */
public class ComputeGridGUI extends GUI {
	/**
	 * 
	 */
	private static final long serialVersionUID = 710192774177947360L;
	private BubbleIndexComputeGrid bubbleIndexComputeGrid;

	public ComputeGridGUI(final RunContext runContext, final BubbleIndexComputeGrid bubbleIndexComputeGrid) {
		super(runContext);
		this.bubbleIndexComputeGrid = bubbleIndexComputeGrid;
		Utilities.displayOutput(runContext, "Bubble Index Compute Grid: " + bubbleIndexComputeGrid.about(), false);
	}

	@Override
	protected void RunProgramActionPerformed(final ActionEvent evt) {
		final DailyDataCache dailyDataCache = new DailyDataCache();
		initializeVariables(dailyDataCache);
		runnableGUI();

		final BubbleIndexGridWorker bubbleIndexGridWorker = new BubbleIndexGridWorker(RunType.Single, this,
				windowsInput, omega, mCoeff, tCrit, categoryName, selectionName, begDate, endDate, isCustomRange,
				GRAPH_ON, dailyDataCache, indices, openCLSrc, runContext, bubbleIndexComputeGrid);
		bubbleIndexGridWorker.execute();
	}

	@Override
	protected void runAllNamesActionPerformed(final ActionEvent evt) {
		final DailyDataCache dailyDataCache = new DailyDataCache();

		initializeVariables(dailyDataCache);
		runnableGUI();

		final BubbleIndexGridWorker bubbleIndexGridWorker = new BubbleIndexGridWorker(RunType.Category, this,
				windowsInput, omega, mCoeff, tCrit, categoryName, selectionName, begDate, endDate, isCustomRange,
				GRAPH_ON, dailyDataCache, indices, openCLSrc, runContext, bubbleIndexComputeGrid);
		bubbleIndexGridWorker.execute();
	}

	@Override
	protected void runAllTypesActionPerformed(final ActionEvent evt) {
		final DailyDataCache dailyDataCache = new DailyDataCache();

		initializeVariables(dailyDataCache);
		runnableGUI();

		final BubbleIndexGridWorker bubbleIndexGridWorker = new BubbleIndexGridWorker(RunType.All, this, windowsInput,
				omega, mCoeff, tCrit, categoryName, selectionName, begDate, endDate, isCustomRange, GRAPH_ON,
				dailyDataCache, indices, openCLSrc, runContext, bubbleIndexComputeGrid);
		bubbleIndexGridWorker.execute();
	}
	
	/**
	 * ExitButtonActionPerformed Calls System.exit(0) upon clicking.
	 * 
	 * @param evt
	 *            The click action event
	 */
	@Override
	protected void ExitButtonActionPerformed(final ActionEvent evt) {
		Logs.myLogger.info("Exit button clicked");
		bubbleIndexComputeGrid.triggerStopAllTasksMessage();
		bubbleIndexComputeGrid.shutdownGrid();
		System.exit(0);
	}
	
	/**
	 * StopRunningButtonActionPerformed Changes the Stop variable in RunContext
	 * to true. This should initiate a complete halt of any executing process.
	 * 
	 * @param evt
	 *            The click action event
	 */
	@Override
	protected void StopRunningButtonActionPerformed(final ActionEvent evt) {
		Logs.myLogger.info("Stop button clicked");
		bubbleIndexComputeGrid.triggerStopAllTasksMessage();
	}

	/**
	 * GUImain provides the main entrance to create and initialize the GUI
	 * containers.
	 * 
	 * @param omega
	 * @param tCrit
	 * @param mCoeff
	 */
	public static void ComputeGridGUImain(final RunContext runContext, final BubbleIndexComputeGrid bubbleIndexComputeGrid) {
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting
		// code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.
		 * html
		 */
		/* Create and display the form */
		try {
			for (final UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (final Exception ex) {
		}

		Logs.myLogger.info("Initializing GUI. Loading categories");

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ComputeGridGUI(runContext, bubbleIndexComputeGrid).setVisible(true);
			}
		});
	}
}
