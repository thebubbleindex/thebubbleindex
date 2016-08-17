package createxyzfiles;

import com.opencsv.CSVReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.apache.commons.math3.util.FastMath;

/**
 *
 * @author windows
 */
public class InputCategory {
    
    private String name;
    private String location;
    private final ArrayList<String> components;
    private String folder;
    
    InputCategory(final String name) {
        components = new ArrayList<>();

        this.name = name;

        this.folder = CreateXYZFiles.userDir + CreateXYZFiles.filePathSymbol + CreateXYZFiles.programDataFolder + 
                CreateXYZFiles.filePathSymbol + this.name + CreateXYZFiles.filePathSymbol;
        this.location =  CreateXYZFiles.userDir + CreateXYZFiles.filePathSymbol + CreateXYZFiles.programDataFolder + 
                CreateXYZFiles.filePathSymbol + this.name + ".csv";
    }
    
    InputCategory(final String name, final String location) {
        components = new ArrayList<>();
        this.name = name;
        this.location = location;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setLocation(final String location) {
        this.location = location;
    }
    
    public void setComponents() {
        try {
            System.out.println("Location of Component: " + this.location);
            final CSVReader reader = new CSVReader(new FileReader(this.location));
            final List<String[]> myEntries = reader.readAll();
            for (final String[] myEntry : myEntries) {
                components.add(myEntry[0]);
            }
        } catch (final FileNotFoundException ex) {
            System.out.println("File Not Found Exception. Code 012." + ex);
        } catch (final IOException ex) {
           System.out.println("IOException Exception. Code 013." + ex);
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public ArrayList<String> getComponents() {
        return this.components;
    }
    
    public String[] getComponentsAsArray() {
        String[] stringArray = new String[this.components.size()];
        stringArray = this.components.toArray(stringArray);
        return stringArray;
    }

    public void createXYZfiles(final String outputFolder, final Integer[] windows) throws IOException {
        for (final String component : components) {
            System.out.println("Reading component: " + component);
            //final Table<String, Integer, Integer> allDataTable = HashBasedTable.create(42000, windows.length);
            final Map<String, int[]> allDataTable = new HashMap<>(42000);
            final List<Integer> windowsExist = new ArrayList<>(windows.length);
            int maximumValue = 0;
            int dailyDataSize = 5000;

                
            final File dailyData = new File(this.folder + component +
                    CreateXYZFiles.filePathSymbol + component + "dailydata.csv");

            if (dailyData.exists()) {
                final String dailyDataAsString = new String(Files.readAllBytes(dailyData.toPath()));
                dailyDataSize = dailyDataAsString.split(System.getProperty("line.separator")).length;
            }
            
            for (int i = 0; i < windows.length; i++) {
                

                
                
                final File file = new File(this.folder + component +
                        CreateXYZFiles.filePathSymbol + component + String.valueOf(windows[i]) + "days.csv");
                if (file.exists()) {
                    
                    windowsExist.add(windows[i]);
                    
                    final String fileAsString = new String(Files.readAllBytes(file.toPath()));
                    final String[] fileLines = fileAsString.split(System.getProperty("line.separator"));
                    //final List<String[]> myEntries = new ArrayList<>(2000);
                    for (int j = 1; j < fileLines.length; j++) {
                        final String[] line = fileLines[j].split(",");
                        try {
                            final int value = bubbleStandardValue(line[1], windows[i]);
                            if (value > maximumValue) maximumValue = value;
                            if (allDataTable.containsKey(line[2])) {
                                allDataTable.get(line[2])[i] = value;
                            }
                            else {
                                final int[] valueArray = new int[windows.length];
                                valueArray[i] = value;
                                allDataTable.put(line[2], valueArray);
                            }
                            //allDataTable.put(line[2], window, value);
                        } catch (final Exception ex) {
                            System.out.println("Exception. Component: " + component + ex);

                        }
                        //myEntries.add(line);
                    }
//                        final CSVReader reader = new CSVReader(new FileReader(this.folder + component +
//                                CreateXYZFiles.filePathSymbol + component + String.valueOf(window) + "days.csv"));
//                        final List<String[]> myEntries = reader.readAll();
                        //myEntries.remove(0);//remove the header

//                        for (final String[] fileEntry : myEntries) {
//                            try{
//                                int value = bubbleStandardValue(fileEntry[1], window);
//                                if (value > maximumValue) maximumValue = value;
//                                allDataTable.put(fileEntry[2], window, value);
//                            } catch (final ArrayIndexOutOfBoundsException ex) {
//                                System.out.println("Array index out of bounds." + ex);
//                            }
//                        }
                        //reader.close();                        
                    }

            }
            
            Integer[] actualWindows = new Integer[windowsExist.size()];
            
            actualWindows = windowsExist.toArray(actualWindows);
            
            writeXYZFile(allDataTable, actualWindows, outputFolder, component, maximumValue, dailyDataSize);
            createNetGridFile(allDataTable, actualWindows, outputFolder, component, maximumValue, dailyDataSize);
            //writeXYZFile(allDataTable, windows, outputFolder, component, maximumValue);
        }
    }

    
    private static int bubbleStandardValue(final String value, final Integer window) {
        //final int roundedValue = (int) Math.round(Double.parseDouble(value) / (FastMath.pow(window, 3.353178) * FastMath.exp(-8.949354) * 2.0 + 550.0) * 100.0);
        final int roundedValue = (int) Math.round(Double.parseDouble(value) / (FastMath.exp(-9.746393 + 3.613444 * FastMath.log(window)) * 2.0 + 550.0) * 100.0);
        return roundedValue;
    }

    //private void writeXYZFile(final Table<String, Integer, Integer> allDataTable, final Integer[] windows, final String outputFolder, final String component, final int max) throws IOException {
    
    private void writeXYZFile(final Map<String, int[]> allDataTable, final Integer[] windows,
            final String outputFolder, final String component, final int max, final int dailyDataSize) throws IOException {
 
        //final double scale = CreateXYZFiles.stdMaxValue / (double) max;
        final double scale = dailyDataSize * CreateXYZFiles.stdConstant / (double) max;
        //final Set<String> rowSet = allDataTable.rowKeySet();
        //final TreeSet<String> rowSet = new TreeSet(allDataTable.rowKeySet());
        final TreeSet<String> rowSet = new TreeSet(allDataTable.keySet());
        
//final List<String> rowsList = asSortedList(rowSet);
        final String[] rowsArray = rowSet.toArray(new String[rowSet.size()]);
        
        int index = 1;
        final List<byte[]> byteListOutputFile = new ArrayList<>(rowsArray.length);
        ///String outputFile = "";
        
        
        //byte[] b = string.getBytes(Charset.forName("UTF-8"));
        
        for (final String date : rowsArray) {                      

            final int[] valueArray = allDataTable.get(date);
            //final String date = row;

            for (int i = 0; i < windows.length; i++) {
                //if (allDataTable.containsColumn(window)) {
                //if (!allDataTable.column(window).isEmpty()) {
                
                int value;
                try {
                    value = valueArray[i];
                    //value = allDataTable.get(date, window);
                } catch (final Exception ex) {
                    value = -1;
                }
                if (value != -1) {

                    //if (allDataTable.contains(date, window)) {
                        try {
                            final String temp = (String.valueOf(index) + "," + String.valueOf(windows[i]) + "," + String.valueOf((int) (value * scale)) + "\n");

                            final byte[] b = temp.getBytes(Charset.forName("UTF-8"));  
                            byteListOutputFile.add(b);
                        }
                        catch (final Exception e) {
//                            final String temp = (String.valueOf(index) + "," + String.valueOf(windows[i]) + "," + "0" + "\n");
//                            final byte[] b = temp.getBytes(Charset.forName("UTF-8"));               
//                            byteListOutputFile.add(b);

                            System.out.print(component + " " + windows[i] + " " + e);
                        }
                   // }
                }
            }
            index++;
        }
        
        final File fileFolder = new File (outputFolder + this.name + CreateXYZFiles.filePathSymbol + component + CreateXYZFiles.filePathSymbol);
        
        final File file = new File (outputFolder + this.name + CreateXYZFiles.filePathSymbol + component + CreateXYZFiles.filePathSymbol +
                component + CreateXYZFiles.Type);
                
        if (file.exists()) {
            file.delete();
        }
        
        fileFolder.mkdirs(); // If the directory containing the file and/or its parent(s) does not exist
        //file.createNewFile();
        //String outputFile = "";
//        for (final byte[] line : byteListOutputFile) {
//            outputFile = outputFile + new String(line);
//        }
        
        try(final BufferedWriter fw = Files.newBufferedWriter(file.toPath(), Charset.forName("UTF-8"));){   

            for (final byte[] line : byteListOutputFile) {
                fw.write(new String(line));
            }
            //fw.write(outputFile);
            
        }                 
        

        final File maxValueFile = new File (outputFolder + this.name + CreateXYZFiles.filePathSymbol + component + CreateXYZFiles.filePathSymbol +
                "maxValue" + CreateXYZFiles.Type);
                
        if (maxValueFile.exists()) {
            maxValueFile.delete();
        }
        
        try(final BufferedWriter fw = Files.newBufferedWriter(maxValueFile.toPath(), Charset.forName("UTF-8"));){   

                fw.write(String.valueOf(max));
            
            //fw.write(outputFile);
            
        }    
        
//        try(final BufferedWriter fw = Files.newBufferedWriter(file.toPath(), Charset.forName("UTF-8"));){                    
//                    
//            //final Iterator<String> rowIterator = allDataTable.rowKeySet().iterator();
//            int index = 1;
//            for (final String date : rowsArray) {                      
//
//                //final String date = row;
//
//                for (final Integer window : windows) {
//                    if (allDataTable.containsColumn(window)) {
//                    //if (!allDataTable.column(window).isEmpty()) {
//                        if (allDataTable.contains(date, window)) {
//                            try {
//                                fw.append(String.valueOf(index) + "," + String.valueOf(window) + "," + String.valueOf((int) (allDataTable.get(date, window) * scale)) + "\n");
//                            }
//                            catch (final NullPointerException e) {
//                                fw.append(String.valueOf(index) + "," + String.valueOf(window) + "," + "0" + "\n");
//                                System.out.print(component + " " + window + " " + e);
//                            }
//                        }
//                    }
//                }
//                index++;
//            }                
//
//            fw.close();
//        }

    }
    
    public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
        List<T> list = new ArrayList<>(c);
        java.util.Collections.sort(list);
        return list;
    }

    private void createNetGridFile(final Map<String, int[]> allDataTable, final Integer[] windows,
            final String outputFolder, final String component, final int max, final int dailyDataSize) throws IOException {

        final int squareSide = (CreateXYZFiles.squareSize != 0) ? CreateXYZFiles.squareSize : 80;
        final String squareSideString = String.format("%.6f", (float) squareSide);
        final String doubleSquareSideString = String.format("%.6f", (float) squareSide*2);
        final int numberOfDates = dailyDataSize;
        int largestWindow = 0;
        for (final Integer window : windows) {
            if (window > largestWindow) {
                largestWindow = window;
            }
        }
        
        final int largest = (largestWindow > numberOfDates) ? largestWindow : numberOfDates;
        
        final int x_len = largest / squareSide;

        //final int y_len = largestWindow / squareSide + 1;

        final File fileFolder = new File (outputFolder + this.name + CreateXYZFiles.filePathSymbol + component + CreateXYZFiles.filePathSymbol);
        
        final File file = new File (outputFolder + this.name + CreateXYZFiles.filePathSymbol + component + CreateXYZFiles.filePathSymbol +
                "EqNet.net");
                
        if (file.exists()) {
            file.delete();
        }
        
        fileFolder.mkdirs(); // If the directory containing the file and/or its parent(s) does not exist

                
        try(final BufferedWriter fw = Files.newBufferedWriter(file.toPath(), Charset.forName("UTF-8"));){   

            fw.write("EqNet:01\r\n");
            fw.write("0\r\n");
            fw.write("0.00000\r\n");
            fw.write("0.000000 0.000000 " + String.format("%.6f", (float) largest) + " " + String.format("%.6f", (float) largest) + "\r\n");
            fw.write(String.valueOf(x_len + 1) + "\r\n");
            for (int i = 0; i < x_len; i++) {
                if (i == 0) 
                    fw.write(doubleSquareSideString + "\r\n");
                else 
                    fw.write(squareSideString + "\r\n");
            }
            
            fw.write(String.valueOf(x_len + 1) + "\r\n");
            for (int i = 0; i < x_len; i++) {
                if (i == 0) 
                    fw.write(doubleSquareSideString + "\r\n");
                else 
                    fw.write(squareSideString + "\r\n");
            }
            
//            for (final byte[] line : byteListOutputFile) {
//                fw.write(new String(line));
//            }

        
    }
    }
    
}
