package compositefiles;

import com.google.common.collect.Table;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import org.apache.commons.math3.util.FastMath;

/**
 *
 * @author thebubbleindex
 */
public class InputData {
    
    /**
     * ReadValues reads an external file containing two columns separated by
     * either a tab or comma, storing each column into its own string list
     * @param CSVLocation the string with the address of the file containing the
     *         data with two columns
     * @param DateList string list containing the first column
     * @param DataList string list containing the second column
     * @param firstLine indicates whether the file contains a header
     * @param update indicates whether the file is a previously created Bubble Index
     *        If true then the file being read has name = 'DJIA''1764'days.csv
     * @return void
    */
    public static void ReadValues(String CSVLocation, List<String> ColumnOne, 
            List<String> ColumnTwo, boolean firstLine, boolean update) {
        
        Scanner s = null;        
        
        try {
            s = new Scanner(new BufferedReader(new FileReader(CSVLocation)));
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
                    String waste = lineScan.next();
                    ColumnOne.add(lineScan.next());
                    ColumnTwo.add(lineScan.next());
                }
                
                else {                
                    ColumnOne.add(lineScan.next());                
                    ColumnTwo.add(lineScan.next());
                }
           } 
         
        } catch (IOException | NoSuchElementException ex) {
        }
        
        finally {
            if (s != null) {
                s.close();
            }
        }
    }
    
    /**
     * ConvertPrices converts the string values in DailyPriceDataList to type double and 
     * stores them as double into PriceDataArray
     * @param PriceDataArray the array storing the double values
     * @param DAILY_DATA_SIZE the size of both the DataList and PriceDataArray
     * @return void
     
    public static void ConvertPrices(double[] PriceDataArray, final int DAILY_DATA_SIZE) {
        for (int i = 0; i < DAILY_DATA_SIZE; i++) {
            try {                
                PriceDataArray[i] = Double.parseDouble(BubbleIndex.DailyPriceDataList.get(i));
            } catch (NumberFormatException ex) {
            }
        }
    }

    /**
     *  GetNumberOfIndices returns the number of Bubble Indices
     *  to create based on the values inputted into Java Swing GUI
     *  @return i the number of indices to create
    
    public static int GetNumberOfIndices() {
        int i = 7;
        return i;
    }
    
    /**
     * GetDayLengths sets the size of the window to be used when 
     * calculating The Bubble Index (#### days); The values inputted 
     * into the GUI must be in filled from top to bottom 
     * @param DayLengths array which contains all the lengths to be used 
     * @param NUMBER the number of Bubble Indices to be created... should 
     * match the size of the DayLengths array 
     * @return void
     
    public static void GetDayLengths(int[] DayLengths, final int NUMBER) {
        DayLengths[0] = BubbleIndex.WindowDataSize1;
        DayLengths[1] = BubbleIndex.WindowDataSize2;
        if (NUMBER > 2) {
            DayLengths[2] = BubbleIndex.WindowDataSize3;
        }
        if (NUMBER > 3) {
            DayLengths[3] = BubbleIndex.WindowDataSize4;
        }
        
        if (NUMBER > 4) {
            DayLengths[4] = BubbleIndex.WindowDataSize5;
        }        

        if (NUMBER > 5) {
            DayLengths[5] = BubbleIndex.WindowDataSize6;
        }        
        
        if (NUMBER > 6) {
            DayLengths[6] = BubbleIndex.WindowDataSize7;
        }        
    }
    
    /**
     * checkForFile checks to see if a file exists
     * @param the address of the file
     * @return boolean true if file exists
     */
    public static boolean checkForFile(String filePathString) {
        File f = new File(filePathString);
        if (f.exists()) {
            return true;
        }
        else {
            return false;
        }
    }

    public static void writetoFile(Map<String, Double> quantileIndex, String savePath) throws IOException {

        FileWriter writer;
        Path filepath = FileSystems.getDefault().getPath(savePath);
        System.out.println("Write To: " + filepath);
        if (Files.exists(filepath)) {
            Files.delete(filepath);
        }
        
        try {
            writer = new FileWriter(savePath);
              
            for (Map.Entry<String, Double> entry : quantileIndex.entrySet()) {
                String key = entry.getKey();
                double temp = entry.getValue();
                writer.append(key);               
                writer.append(",");
                writer.append(String.valueOf(temp));
                writer.append("\n");
            }            

            writer.flush();
            writer.close();
        
        } catch (IOException ex) {
            System.out.println("File Writer failed");
        }              
    }

    static void createD3File(Table<String, Integer, Double> d3Table, String d3savePath) throws IOException {
        
        int windows[] = new int[d3Table.columnKeySet().size()];
        
        int tableSize = d3Table.rowKeySet().size();
                
        System.out.println("Table size: " + tableSize);
        
        List<String> rows = new ArrayList<>();
        
        if (tableSize > 252) {
            
            Iterator<String> rowIterator = d3Table.rowKeySet().iterator();
                        
            int itIndex = 0;

            while (rowIterator.hasNext()) {
                                
                String row = rowIterator.next();
                
                if (itIndex >= tableSize - 252) {
                    rows.add(row);
                }
                
                itIndex = itIndex + 1;
            }
            
            System.out.println("Table larger than 252. Added " + rows.size() + " rows");

        }
        
        
        else {
        
            Iterator<String> rowIterator = d3Table.rowKeySet().iterator();
            
            while (rowIterator.hasNext()) {
                rows.add(rowIterator.next());
            }
            System.out.println("Table smaller than 252. Added " + rows.size() + " rows");

        }
        
        int i = 0;
        for (Integer col : d3Table.columnKeySet()) {
            windows[i++] = col;
        }
        
        File file = new File (d3savePath);
                
        if (file.exists()) {
            file.delete();
        }
        file.getParentFile().mkdirs();
        //file.mkdirs(); // If the directory containing the file and/or its parent(s) does not exist
        file.createNewFile();
        
        try(FileWriter writer = new FileWriter(file)){                    
                                        
            //write header
            writer.append("date");

            for (int window : windows) {                                                
                if (!d3Table.column(window).isEmpty()) 
                    writer.append("\t" + String.valueOf(window) + " Days");
            }

            writer.append("\n");

            for (String row : rows) {
                
                writer.append(row);

                for (int window : windows) {

                    if (!d3Table.column(window).isEmpty()) {

                        int dataValue;

                        try {
                            dataValue = bubbleStandardValue(d3Table.get(row, window), window);
                        }
                        catch (NullPointerException e) {
                            dataValue = 0;
                        }

                        writer.append("\t" + String.valueOf(dataValue));
                    }
                }

                writer.append("\n");
            }                

            writer.close();
                
        }
    }
        
    private static int bubbleStandardValue(Double value, int window) {
//        int roundedValue = (int) Math.round(value / (FastMath.pow(window, 3.353178) * 
//                FastMath.exp(-8.949354) * 2.0 + 550.0) * 100.0);

                final int roundedValue = (int) Math.round(value / (FastMath.exp(-9.746393 + 3.613444 * FastMath.log(window)) * 2.0 + 550.0) * 100.0);

        return roundedValue;
    }
   // 20160dayValue<-20160^3.353178*exp(-8.949354)*2+550

}