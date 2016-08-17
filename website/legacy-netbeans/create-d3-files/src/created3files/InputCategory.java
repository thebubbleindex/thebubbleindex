package created3files;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.util.FastMath;

/**
 *
 * @author windows
 */
public class InputCategory {
    
    private String name;
    private String location;
    private ArrayList<String> components;
    private String folder;
    
    InputCategory(String name) {
        components = new ArrayList<>();

        this.name = name;

        this.folder = CreateD3Files.userDir + CreateD3Files.filePathSymbol + CreateD3Files.programDataFolder + 
                CreateD3Files.filePathSymbol + this.name + CreateD3Files.filePathSymbol;
        this.location =  CreateD3Files.userDir + CreateD3Files.filePathSymbol + CreateD3Files.programDataFolder + 
                CreateD3Files.filePathSymbol + this.name + ".csv";
    }
    
    InputCategory(String name, String location) {
        components = new ArrayList<>();
        this.name = name;
        this.location = location;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public void setComponents() {
        try {
            System.out.println("Location of Component: " + this.location);
            CSVReader reader = new CSVReader(new FileReader(this.location));
            List<String[]> myEntries = reader.readAll();
            for (String[] myEntry : myEntries) {
                components.add(myEntry[0]);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found Exception. Code 012." + ex);
        } catch (IOException ex) {
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

    void createD3Files(String outputFolder, int maxLength, int[] windows) throws IOException {
        for (String component : components) {
            Table<String, Integer, String> d3Table = TreeBasedTable.create();
            for (int window : windows) {
                try {
                    File file = new File(this.folder + component +
                            CreateD3Files.filePathSymbol + component + String.valueOf(window) + "days.csv");
                    if (file.exists()) {
                        CSVReader reader = new CSVReader(new FileReader(this.folder + component +
                                CreateD3Files.filePathSymbol + component + String.valueOf(window) + "days.csv"));
                        List<String[]> myEntries = reader.readAll();
                        myEntries.remove(0);//remove the header
                        final int sizeEntries = myEntries.size();

                        if (sizeEntries >= maxLength) {

                            for (int i = sizeEntries - maxLength; i < sizeEntries; i++) {
                                try {
                                    d3Table.put(myEntries.get(i)[2], window, myEntries.get(i)[1]);
                                } catch (ArrayIndexOutOfBoundsException ex) {
                                    System.out.println("Array index out of bounds." + ex);
                                }
                            }
                        }

                        else {
                            for (String[] fileEntry : myEntries) {
                                try{
                                    d3Table.put(fileEntry[2], window, fileEntry[1]);
                                } catch (ArrayIndexOutOfBoundsException ex) {
                                    System.out.println("Array index out of bounds." + ex);
                                }
                            }
                        }
                    }         
                } catch (FileNotFoundException ex) {
                    
                } catch (IOException ex) {
                    
                }
            }
            
            writeFileFromTable(d3Table, outputFolder, component, windows, CreateD3Files.Type);

        }
    }

    private void writeFileFromTable(Table<String, Integer, String> d3Table, 
            String outputFolder, String component, int[] windows, String fileType) throws IOException {

        File fileFolder = new File (outputFolder + this.name + CreateD3Files.filePathSymbol + component + CreateD3Files.filePathSymbol);
        
        File file = new File (outputFolder + this.name + CreateD3Files.filePathSymbol + component + CreateD3Files.filePathSymbol +
                component + fileType);
                
        if (file.exists()) {
            file.delete();
        }
        
        fileFolder.mkdirs(); // If the directory containing the file and/or its parent(s) does not exist
        file.createNewFile();

        switch (fileType) {
            case ".json":
                try(FileWriter writer = new FileWriter(file)){
                    
                    JSONWriter jsWriter = new JSONWriter(writer)
                            .array()
                            .object()
                            .key("Name")
                            .value(component)
                            .endObject()
                            .object()
                            .key("Dates")
                            .array();
                    
                    Iterator<String> rowIterator = d3Table.rowKeySet().iterator();
                    
                    while (rowIterator.hasNext()) {
                        String date = rowIterator.next();
                        jsWriter.value(date);
                    }
                    
                    jsWriter.endArray()
                            .endObject()
                            .object()
                            .key("Windows")
                            .array();
                    
                    for (int window : windows) {
                        
                        if (!d3Table.column(window).isEmpty()) {
                            
                            rowIterator = d3Table.rowKeySet().iterator();
                            jsWriter.object()
                                    .key(String.valueOf(window) + " Days")
                                    .array();
                            
                            while (rowIterator.hasNext()) {
                                String date = rowIterator.next();
                                int dataValue;
                                
                                try {
                                    dataValue = bubbleStandardValue(d3Table.get(date, window), window);
                                }
                                catch (NullPointerException e) {
                                    dataValue = 0;
                                }
                                jsWriter.value(dataValue);
                            }
                            
                            jsWriter.endArray()
                                    .endObject(); //window object
                        }
                        
                    }
      
                    jsWriter.endArray()
                            .endObject()
                            .endArray();
                    
                    
                    writer.close();
                    
                }
                
                
                
                /*    Iterator<String> rowIterator = d3Table.rowKeySet().iterator();
                
                while (rowIterator.hasNext()) {
                String date = rowIterator.next();
                //System.out.println("date: " + date);
                jsWriter.value(date);
                }
                
                jsWriter.endArray()
                .endObject();
                // .endArray(); //end date object array
                
                //jsWriter.object()
                //          .key("Windows")
                //        .array();
                
                
                /*for (int window : windows) {
                //  if (!d3Table.column(window).isEmpty()) {
                
                rowIterator = d3Table.rowKeySet().iterator();
                jsWriter.object()
                .key(String.valueOf(window) + " Days")
                .array();
                
                while (rowIterator.hasNext()) {
                String date = rowIterator.next();
                String dataValue;
                
                try {
                dataValue = bubbleStandardValue(d3Table.get(date, window), window);      
                }
                catch (NullPointerException e) {
                dataValue = "0";
                }
                jsWriter.value(dataValue);
                }
                
                jsWriter.endArray()
                .endObject(); //window object
                //  }
                
                }
                */
                // jsWriter.endArray()
                //       .endObject()
                // jsWriter.endArray();
                break;
            case ".tsv":
                try(FileWriter writer = new FileWriter(file)){                    
                    
                    Iterator<String> rowIterator = d3Table.rowKeySet().iterator();
                    
                    //write header
                    writer.append("date");
                    
                    for (int window : windows) {                                                
                        if (!d3Table.column(window).isEmpty()) 
                            writer.append("\t" + String.valueOf(window) + " Days");
                    }
                    
                    writer.append("\n");
                    
                    while (rowIterator.hasNext()) {                      
                                                      
                        String date = rowIterator.next();
                        writer.append(date);
                        
                        for (int window : windows) {
                        
                            if (!d3Table.column(window).isEmpty()) {
                                                        
                                int dataValue;
                                
                                try {
                                    dataValue = bubbleStandardValue(d3Table.get(date, window), window);
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
                break;
        }
    }
    
    private static int bubbleStandardValue(String value, int window) {
//        int roundedValue = (int) Math.round(Double.parseDouble(value) / (FastMath.pow(window, 3.353178) * 
//                FastMath.exp(-8.949354) * 2.0 + 550.0) * 100.0);
        final int roundedValue = (int) Math.round(Double.parseDouble(value) / (FastMath.exp(-9.746393 + 3.613444 * FastMath.log(window)) * 2.0 + 550.0) * 100.0);

        
        return roundedValue;
    }
    
}
