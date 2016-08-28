/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package createjson3dfiles;

import de.raida.cad.dex.CADExportContainer;
import de.raida.cad.dex.cadexport.json.JSONExporterImplementation;
import de.raida.cad.dex.cadimport.dxf.loader.DXFImporterImplementation;
import de.raida.cad.dex.interfaces.CADExportInterface;
import de.raida.cad.dex.interfaces.CADImportInterface;
import java.io.File;
import java.util.concurrent.Callable;

/**
 *
 * @author green
 */
public class MyCallable implements Callable {

    
    private final File file;
    private final CreateJSON3DFiles cassVar;
    public MyCallable(final File file, final CreateJSON3DFiles cassVar) {
        this.cassVar = cassVar;
        this.file = file;
    }
    
    @Override
    public Boolean call() {

        try {
            final String fileName = file.getName();
            final String filePath = file.getAbsolutePath();

            if (fileName.contains(".dxf")) {
                System.out.println("Path: " + file.getAbsolutePath());
                final CADImportInterface cadImportInterface = new DXFImporterImplementation();
                cadImportInterface.importFile(filePath, null);

                // Write the JSON file
                final String editFileName = filePath.replace(".dxf", "");//remove dxf extension from file name
                final String newFilePath = editFileName + ".json";

                final CADExportInterface cadExportInterface = new JSONExporterImplementation();
                cadExportInterface.exportFile(newFilePath, new CADExportContainer(cadImportInterface, newFilePath), null);

                CreateJSON3DFiles.editJSONFile(newFilePath, file.getParentFile().getName());
                //String begDateString = null, endDateString = null, begDateStringStd = null, endDateStringStd = null;
                //        int maxWindow = 0;
                //        double maxValue = 0.0;
                cassVar.writeJSONDailyDataDir(newFilePath, file.getParentFile().getName());

                cassVar.writeHTMLFile(newFilePath, file.getParentFile().getName());

            }
            
            return true;
        } catch (final Exception ex) {
            System.out.println("" + ex);
            return false;
        }
        
        
    }
    
}
