package bubbleindex;

import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author thebubbleindex
 * 
 */
public class Indices {

    static String programDataFolder;
    static String categoryList;
    static String userDir;
    public static String filePathSymbol;
    public static Map<String, InputCategory> categoriesAndComponents;

    public static void initialize() {
        
        programDataFolder = "ProgramData";
        categoryList = "CategoryList.csv";
        filePathSymbol = setOperatingSystem();
        categoriesAndComponents = new TreeMap<>();
        
        Logs.myLogger.info("Initializing categories and selections.");
        
        try {
            userDir = getFilePath();
            Logs.myLogger.info("File path: {}", userDir);

        } catch (UnsupportedEncodingException ex) {
            Logs.myLogger.error("userDir = {}. {}", userDir, ex);
        }
        
        try {

            Logs.myLogger.info("Reading category list: {}", userDir + programDataFolder + filePathSymbol + categoryList);

            final CSVReader reader = new CSVReader(new FileReader(userDir + programDataFolder + filePathSymbol + categoryList));
            final List<String[]> myEntries = reader.readAll();

            for (final String[] myEntry : myEntries) {
                final String categoryName = myEntry[0];
                final InputCategory tempInputCategory = new InputCategory(categoryName);
                tempInputCategory.setComponents();
                categoriesAndComponents.put(categoryName, tempInputCategory);
            }
            
        } catch (final FileNotFoundException ex) {
            Logs.myLogger.error("File = {}. {}", userDir + programDataFolder + filePathSymbol + categoryList, ex);
        } catch (final IOException ex) {
            Logs.myLogger.error("File = {}. {}", userDir + programDataFolder + filePathSymbol + categoryList, ex);
        }
        
    }
    
    public static String[] getCategoriesAsArray() {
        final String[] categories = new String[categoriesAndComponents.size()];
        int index = 0;
        for (final String category : categoriesAndComponents.keySet()) {
            categories[index] = category;
            index++;
        }
        return categories;
    }

    /**
     * 
     * @return 
     */
    public static String setOperatingSystem() {
        if (OSValidator.isWindows()) {
            return "\\";
        }
        
        else if (OSValidator.isUnix()) {
            return "/";
        }
        
        else {
            return "/";
        }
    }
    
    /**
     * getFilePath returns the string containing the system address location of Bubble_Index.jar
     * @return decodedPath the address string without the "Bubble_Index.jar" attached
     * @throws java.io.UnsupportedEncodingException
     */
    public static String getFilePath() throws UnsupportedEncodingException {
        String path1, decodedPath, Name;
        if (OSValidator.isWindows()) {
            final String rawPath = URLS.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            final String newPath = rawPath.substring(1);
            path1 = newPath.replace("/", "\\");
            decodedPath = URLDecoder.decode(path1, "UTF-8");
            Name = "Bubble_Index.jar";
            final int Size = Name.length();
            decodedPath = decodedPath.substring(0, decodedPath.length() - Size);
        }
        else {
            path1 = URLS.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            decodedPath = URLDecoder.decode(path1, "UTF-8");
            Name = "Bubble_Index.jar";
            final int Size = Name.length();
            decodedPath = decodedPath.substring(0, decodedPath.length() - Size);
        }
        
        return decodedPath;
    }
}