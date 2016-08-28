/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package createxyzfiles;

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
public class CreateXYZFiles {
    
    final static String programDataFolder = "ProgramData";
    final static String categoryList = "CategoryList.csv";
    static String userDir;
    public static String filePathSymbol;
    private static Map<String, InputCategory> categoriesAndComponents;
    final private static String outputFolder = "/home/green/Desktop/XYZConversion/";
    final private static Integer[] windows = {52,100,104,125,153,175,200,225,250,256,275,300,325,350,375,400,425,450,475,500,
        512,525,550,575,600,625,650,675,700,725,750,775,800,825,850,875,900,925,950,975,1000,
        1050,1100,1150,1200,1260,1500,1700,1764,2000,2200,2520,2700,3000,3200,3500,3700,4000,4200,4500,4700,5040,5200,5500,5700,
        6000,6200,6500,6700,7000,7200,7500,7700,8000,8100,8200,8300,8400,8500,8600,8700,9000,9200,9500,9700,10080,10200,10500,10700,
        11000,11200,11500,11700,12000,12200,12500,12700,13000,13200,13500,13700,14000,14200,14500,14700,15000,15200,15500,15700,
        16000,16200,16500,16700,17000,17200,17500,17700,18000,18200,18500,18700,19000,19200,19500,19700,20000,20160,20500,20700,
        21000,21200,21500,21700,22000,22200,22500,22700,23000,23200,23500,23700,24000,24200,24500,24700,25000,25200,25500,25700,
        26000,26200,26500,26700,27000,27200,27500,27700,28000,28200,28500,28700,29000,29200,29500,29700,30000,30200,30500,30700,
        31000,31200,31500,31700,32000,32200,32500,32700,33000,33200,33500,33700,34000,34200,34500,34700,35000,35200,35500};
    //final private static Integer[] windows = {52,104,153,256,512,1260,1764,5040,2520,10080,200,300,350,400,450,550,600,650,700,750,800,850,900,950,1000,1050,1100,1150,1200,1300,1400,1500,1600,1700,1800,1900,2000,2100,2200,2300,2400,2600,2700,2800,2900,3000,3140,3200,3300,3500,3700,4000,4200,4500,4700,5200,5300,5500,5700,6000,6200,6500,6700,7000,7200,7500,7600,7700,7800,7900,8000,8100,8150,8200,8250,8300,8350,8400,8450,8500,8550,8600,8650,8700,8750,8850,9000,9200,9500,9700,10000,10200,10300,10500,10700,11000,11200,11500,11700,12000,12200,12300,12500,12700,13000,13200,13500,13700,14000,14200,14500,14700,15000,15200,15500,15700,16000,16200,16500,16700,17000,17200,17500,17700,18000,18200,18500,18700,19000,19200,19500,19700,20000,20160,20500,20700,21000,21200,21500,21700,22000,22200,22500,22700,23000,23200,23500,23700,24000,24200,24500,24700,25000,25200,25500,25700,26000,26200,26500,26700,27000,27200,27500,27700,28000,28200,28500,28700,29000,29200,29500,29700,30000,30200,30500,30700,31000,31200,31500,31700,32000,32200,32500,32700,33000,33200,33500,33700,34000,34200,34500,34700,35000,35200,35500};
        
    //public static Integer[] windows;
    static String Type = ".csv";
    final private static int threadNumber = Runtime.getRuntime().availableProcessors();
    final private static int arraySize = 50000;
    final public static double stdMaxValue = 10000.0;
    final public static double stdConstant = 1.0;
    public static int squareSize = 0;
    public static void main(String[] args) throws InterruptedException, ExecutionException {
 
        squareSize = (args.length > 0) ? Integer.parseInt(args[0]) : 0;
        
        System.out.println("output Folder: " + outputFolder);
        System.out.println("windows: ");
        
        //windows = new Integer[arraySize];
        
        //for (int i = 0; i < arraySize; i++) {
        //    windows[i] = i + 52;
        //}
        
//        for (int windowPrint : windows) {
//            System.out.println(windowPrint);
//        }
        
        setOperatingSystem();
        userDir = System.getProperty("user.dir");
        categoriesAndComponents = new TreeMap<>();
        
        try {
            System.out.println("Input Location: " + userDir + filePathSymbol + programDataFolder + filePathSymbol + categoryList);
            CSVReader reader = new CSVReader(new FileReader(userDir + filePathSymbol + programDataFolder + filePathSymbol + categoryList));
            final List<String[]> myEntries = reader.readAll();

            for (final String[] myEntry : myEntries) {
                final String categoryName = myEntry[0];
                final InputCategory tempInputCategory = new InputCategory(categoryName);
                tempInputCategory.setComponents();
                categoriesAndComponents.put(categoryName, tempInputCategory);
            }
            
        } catch (final FileNotFoundException ex) {
            System.out.println("File Not Found Exception. Code 008." + ex);
        } catch (final IOException ex) {
            System.out.println("IO Exception. Code 009." + ex);
        }
        

   //     for (Map.Entry<String, InputCategory> entry : categoriesAndComponents.entrySet()) {
    // /       String category = entry.getKey();
     //       System.out.println("Creating D3 files for category: " + category);
      //      InputCategory tempCategory = entry.getValue();
      //      tempCategory.createD3Files(outputFolder, maxLength, windows);
      //  }
        
        final ExecutorService executor = Executors.newFixedThreadPool(threadNumber);

        final List<Callable<Boolean>> callables = new ArrayList<>();
        
        for (final Map.Entry<String, InputCategory> entry : categoriesAndComponents.entrySet()) {
            callables.add(new MyCallable(entry.getKey(), entry.getValue(),
                outputFolder, windows));
        }
        
        final List<Future<Boolean>> futures = executor.invokeAll(callables);
        
        for (final Future<Boolean> future : futures) {
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
