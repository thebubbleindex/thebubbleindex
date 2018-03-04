package bubbleindex;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author thebubbleindex
 */
public class UpdateData {

    private final List<String> Categories = new ArrayList();
    private final UpdateWorker updateWorker;
    
    public UpdateData(final UpdateWorker updateWorker, final String quandlKey) {
        this.updateWorker = updateWorker;
        Logs.myLogger.info("Running update.");
        getTypes();
        final Map<String, Integer> errorsPerCategory = new HashMap<>();
        int errors;

        for (final String Category : Categories) {
            errors = 0;
            final List<String> Selections = new ArrayList();
            final List<String> Sources = new ArrayList();
            final List<String> quandlDataSet = new ArrayList();
            final List<String> quandlDataName = new ArrayList();
            final List<Integer> quandlColumn = new ArrayList();
            final List<String> isYahooIndex = new ArrayList();
            getCategoryList(Category, Selections, Sources, quandlDataSet, quandlDataName, 
                    quandlColumn, isYahooIndex);
                    
            final ExecutorService executor = Executors.newFixedThreadPool(RunContext.threadNumber);
            final List<Callable<Integer>> callables = 
                    new ArrayList<>();
            
            for (int j = 0; j < Selections.size(); j++) {
                callables.add(new UpdateRunnable(this.updateWorker, Category, Selections.get(j), Sources.get(j),
                        quandlDataSet.get(j), quandlDataName.get(j), quandlColumn.get(j),
                        isYahooIndex.get(j), quandlKey));
            }
            
            final List<Future<Integer>> results;
            try {
                results = executor.invokeAll(callables);

                for (final Future<Integer> futures : results) {
                    try {
                        errors = errors + futures.get();
                        //updatelist.add(futures.get());
                    } catch (final ExecutionException ex) {
                        errors++;
                        Logs.myLogger.error("Category Name = {}. {}", Category, ex);
                    }
                }
                
            } catch (final InterruptedException ex) {
                Logs.myLogger.error("Category Name = {}. {}", Category, ex);
            }
            
            executor.shutdownNow();
            final int finalErrorNumber = errors;
            errorsPerCategory.put(Category, finalErrorNumber);
            //updatelist = new ArrayList();
        }
        checkForErrors(errorsPerCategory);       
        if (RunContext.isGUI) {
            updateWorker.publishText("Update Done!");
        } 
        else {
            System.out.println("Update Done!");
        }
    }
    
    /**
     * 
     */
    private void getTypes() {
        try {
            final Charset charset = Charset.forName("UTF-8");
            final Path filepath = FileSystems.getDefault().getPath(Indices.getFilePath() + 
                    "ProgramData" + Indices.filePathSymbol + "UpdateCategories.csv");
            Logs.myLogger.info("Filepath: {}", filepath);
            final BufferedReader reader = Files.newBufferedReader(filepath, charset);
            String line;
            while ((line = reader.readLine()) != null) {
                Categories.add(line);
            }
        
        } catch (final IOException ex) {
            Logs.myLogger.error("Failed to read UpdateCategories.csv... {}", ex);
            if (RunContext.isGUI) {
                updateWorker.publishText("Failed to Read File: UpdateCategories.csv");
            }   
            else {
                System.out.println("Failed to Read File: UpdateCategories.csv");
            }
        }
    }

    /**
     * 
     * @param Category
     * @param Selections
     * @param DataTypes
     * @param quandlDataSet
     * @param quandlDataName
     * @param quandlColumn
     * @param isYahooIndex 
     */
    private void getCategoryList(final String Category, final List<String> Selections, 
            final List<String> DataTypes, final List<String> quandlDataSet, final List<String> quandlDataName,
            final List<Integer> quandlColumn, final List<String> isYahooIndex) {
        try {
            final Charset charset = Charset.forName("UTF-8");
            final Path filepath = FileSystems.getDefault().getPath(Indices.getFilePath() + 
                    "ProgramData" + Indices.filePathSymbol + Category + Indices.filePathSymbol + 
                    "UpdateSelection.csv");
            Logs.myLogger.info("Filepath: {}", filepath);

            final BufferedReader reader = Files.newBufferedReader(filepath, charset);
            
            //These files have headers
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                final String[] splits = line.split(",");
                Selections.add(splits[0]);
                DataTypes.add(splits[1]);
                quandlDataSet.add(splits[2]);
                quandlDataName.add(splits[3]);
                quandlColumn.add(Integer.parseInt(splits[4]));
                isYahooIndex.add(splits[5]);
            }
        
        } catch (final IOException ex) {
            Logs.myLogger.error("Failed to get Name Selections from {}/UpdateSelection.csv... {}", Category, ex);
            final String cat = Category;
            if (RunContext.isGUI) {
                updateWorker.publishText("Failed to Get Selections From: " + 
                            cat + "/UpdateSelection.csv");               
        
            }
            else {
                System.out.println("Failed to Get selections from: " + cat + "/UpdateSelection.csv");
            }
        }
    }

    /**
     * 
     */
    private void checkForErrors(final Map<String, Integer> errorsPerCategory) {
        for (final Map.Entry<String, Integer> errors : errorsPerCategory.entrySet()) {
            final String categoryName = errors.getKey();
            final int errorNumber = errors.getValue();
            if (RunContext.isGUI) {
                updateWorker.publishText(categoryName + ": Errors or missing files = " + errorNumber);
            }  
            else {
                System.out.println(categoryName + ": Errors of missing files = " + errorNumber);
            }
        }
    }
}