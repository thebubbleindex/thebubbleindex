package bubbleindex;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author thebubbleindex
 */
public class URLS {
    private String dataName;
    private String url;
    private String dataType;
    private String source;
    private Path path;
    private final int startYear = 1900;
    private Date today;
    private final DateFormat df = new SimpleDateFormat("yyyy");
    private boolean INDEX = false;
    private String yahooSymbol;
    private String QuandlDataset;
    private String QuandlName;
    private int QuandlColumn;
    private boolean specialSymbol = false;
    private UpdateWorker updateWorker;
    /**
     * 
     */
    public void setToday() {
        this.today = new Date();
    }
    
    public void setUpdateWorker(final UpdateWorker updateWorker) {
        this.updateWorker = updateWorker; 
    }
    
    /**
     * 
     * @param dataName 
     */
    public void setDataName (final String dataName) {
        this.dataName = dataName;
        final String name = this.dataName;
        if (RunContext.isGUI) {
            updateWorker.publishText("GET: " + name);
        }
        else {
            System.out.println("GET: " + name);
        }
    }
    
    /**
     * 
     */
    public void setYahooUrl() {
        if (this.INDEX) {
            this.yahooSymbol = "%5E";
        }
        else {
            this.yahooSymbol = "";
        }
        
        this.url = "http://ichart.yahoo.com/table.csv?s=" + this.yahooSymbol + this.dataName 
                + "&a=0&b=1&c=" + this.startYear + "&d=11&e=31&f=" 
                + getYear() + "&g=d&ignore=.csv";
    }    
    
    /**
     * 
     */
    public void setFEDUrl() {
        this.url = "https://research.stlouisfed.org/fred2/series/" + this.dataName 
                + "/downloaddata/" + this.dataName + ".csv";
    }
    
    /**
     * 
     * @param dataset
     * @param name 
     * @param quandlKey 
     */
    public void setQuandlUrl(final String dataset, final String name, final String quandlKey) {
        this.QuandlDataset = dataset;
        this.QuandlName = name;
        this.url = "https://www.quandl.com/api/v1/datasets/" + this.QuandlDataset +
                "/" + this.QuandlName + ".csv?sort_order=asc";
        if (quandlKey.trim().length() > 0) {
            this.url = this.url + "&api_key=" + quandlKey.trim();
        }
    }
    
    /**
     * 
     * @param Column 
     */
    public void setQuandlColumn(final int Column) {
        this.QuandlColumn = Column;
    }
    
    /**
     * 
     */
    public void isSpecial() {
        this.specialSymbol = true;
    }
    
    /**
     * 
     * @param dataType 
     */
    public void setDataType(final String dataType) {
        this.INDEX = this.specialSymbol;
        this.dataType = dataType;
    }

    /**
     * 
     * @param source 
     */
    public void setSource(final String source) {
        this.source = source;
    }
    
    /**
     * 
     * @throws UnsupportedEncodingException 
     */
    public void setPath () throws UnsupportedEncodingException {
        this.path = FileSystems.getDefault().getPath(Indices.getFilePath() + this.dataName + "raw.csv");
        final Path pathstring = this.path;
        if (RunContext.isGUI) {
            updateWorker.publishText("Path: " + pathstring + " ...");
        }
        else {
            System.out.println("Path: " + pathstring + " ...");
        }
    }
    
    /**
     * 
     * @return 
     */
    public String getYear() {
        setToday();
        final String s = df.format(this.today);
        return s;
    }
    
    /**
     * 
     * @return 
     */
    public String getDataName () {
        return this.dataName;
    }
    
    /**
     * 
     * @return 
     */
    public String getUrl () {
        return this.url;
    }   
    
    /**
     * 
     * @return 
     */
    public Path getPath () {
        return this.path;
    }
    
    /**
     * 
     * @return 
     */
    public String getDataType () {
        return this.dataType;
    }
    
    /**
     * 
     * @return 
     */
    public String getSource () {
        return this.source;
    }
    
    /**
     * 
     * @param outputstream
     * @throws java.net.MalformedURLException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void readURL_file(final ByteArrayOutputStream outputstream) throws MalformedURLException, IOException {
        final URL url1 = new URL(url);
        final byte[] ba1 = new byte[1024];
        int baLength;
        //FileOutputStream fos1 = new FileOutputStream(Indices.getFilePath() + this.dataName + "raw.csv");
            //Create connection
            if (RunContext.isGUI) {
                updateWorker.publishText("Connecting to " + url1.toString() + " ...");
            }   
            else {
                System.out.println("Connection to " + url1.toString() + " ...");
            }
          
            final URLConnection urlConn = url1.openConnection();

            if (RunContext.isGUI) {
                updateWorker.publishText("Downloading the CSV: " + this.dataName + "...");
            }   
            else {
                System.out.println("Downloading the CSV: " + this.dataName + "...");
            }

            try ( // Read the CSV from the URL and save to a local file
                    final InputStream is1 = url1.openStream()) {
                while ((baLength = is1.read(ba1)) != -1) {
                    outputstream.write(ba1, 0, baLength);
                }
            }
                
//            } catch (final IOException e) {
//                Logs.myLogger.error("{} :: {}", url1.toExternalForm(), e);
//                Utilities.displayOutput("Failed while reading bytes from " + 
//                                url1.toExternalForm(), false);    
//                throw new IOException(e);
//            }
//              
//        } catch (final NullPointerException npe) {
//            Logs.myLogger.error("Invaild URL: {}. {}", this.url, npe);
//            Utilities.displayOutput("Invalid URL: " + this.url, false);   
//            throw new NullPointerException();
//        }         
    }

    /**
     * 
     * @param outputstream
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void cleanData(final ByteArrayOutputStream outputstream) throws IOException {
        boolean YAHOO = false;
        boolean QUANDL = false;
        
        if (this.source.matches("Yahoo")) {
            YAHOO = true;
        }
        else if (this.source.matches("QUANDL")) {
            QUANDL = true;
        }
        
        final Charset charset = Charset.forName("UTF-8");
        final List<String> dateData = new ArrayList();
        final List<String> priceData = new ArrayList();

        try {
            try ( //BufferedReader reader = Files.newBufferedReader(this.path, charset);
                    final BufferedReader reader = new BufferedReader(new StringReader(outputstream.toString()))) {
                //Ignore header is True for Yahoo, FED, and QUANDL
                String line = reader.readLine();
                
                while ((line = reader.readLine()) != null) {
                    //fileData.add(line);
                    
                    final String[] splits = line.split(",");
                    if (YAHOO) {
                        if (splits.length > 6) {
                            boolean found = false;
                            for (String p : dateData) {
                                if (p.equals(splits[0])) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                try {
                                    priceData.add(String.valueOf(Double.parseDouble(splits[6])));
                                    dateData.add(splits[0]);
                                } catch (final Exception ex) {
                                    Logs.myLogger.error("Failed to write line. Category Name = {}. Selection Name = {}.", this.dataType, this.dataName);
                                }
                            }
                        }
                    }
                    else if (QUANDL) {
                        if (splits.length > this.QuandlColumn-1) {
                            boolean found = false;
                            for (String p : dateData) {
                                if (p.equals(splits[0])) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                try {
                                    priceData.add(String.valueOf(Double.parseDouble(splits[this.QuandlColumn-1])));
                                    dateData.add(splits[0]);
                                } catch (final Exception ex) {
                                    Logs.myLogger.error("Failed to write line. Category Name = {}. Selection Name = {}.", this.dataType, this.dataName);
                                }
                            }
                        }
                    }
                    else {
                        if (splits.length > 1) {
                            if (!splits[1].equals(".")) {
                                boolean found = false;
                                for (String p : dateData) {
                                    if (p.equals(splits[0])) {
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    try {
                                        priceData.add(String.valueOf(Double.parseDouble(splits[1])));
                                        dateData.add(splits[0]);
                                    }
                                    catch(final Exception ex) {
                                        Logs.myLogger.error("Failed to write line. Category Name = {}. Selection Name = {}.", this.dataType, this.dataName);
                                    }
                                }
                            }
                        }
                    }
                }

                if (YAHOO) {
                    //Reverse Yahoo Data
                    Collections.reverse(dateData);
                    Collections.reverse(priceData);
                }
                //write data
            }
        } catch (final IOException x) {
            Logs.myLogger.error("Failed to write CSV file. Category Name = {}, Selection Name = {}. {}",
                    this.dataType, this.dataName, x);
            if (RunContext.isGUI) {
                updateWorker.publishText("Failed to process CSV file: " + this.dataName);
            }
            else {
                System.out.println("Failed to process CSV file: " + this.dataName);
            }
            throw new IOException(x);
        }
        
        final List<String> oldpriceData = new ArrayList();
        final List<String> olddateData = new ArrayList();
                    
        final Path filepath = FileSystems.getDefault().getPath(Indices.getFilePath() + 
            "ProgramData" + Indices.filePathSymbol + this.dataType +
            Indices.filePathSymbol + this.dataName + Indices.filePathSymbol + 
            this.dataName + "dailydata.csv");
        
        //if dailydata file already exists for certain countries, delete them
        final String[] badCountries = {"Stocks", "HongKong", "Germany", "UnitedKingdom", "India", "Brazil",
            "China", "Japan", "Australia", "Argentina", "SouthKorea", "Israel",
            "Singapore", "Italy", "Mexico", "Indonesia", "France", "Canada",
            "Taiwan", "Austria", "Denmark", "Netherlands", "NewZealand", "Norway",
            "Spain", "Sweden", "Switzerland", "Russia", "Dubai", "Greece", "Baltic",
            "Peru", "Venezuela", "Chile"};

        boolean matchBadCountry = false;
        for (final String badCountry : badCountries) {
            if (this.dataType.matches(badCountry) && Files.exists(filepath)) {
                matchBadCountry = true;
            }
        }
        
        if (matchBadCountry) {
            Files.delete(filepath);
        }

        if (Files.exists(filepath)) {
                
            //No Header
            try (final BufferedReader reader = Files.newBufferedReader(filepath, charset)) {
                //No Header
                String line; //= reader.readLine();
                
                //All daily data is tab separated .tsv file really incorrect to name .csv
                while ((line = reader.readLine()) != null) {
                    final String[] splits = line.split("\t");
                    olddateData.add(splits[0]);
                    oldpriceData.add(splits[1]);
                }
            } //= reader.readLine();
            //find where dates match
            final String oldDateDataLastString = olddateData.get(olddateData.size() - 1);

            int match = 0;
            for (int i = 0; i < dateData.size(); i++) {
                if (oldDateDataLastString.equals(dateData.get(i))) {
                    match = i;
                }
            }



            final File dailydata = new File(Indices.getFilePath() + "ProgramData" +
                    Indices.filePathSymbol + this.dataType + Indices.filePathSymbol + 
                    this.dataName + Indices.filePathSymbol + this.dataName + 
                    "dailydata.csv");
        
            dailydata.createNewFile();
            final FileWriter writer = new FileWriter(dailydata);
        
            for (int i = 0; i < olddateData.size(); i++) {
                writer.write(olddateData.get(i) + "\t" + oldpriceData.get(i) + "\n");
            }
        
            if (match > 0) {
                for (int i = match + 1; i < dateData.size(); i++) {
                    writer.write(dateData.get(i) + "\t" + priceData.get(i) + "\n");
                }
            }
            writer.flush();
            writer.close();
            
        } else {
            
            try {
                final File dailydata = new File(Indices.getFilePath() + "ProgramData" + 
                        Indices.filePathSymbol + this.dataType + Indices.filePathSymbol + 
                        this.dataName + Indices.filePathSymbol + this.dataName + "dailydata.csv");

                dailydata.createNewFile();
                try (final FileWriter writer = new FileWriter(dailydata)) {
                    for (int i = 0; i < dateData.size(); i++) {
                        writer.write(dateData.get(i) + "\t" + priceData.get(i) + "\n");
                    }
                    writer.flush();
                }
                
            } catch (final IOException th) {
                final String name = this.dataName;
                Logs.myLogger.error("Failed to create daily data. Category Name = {}, Selection Name = {}. {}",
                        this.dataType, this.dataName, th);
                if (RunContext.isGUI) {
                    updateWorker.publishText("Failed to create daily data: " + name);
                } 
                else {
                    System.out.println("Failed to create daily data: " + name);
                }
                throw new IOException(th);
            }
        }    
    }       
}