package created3files;

import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author green
 */
public class CreateD3Files {

    final static String programDataFolder = "ProgramData";
    final static String categoryList = "CategoryList.csv";
    static String userDir;
    public static String filePathSymbol;
    private static Map<String, InputCategory> categoriesAndComponents;
    final private static String outputFolder = "/home/green/Desktop/D3/";
    final private static int maxLength = 252;
    final private static int[] windows = {52, 104, 153, 256, 512, 1260, 1764, 2520, 5040, 10080};
    static String Type = ".tsv";
    final private static int threadNumber = 4;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException, InterruptedException, InterruptedException, ExecutionException {

        System.out.println("output Folder: " + outputFolder);
        System.out.println("maxLength: " + maxLength);
        System.out.println("windows: ");
        for (int windowPrint : windows) {
            System.out.println(windowPrint);
        }
        
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
        

   //     for (Map.Entry<String, InputCategory> entry : categoriesAndComponents.entrySet()) {
    // /       String category = entry.getKey();
     //       System.out.println("Creating D3 files for category: " + category);
      //      InputCategory tempCategory = entry.getValue();
      //      tempCategory.createD3Files(outputFolder, maxLength, windows);
      //  }
        
        ExecutorService executor = Executors.newFixedThreadPool(threadNumber);

        List<Callable<Boolean>> callables = new ArrayList<>();
        
        for (Map.Entry<String, InputCategory> entry : categoriesAndComponents.entrySet()) {
            callables.add(new MyCallable(entry.getKey(), entry.getValue(), maxLength,
                outputFolder, windows));
        }
        
        List<Future<Boolean>> futures = executor.invokeAll(callables);
        
        for (Future<Boolean> future : futures) {
            System.out.println("Results: " + (future.get() ? "Success" : "Failure"));
        }

        executor.shutdown();
    }
 
    
        /**
     * 
     */
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
