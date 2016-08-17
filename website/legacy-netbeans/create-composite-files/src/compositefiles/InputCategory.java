package compositefiles;

import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        this.folder = CompositeFiles.userDir + CompositeFiles.filePathSymbol + CompositeFiles.programDataFolder + 
                CompositeFiles.filePathSymbol + this.name + CompositeFiles.filePathSymbol;
        this.location =  CompositeFiles.userDir + CompositeFiles.filePathSymbol + CompositeFiles.programDataFolder + 
                CompositeFiles.filePathSymbol + this.name + ".csv";
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
        
}
