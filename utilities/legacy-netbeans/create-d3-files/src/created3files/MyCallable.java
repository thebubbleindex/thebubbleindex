package created3files;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 *
 * @author green
 */
public class MyCallable implements Callable {

    private final String category;
    private final InputCategory inputCategory;
    private final String outputFolder;
    private final int maxLength;
    private final int[] windows;
            
    public MyCallable(String category, InputCategory inputCategory, int maxLength,
            String outputFolder, int[] windows) {
        
        this.category = category;
        this.inputCategory = inputCategory;
        this.maxLength = maxLength;
        this.outputFolder = outputFolder;
        this.windows = windows;
    }
    
    
    @Override
    public Boolean call() {
            
        try {
            System.out.println("Creating D3 files for category: " + category);
            inputCategory.createD3Files(outputFolder, maxLength, windows);
            return true;
        } catch (IOException ex) {
            System.out.println("" + ex);
            return false;
        }
    }
}
