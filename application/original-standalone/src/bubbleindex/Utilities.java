package bubbleindex;

import static info.yeppp.Core.Multiply_V64fV64f_V64f;
import static info.yeppp.Core.Subtract_V64fV64f_V64f;
import static info.yeppp.Math.Log_V64f_V64f;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.SwingUtilities;
import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.PrimitiveMatrix;

/**
 *
 * @author ttrott
 */
public class Utilities {
    
    public static int numberOfLines = 0;
    
    /**
     * 
     * @param displayText
     * @param resetTextArea 
     */
    public static void displayOutput(final String displayText, final boolean resetTextArea) {
        if (RunContext.isGUI) {
            numberOfLines++;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() { 
                    if (resetTextArea || numberOfLines > 200) {
                        GUI.OutputText.setText("");
                        numberOfLines = 0;
                    }
                    GUI.OutputText.append(displayText + "\n");
                }
            });
        }
        else {
            Logs.myLogger.info("{}", displayText);
        }
    }
    
    /**
     * DataReverse method takes an array and reverses the linear order of the
     * array.
     * 
     * @param Data The array to be reversed
     * @param SIZE The size of the array
     */   
    public static void DataReverse(final double[] Data, final int SIZE) {
        
        final double[] Temp = new double[SIZE];
        for (int i = 0; i < SIZE; i++) {
            Temp[i] = Data[SIZE - 1 - i];
        }
        
        System.arraycopy(Temp, 0, Data, 0, SIZE);      
    }  

    /**
     * Normalize takes the data, calculates the returns. Then normalizes the 
     * price data series to begin at a value of 100.0
     * 
     * @param SelectedData
     * @param NumberOfDays 
     */
    public static void Normalize(final double[] SelectedData, final int NumberOfDays) {
		
        final double[] ReturnsData = new double[NumberOfDays];
        ReturnsData[0] = 0.0;
        final double[] tempOne = new double[NumberOfDays];
        final double[] tempTwo = new double[NumberOfDays];
        final double[] tempThree = new double[NumberOfDays];
        final double[] tempFour = new double[NumberOfDays];
        final double[] tempFive = new double[NumberOfDays];
        
        for (int i = 1; i < NumberOfDays; i++) {
            tempOne[i] = SelectedData[i];
            tempTwo[i] = SelectedData[i-1];
            tempThree[i] = 1.0 / SelectedData[i-1];
        }
        
        Subtract_V64fV64f_V64f(tempOne, 0, tempTwo, 0, tempFour, 0, NumberOfDays);
        
        Multiply_V64fV64f_V64f(tempFour, 0, tempThree, 0, tempFive, 0, NumberOfDays);
        
        System.arraycopy(tempFive, 1, ReturnsData, 1, NumberOfDays - 1);

        //Declare and initialize array of normalized price values
        SelectedData[0] = 100.0;

        for (int i = 1; i < NumberOfDays; i++) {
            SelectedData[i] = SelectedData[i-1] * ReturnsData[i] + SelectedData[i-1];
        }

        Log_V64f_V64f(SelectedData, 0, tempFive, 0, NumberOfDays);
        
        System.arraycopy(tempFive, 0, SelectedData, 0, NumberOfDays);

    }

    /**
     * LinearFit method solves the b = Ax matrix for the x vector. In other
     * words this is a linear regression to fit the equation to the log prices.
     * The equation is y = b + Ax1 + Bx2 where x1 and x2 are TimeValues_M_power
     * and LogCosTimeValues respectively.
     * 
     * @param Data The array containing the log prices
     * @param TimeValues_M_Power Array of time values raised to the M power
     * @param LogCosTimeValues Array of logcos time values
     * @param Coef Array containing the models fitted coefficients
     * @param SIZE Size of the data window (days)
     */
    public static void LinearFit(final double[] Data, final double[] TimeValues_M_Power,
            final double[] LogCosTimeValues, final double[] Coef, final int SIZE){
//            
//    	final double[][] Array = new double[SIZE][3];
//            
//    	for (int i = 0; i < SIZE; i++) {
//    	    Array[i][0] = 1.0;
//    	    Array[i][1] = TimeValues_M_Power[i];
//    	    Array[i][2] = LogCosTimeValues[i];
//        }
    
        final BasicMatrix.Factory<PrimitiveMatrix> tmpFactory = PrimitiveMatrix.FACTORY;
        final BasicMatrix.Builder<PrimitiveMatrix> tmpBuilderA = tmpFactory.getBuilder(SIZE, 3);
        final BasicMatrix.Builder<PrimitiveMatrix> tmpBuilderB = tmpFactory.getBuilder(SIZE);
        
        for (int i = 0; i < SIZE; i++) {
            tmpBuilderA.set(i, 0, 1.0);
            tmpBuilderA.set(i, 1, TimeValues_M_Power[i]);
            tmpBuilderA.set(i, 2, LogCosTimeValues[i]);
            tmpBuilderB.set(i, Data[i]);
        }
        
        final BasicMatrix A = tmpBuilderA.build();
        final BasicMatrix B = tmpBuilderB.build();
        final BasicMatrix x = A.solve(B);
            
        for (int i = 0; i < 3; i++) {
    	    Coef[i] = x.get(i,0).doubleValue();
        }
    }     
    
    /**
     * ReadValues reads an external file containing two columns separated by
     * either a tab or comma, storing each column into its own string list.
     * 
     * @param CSVLocation
     * @param ColumnOne
     * @param ColumnTwo
     * @param firstLine
     * @param update
     * @throws FailedToRunIndex 
     */
    public static void ReadValues(final String CSVLocation, final List<String> ColumnOne, 
            final List<String> ColumnTwo, final boolean firstLine, final boolean update) throws FailedToRunIndex {
        
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;        
        Scanner s = null;        
        
        try {
            fileReader = new FileReader(CSVLocation);
            bufferedReader = new BufferedReader(fileReader);
            s = new Scanner(bufferedReader);
            String line;
            
            //check for header
            if (firstLine) {
                String waste = s.nextLine();
            }
            
            while(s.hasNext()) {                
                line = s.nextLine();
                Scanner lineScan = new Scanner(line);
                lineScan.useDelimiter(",|\t");
            /*
                    When updating... The Bubble Index files contain three
                    columns. The first column is not needed.
            */
                if (update) {
                    final String waste = lineScan.next();
                    ColumnOne.add(lineScan.next());
                    ColumnTwo.add(lineScan.next());
                }
                
                else {                
                    ColumnOne.add(lineScan.next());                
                    ColumnTwo.add(lineScan.next());
                }
           } 
         
        } catch (final IOException | NoSuchElementException ex) {
            Logs.myLogger.error("CSVLocation = {}. {}", CSVLocation, ex);
            
            throw new FailedToRunIndex(ex);
        }
        
        finally {
            if (s != null) {
                s.close();
            }
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (final IOException ex) {
                Logs.myLogger.error("CSVLocation = {}. {}", CSVLocation, ex);
                throw new FailedToRunIndex(ex);
            }
        }
    }
       
    /**
     * checkForFile checks to see if a file exists
     * 
     * @param filePathString
     * @return boolean true if file exists
     */
    public static boolean checkForFile(final String filePathString) {
        final File f = new File(filePathString);
        return f.exists();
    }
}

