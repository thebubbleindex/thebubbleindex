package compositefiles;

import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CompositeFiles {

    final static String programDataFolder = "ProgramData";
    final static String categoryList = "CompositeCategoryList.csv";
    static String userDir;
    public static String filePathSymbol;
    private static Map<String, InputCategory> categoriesAndComponents;
    final private static String outputFolder = "/home/green/Desktop/D3/";
    final private static int maxLength = 252;
    final private static int[] windows = {52, 104, 153, 256, 512, 1260, 1764};
    final private static double[] quantileValues = {0.5,0.8,0.9,0.95,0.99};

    static String fileType = ".tsv";
    final private static int threadNumber = 4;    
    
    public static void main(String[] args) {

        System.out.println("output Folder: " + outputFolder);
        System.out.println("maxLength: " + maxLength);
        System.out.println("Quantile Values: " + Arrays.toString(quantileValues));
       // System.out.println("Number of types: " + CompositeFiles.Types.length);
        System.out.println("windows: " + Arrays.toString(windows));

        setOperatingSystem();
        userDir = System.getProperty("user.dir");
        categoriesAndComponents = new TreeMap<>();
        
        try {
            System.out.println("Input Location: " + userDir + filePathSymbol + programDataFolder + filePathSymbol + categoryList);
            CSVReader reader = new CSVReader(new FileReader(userDir + filePathSymbol + programDataFolder + filePathSymbol + categoryList));
            List<String[]> myEntries = reader.readAll();

            for (String[] myEntry : myEntries) {
                String categoryName = myEntry[0];
                InputCategory tempInputCategory = new InputCategory(categoryName);
                tempInputCategory.setComponents();
                categoriesAndComponents.put(categoryName, tempInputCategory);
            }
            
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found Exception. Code 008." + ex);
        } catch (IOException ex) {
            System.out.println("IO Exception. Code 009." + ex);
        }
        

        for (Map.Entry<String, InputCategory> entry : categoriesAndComponents.entrySet()) {
                       
            System.out.println("Name of Type: " + entry.getKey());
            CompositeIndex compositeindex = new CompositeIndex(entry.getValue(), windows, quantileValues, (entry.getKey().equals("Stocks")) ? 100 : 30);
            compositeindex.run();
        }
        
        //read and input all categories and stock names
        //Indices.initialize();
        //Indices.GetIndices();
        
       // double[] quantileValues = {0.5,0.8,0.9,0.95,0.99};
        //int[] dayWindows = {52,104,153,256,512,1260,1764};

      //  for (String Type : Indices.Types) {
      //      System.out.println("Name of Type: " + Type);
       //     CompositeIndex compositeindex = new CompositeIndex(Type, windows, quantileValues, (Type.equals("Stocks")) ? 100 : 30);
       //     compositeindex.run();
       // }
    
    }
    
    
    public static void setOperatingSystem() {
        if (OSValidator.isWindows()) {
            filePathSymbol = "\\";
        }
        
        else if (OSValidator.isUnix()) {
            filePathSymbol = "/";
        }
        
        else {
            filePathSymbol = "/";
        }
    }
    
}
