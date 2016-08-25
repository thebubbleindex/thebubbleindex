package org.thebubbleindex.swing;

import com.nativelibs4java.util.IOUtils;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.thebubbleindex.driver.DailyDataCache;
import org.thebubbleindex.driver.noGUI.RunType;
import org.thebubbleindex.inputs.Indices;
import org.thebubbleindex.inputs.InputCategory;
import org.thebubbleindex.logging.Logs;
import org.thebubbleindex.runnable.RunContext;
import org.thebubbleindex.runnable.RunIndex;
import org.thebubbleindex.util.Utilities;

/**
 * GUI Creates, draws, and contains the input fields to run the GUI mode of The
 * Bubble Index application.
 * 
 * @author bigttrott
 */
public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -506515368957479177L;
	private double omega;
	private double mCoeff;
	private double tCrit;
	private String windowsInput;
	private String categoryName;
	private String selectionName;
	private boolean GRAPH_ON;
	private boolean isCustomRange;
	private Date begDate;
	private Date endDate;
	private String quandlKey;

	/**
	 * Creates new form GUI
	 */
	public GUI() {
		initComponents();
		final Properties guiProperties = new Properties();
		InputStream input = null;
		try {
			Logs.myLogger.info("Reading gui.properties.");
			input = new FileInputStream(Indices.userDir + Indices.filePathSymbol + "ProgramData"
					+ Indices.filePathSymbol + "gui.properties");
			guiProperties.load(input);
			customInit(guiProperties);

		} catch (final FileNotFoundException ex) {
			input = null;
			Logs.myLogger.error("Could not find gui.properties file. {}", ex);
		} catch (final IOException ex) {
			input = null;
			Logs.myLogger.error("Error while reading gui.properties file. {}", ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (final IOException ex) {
					Utilities.displayOutput("Error closing gui.properties file. " + ex.getLocalizedMessage(), false);
				}
			}
		}

		Utilities.displayOutput("Working Dir: " + Indices.userDir, false);
	}

	/**
	 * Creates new form GUI
	 * 
	 * @param omega
	 * @param mCoeff
	 * @param tCrit
	 */
	public GUI(final double omega, final double tCrit, final double mCoeff) {
		initComponents();
		final Properties guiProperties = new Properties();
		InputStream input = null;
		try {
			Logs.myLogger.info("Reading gui.properties.");
			input = new FileInputStream(Indices.userDir + Indices.filePathSymbol + "ProgramData"
					+ Indices.filePathSymbol + "gui.properties");
			guiProperties.load(input);
			customInit(guiProperties);

		} catch (final FileNotFoundException ex) {
			input = null;
			Logs.myLogger.error("Could not find gui.properties file. {}", ex);
		} catch (final IOException ex) {
			input = null;
			Logs.myLogger.error("Error while reading gui.properties file. {}", ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (final IOException ex) {
					Utilities.displayOutput("Error closing gui.properties file. " + ex.getLocalizedMessage(), false);
				}
			}
		}

		OmegaTextField.setText(String.valueOf(omega));
		TCriticalField.setText(String.valueOf(tCrit));
		MTextField.setText(String.valueOf(mCoeff));

		Utilities.displayOutput("Working Dir: " + Indices.userDir, false);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {

		quandlKeyTextField = new JTextField();
		quandlKeyLabel = new JLabel();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("The Bubble Index");

		TitleLabel.setFont(new Font("Serif", 1, 18)); // NOI18N
		TitleLabel.setText("The Bubble Index\u2122");
		TitleLabel.setToolTipText("");

		RunProgram.setText("Run");
		RunProgram.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				RunProgramActionPerformed(evt);
			}
		});

		OutputText.setRows(100);
		OutputText.setFont(new Font("Courier New", Font.PLAIN, 12));

		ExitButton.setText("Exit");
		ExitButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				ExitButtonActionPerformed(evt);
			}
		});

		DropDownCategory.setModel(new DefaultComboBoxModel<String>(Indices.getCategoriesAsArray()));
		DropDownCategory.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				DropDownCategoryActionPerformed(evt);
			}
		});

		DropDownSelection.setModel(new DefaultComboBoxModel<String>(Indices.categoriesAndComponents
				.get((String) DropDownCategory.getSelectedItem()).getComponentsAsArray()));

		CategoryLabel.setText("Type");

		SelectionLabel.setText("Name");

		SelectionAreaLabel.setFont(new Font("Ubuntu", 1, 15));
		SelectionAreaLabel.setText("Choose Index:");

		ModelOptionsHeaderLabel.setFont(new Font("Ubuntu", 1, 15));
		ModelOptionsHeaderLabel.setText("Model Options:");

		StopRunningButton.setText("Stop");
		StopRunningButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				StopRunningButtonActionPerformed(evt);
			}
		});

		GraphCheckBox.setText("Plot Graph");

		customBegDate.setValue(new Date(0));

		customEndDate.setValue(new Date());

		customDates.setText("Plot Custom Dates");

		GraphOptionsHeaderLabel.setFont(new Font("Ubuntu", 1, 15)); // NOI18N
		GraphOptionsHeaderLabel.setText("Graph Options:");

		updateDataButton.setText("Update Data");
		updateDataButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				updateDataButtonActionPerformed(evt);
			}
		});

		runAllNames.setText("Run All Names");
		runAllNames.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				runAllNamesActionPerformed(evt);
			}
		});

		runAllTypes.setText("Run All Types");
		runAllTypes.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				runAllTypesActionPerformed(evt);
			}
		});

		threadsTextLabel.setText("Threads");

		ThreadNumber.setText("4");

		windowsTextField.setText("52, 104, 153, 256, 512, 1260, 1764");

		WindowsLabel.setText("Windows:");

		OmegaTextLabel.setText("Omega");

		MTextLabel.setText("M");

		OmegaTextField.setText("6.28319");

		MTextField.setText("0.38");

		TCriticalLabel.setText("T_critical");

		TCriticalField.setText("21.0");

		forceCPUBox.setText("Force CPU");

		quandlKeyLabel.setText("Quandl API Key");

		final GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addComponent(OutputText, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGap(10, 10, 10))
						.addGroup(layout.createSequentialGroup()
								.addComponent(TitleLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGap(289, 289, 289))
						.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
								.addGap(0, 137, Short.MAX_VALUE).addComponent(updateDataButton).addGap(51, 51, 51)
								.addComponent(runAllTypes).addGap(53, 53, 53).addComponent(runAllNames)
								.addGap(36, 36, 36)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(RunProgram, GroupLayout.PREFERRED_SIZE, 94,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(StopRunningButton, GroupLayout.PREFERRED_SIZE, 94,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(ExitButton, GroupLayout.PREFERRED_SIZE, 94,
												GroupLayout.PREFERRED_SIZE))
								.addGap(22, 22, 22))
						.addGroup(GroupLayout.Alignment.TRAILING, layout
								.createSequentialGroup().addGroup(layout.createParallelGroup(
										GroupLayout.Alignment.TRAILING)
										.addGroup(layout
												.createSequentialGroup().addGroup(layout
														.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
																GroupLayout.Alignment.TRAILING, layout
																		.createSequentialGroup().addGroup(layout
																				.createParallelGroup(
																						GroupLayout.Alignment.LEADING)
																				.addComponent(customDates)
																				.addComponent(GraphCheckBox))
																		.addPreferredGap(
																				LayoutStyle.ComponentPlacement.UNRELATED)
																		.addGroup(layout
																				.createParallelGroup(
																						GroupLayout.Alignment.LEADING,
																						false)
																				.addComponent(customBegDate)
																				.addComponent(customEndDate,
																						GroupLayout.PREFERRED_SIZE, 98,
																						GroupLayout.PREFERRED_SIZE)))
														.addGroup(GroupLayout.Alignment.TRAILING,
																layout.createSequentialGroup().addComponent(
																		GraphOptionsHeaderLabel).addPreferredGap(
																				LayoutStyle.ComponentPlacement.RELATED,
																				168, GroupLayout.PREFERRED_SIZE)))
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(quandlKeyLabel)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(quandlKeyTextField, GroupLayout.PREFERRED_SIZE, 103,
														GroupLayout.PREFERRED_SIZE))
										.addGroup(GroupLayout.Alignment.LEADING,
												layout.createSequentialGroup().addComponent(WindowsLabel)
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(windowsTextField))
										.addGroup(layout.createSequentialGroup()
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(DropDownCategory, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(SelectionLabel)
														.addComponent(DropDownSelection, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(CategoryLabel)
														.addComponent(
																SelectionAreaLabel)
														.addComponent(ModelOptionsHeaderLabel))
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addGroup(GroupLayout.Alignment.TRAILING, layout
																.createSequentialGroup()
																.addGroup(layout
																		.createParallelGroup(
																				GroupLayout.Alignment.LEADING)
																		.addComponent(MTextLabel,
																				GroupLayout.Alignment.TRAILING)
																		.addComponent(OmegaTextLabel,
																				GroupLayout.Alignment.TRAILING)
																		.addComponent(threadsTextLabel,
																				GroupLayout.Alignment.TRAILING)
																		.addComponent(TCriticalLabel,
																				GroupLayout.Alignment.TRAILING))
																.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(layout
																		.createParallelGroup(
																				GroupLayout.Alignment.LEADING, false)
																		.addComponent(TCriticalField)
																		.addComponent(ThreadNumber)
																		.addComponent(MTextField)
																		.addComponent(OmegaTextField)))
														.addComponent(forceCPUBox, GroupLayout.Alignment.TRAILING))))
								.addGap(73, 73, 73)))));
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addComponent(TitleLabel, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
								.addGap(0, 0, 0).addComponent(SelectionAreaLabel)
								.addGap(19, 19,
										19)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addGroup(layout.createSequentialGroup()
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
														.addComponent(CategoryLabel)
														.addComponent(threadsTextLabel).addComponent(
																ThreadNumber, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
														.addGroup(layout.createSequentialGroup().addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.BASELINE)
																.addComponent(DropDownCategory,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE)
																.addComponent(TCriticalLabel).addComponent(
																		TCriticalField, GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE))
																.addGap(20, 20, 20).addComponent(SelectionLabel)
																.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(DropDownSelection,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE))
														.addGroup(layout.createSequentialGroup().addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.BASELINE)
																.addComponent(OmegaTextLabel).addComponent(
																		OmegaTextField, GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE))
																.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(layout
																		.createParallelGroup(
																				GroupLayout.Alignment.BASELINE)
																		.addComponent(MTextLabel).addComponent(
																				MTextField, GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))))
												.addGap(18, 18, 18).addComponent(ModelOptionsHeaderLabel))
										.addComponent(forceCPUBox))
								.addGap(18, 18, 18)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(windowsTextField, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(WindowsLabel))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(quandlKeyTextField, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(quandlKeyLabel))
										.addComponent(GraphOptionsHeaderLabel))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
														.addComponent(GraphCheckBox).addComponent(customBegDate,
																GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(customDates))
										.addComponent(customEndDate, GroupLayout.Alignment.TRAILING,
												GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(RunProgram, GroupLayout.Alignment.TRAILING))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(StopRunningButton)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(ExitButton).addComponent(runAllNames).addComponent(runAllTypes)
										.addComponent(updateDataButton))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(OutputText,
										GroupLayout.PREFERRED_SIZE, 197, GroupLayout.PREFERRED_SIZE)));

		pack();
	}

	/**
	 * RunProgramActionPerformed starts when the Run button is pressed. Creates
	 * a thread to run The Bubble Index for a single selection name which is
	 * parsed from the drop down menus.
	 * <p>
	 * If the Graph check box is selected, then the plots are called upon
	 * completion of the Run. The plots are designed to only plot the first four
	 * windows of the windowsInput text field.
	 * 
	 * @param evt
	 *            The click action event
	 */
	private void RunProgramActionPerformed(final ActionEvent evt) {
		initializeVariables();
		runnableGUI();

		final BubbleIndexWorker bubbleIndexWorker = new BubbleIndexWorker(RunType.Single, this, windowsInput, omega,
				mCoeff, tCrit, categoryName, selectionName, begDate, endDate, isCustomRange, GRAPH_ON);
		bubbleIndexWorker.execute();
	}

	/**
	 * initializeVariables gets values from GUI in preparation for any of the
	 * Runs.
	 * 
	 */
	public void initializeVariables() {
		DailyDataCache.reset();
		RunContext.threadNumber = Integer.parseInt(ThreadNumber.getText().trim());
		RunContext.Stop = false;
		GRAPH_ON = GraphCheckBox.isSelected();
		isCustomRange = customDates.isSelected();
		categoryName = (String) DropDownCategory.getSelectedItem();
		selectionName = (String) DropDownSelection.getSelectedItem();
		omega = Double.parseDouble(OmegaTextField.getText().trim());
		mCoeff = Double.parseDouble(MTextField.getText().trim());
		tCrit = Double.parseDouble(TCriticalField.getText().trim());
		windowsInput = windowsTextField.getText();
		try {
			quandlKey = quandlKeyTextField.getText();
			Logs.myLogger.info("Quandl Key provided: {}", quandlKey);
		} catch (final Exception ex) {
			quandlKey = "";
			Logs.myLogger.info("No Quandl Key provided.");
		}
		if (customDates.isSelected()) {
			begDate = (Date) customBegDate.getValue();
			endDate = (Date) customEndDate.getValue();
		} else {
			begDate = new Date(0);
			endDate = new Date();
		}
	}

	/**
	 * runnableGUI disable buttons during program operation
	 */
	public void runnableGUI() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				RunProgram.setEnabled(false);
				runAllNames.setEnabled(false);
				runAllTypes.setEnabled(false);
				StopRunningButton.setEnabled(true);
				updateDataButton.setEnabled(false);
			}
		});
	}

	/**
	 * resetGUI enable buttons after application finishes
	 */
	public void resetGUI() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				RunProgram.setEnabled(true);
				runAllNames.setEnabled(true);
				runAllTypes.setEnabled(true);
				StopRunningButton.setEnabled(false);
				customEndDate.setValue(new Date());
				updateDataButton.setEnabled(true);
			}
		});
	}

	/**
	 * ExitButtonActionPerformed Calls System.exit(0) upon clicking.
	 * 
	 * @param evt
	 *            The click action event
	 */
	private void ExitButtonActionPerformed(final ActionEvent evt) {
		Logs.myLogger.info("Exit button clicked");
		System.exit(0);
	}

	/**
	 * DropDownCategoryActionPerformed Updates the drop down boxes upon
	 * interaction click event.
	 * 
	 * @param evt
	 *            The click action event
	 */
	private void DropDownCategoryActionPerformed(final ActionEvent evt) {
		@SuppressWarnings("unchecked")
		final JComboBox<String> cb = (JComboBox<String>) evt.getSource();
		updateDropDownSelection((String) cb.getSelectedItem());
	}

	/**
	 * StopRunningButtonActionPerformed Changes the Stop variable in RunContext
	 * to true. This should initiate a complete halt of any executing process.
	 * 
	 * @param evt
	 *            The click action event
	 */
	private void StopRunningButtonActionPerformed(final ActionEvent evt) {
		Logs.myLogger.info("Stop button clicked");
		RunContext.Stop = true;
		DailyDataCache.reset();
	}

	/**
	 * runAllNamesActionPerformed Creates a single thread which loops through
	 * all Selection Names in a single category. This thread in turn forms a
	 * BubbleIndexThread instance which Runs a single Selection Name.
	 * 
	 * @param evt
	 *            The click action event.
	 */
	private void runAllNamesActionPerformed(final ActionEvent evt) {// GEN-FIRST:event_runAllNamesActionPerformed

		initializeVariables();
		runnableGUI();

		final BubbleIndexWorker bubbleIndexWorker = new BubbleIndexWorker(RunType.Category, this, windowsInput, omega,
				mCoeff, tCrit, categoryName, selectionName, begDate, endDate, isCustomRange, GRAPH_ON);
		bubbleIndexWorker.execute();
	}

	/**
	 * updateDataButtonActionPerformed Creates a thread which loops through all
	 * Categories specified in the UpdateCategories.csv external file. For every
	 * category it loops through all Selection Names obtained from the
	 * corresponding category's folder file: UpdateSelection.csv.
	 * <p>
	 * Based on this file, it fetches the appropriate URL containing the daily
	 * price data.
	 * 
	 * @param evt
	 *            The click action event
	 */
	private void updateDataButtonActionPerformed(final ActionEvent evt) {
		initializeVariables();
		runnableGUI();

		final UpdateWorker updateWorker = new UpdateWorker(this, quandlKey);

		updateWorker.execute();
	}

	/**
	 * runAllTypesActionPerformed Creates a thread which loops through all
	 * Categories and all Selection Names. This thread in turn forms a
	 * BubbleIndexThread instance which Runs a single Selection Name.
	 * 
	 * @param evt
	 *            The click action event
	 */
	private void runAllTypesActionPerformed(final ActionEvent evt) {
		initializeVariables();
		runnableGUI();

		final BubbleIndexWorker bubbleIndexWorker = new BubbleIndexWorker(RunType.All, this, windowsInput, omega,
				mCoeff, tCrit, categoryName, selectionName, begDate, endDate, isCustomRange, GRAPH_ON);
		bubbleIndexWorker.execute();

	}

	/**
	 * updateDropDownSelection Grabs the appropriate list to fill the drop down
	 * Selection Name box.
	 * 
	 * @param name
	 *            The name of the category
	 */
	protected void updateDropDownSelection(final String name) {

		for (final String category : Indices.categoriesAndComponents.keySet()) {
			if (name.equalsIgnoreCase(category)) {
				final InputCategory inputCategory = Indices.categoriesAndComponents.get(category);
				DropDownCategory.setSelectedItem(name);
				DropDownSelection.setModel(new DefaultComboBoxModel<String>(inputCategory.getComponentsAsArray()));
			}
		}
	}

	/**
	 * GUImain provides the main entrance to create and initialize the GUI
	 * containers.
	 * 
	 */
	public static void GUImain() {
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

		Logs.myLogger.info("Initializing GUI. Loading categories.");
		Indices.initialize();

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GUI().setVisible(true);

				try {
					Logs.myLogger.info("Reading OpenCL source file.");
					RunIndex.src = IOUtils.readText(RunIndex.class.getResource("/GPUKernel.cl"));
				} catch (final IOException ex) {
					Logs.myLogger.error("IOException Exception. Failed to read OpenCL source file. {}", ex);
					Utilities.displayOutput("Error. OpenCL file missing.", false);
				}

			}
		});
	}

	/**
	 * GUImain provides the main entrance to create and initialize the GUI
	 * containers.
	 * 
	 * @param omega
	 * @param tCrit
	 * @param mCoeff
	 */
	public static void GUImain(final double omega, final double tCrit, final double mCoeff) {
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
		Indices.initialize();

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GUI(omega, tCrit, mCoeff).setVisible(true);

				try {
					Logs.myLogger.info("Reading OpenCL source file.");
					RunIndex.src = IOUtils.readText(RunIndex.class.getResource("GPUKernel.cl"));
				} catch (final IOException ex) {
					Logs.myLogger.error("IOException Exception. Failed to read OpenCL source file. {}", ex);
					Utilities.displayOutput("Error. OpenCL file missing.", false);
				}

			}
		});
	}

	private final JLabel CategoryLabel = new JLabel();
	private final JComboBox<String> DropDownCategory = new JComboBox<String>();
	private final JComboBox<String> DropDownSelection = new JComboBox<String>();
	private final JButton ExitButton = new JButton();
	private final JCheckBox GraphCheckBox = new JCheckBox();
	private final JLabel GraphOptionsHeaderLabel = new JLabel();
	public final JTextField MTextField = new JTextField();
	private final JLabel MTextLabel = new JLabel();
	private final JLabel ModelOptionsHeaderLabel = new JLabel();
	private final JTextField OmegaTextField = new JTextField();
	private final JLabel OmegaTextLabel = new JLabel();
	public static final TextArea OutputText = new java.awt.TextArea();
	private final JButton RunProgram = new JButton();
	private final JLabel SelectionAreaLabel = new JLabel();
	private final JLabel SelectionLabel = new JLabel();
	private final JButton StopRunningButton = new JButton();
	public final JTextField TCriticalField = new JTextField();
	private final JLabel TCriticalLabel = new JLabel();
	public final JTextField ThreadNumber = new JTextField();
	private final JLabel TitleLabel = new JLabel();
	private final JLabel WindowsLabel = new JLabel();
	private final JFormattedTextField customBegDate = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
	private final JCheckBox customDates = new JCheckBox();
	private final JFormattedTextField customEndDate = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
	public final JCheckBox forceCPUBox = new JCheckBox();
	private JLabel quandlKeyLabel;
	private JTextField quandlKeyTextField;
	private final JButton runAllNames = new JButton();
	private final JButton runAllTypes = new JButton();
	private final JLabel threadsTextLabel = new JLabel();
	private final JButton updateDataButton = new JButton();
	public final JTextField windowsTextField = new JTextField();

	private void customInit(final Properties guiProperties) {

		OmegaTextField.setText(guiProperties.getProperty("omega").trim());
		TCriticalField.setText(guiProperties.getProperty("tcrit").trim());
		MTextField.setText(guiProperties.getProperty("mcoeff").trim());
		windowsTextField.setText(guiProperties.getProperty("windows").trim());
		ThreadNumber.setText(guiProperties.getProperty("threads"));
		customBegDate.setText(guiProperties.getProperty("begdate"));
		customEndDate.setText(guiProperties.getProperty("enddate"));

	}
}
