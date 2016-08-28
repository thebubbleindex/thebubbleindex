/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compositefiles;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author windows
 */
public class CompositeIndex {
    
    private String countryName;
    private int[] dayWindow;
    private double[] quantileValues;
    private HashMap<String, ArrayList<Double>> rawValueMap;
    //private String[] stockNames;
    private List<String> stockNames;        
    private int minSize;
    Map<String, Table<String, Integer, Double>> d3Tables;

    static final Map<Integer, String> quantiles;
        
    private final InputCategory category;

    static { 
        quantiles = new HashMap<>();
        quantiles.put(50, "Fifty");
        quantiles.put(80, "Eighty");
        quantiles.put(90, "Ninety");
        quantiles.put(95, "NinetyFive");
        quantiles.put(99, "NinetyNine");
    }
    
    CompositeIndex(InputCategory category, int[] dayWindow, double[] quantileValues, int minSize) {
        this.countryName = category.getName();
        this.category = category;
        this.stockNames = category.getComponents();
        this.dayWindow = dayWindow;
        this.quantileValues = quantileValues;
        this.minSize = minSize;
        rawValueMap = new HashMap();

        //d3Table = TreeBasedTable.create();
        d3Tables = new HashMap<>();
    }
    
    public void run() {
        //getStockNames();
        System.out.println("Name of country: " + category.getName());
        System.out.println("Number of of stocks: " + stockNames.size());
        
        for (int i = 0; i < dayWindow.length; i++) {
            for (int j = 0; j < stockNames.size(); j++) {
                
                String PreviousFilePath = CompositeFiles.userDir + CompositeFiles.filePathSymbol + "ProgramData" + 
                    CompositeFiles.filePathSymbol + countryName + CompositeFiles.filePathSymbol + 
                    stockNames.get(j) + CompositeFiles.filePathSymbol + stockNames.get(j) + 
                    Integer.toString(dayWindow[i]) + "days.csv";
                
                Path filepath = FileSystems.getDefault().getPath(PreviousFilePath);
                if (Files.exists(filepath)) {
                    
                
                    List<String> dataValues = new ArrayList<>();
                    List<String> dateValues = new ArrayList<>();

                    InputData.ReadValues(PreviousFilePath, dataValues, 
                                dateValues, true, true);

                    putRawValues(dataValues, dateValues);
                }
                
            }
            String savePath = CompositeFiles.userDir + CompositeFiles.filePathSymbol + "Composite" + CompositeFiles.filePathSymbol +
                        countryName + String.valueOf(dayWindow[i]);
            String d3savePath = "/home/green/Desktop/D3/";
            calculateQuantileValues(dayWindow[i], savePath, d3savePath);
            rawValueMap.clear();
        }
    }
    
    private void putRawValues(List<String> dataValues, List<String> dateValues) {
        Iterator<String> dataIterator = dataValues.iterator();
        Iterator<String> dateIterator = dateValues.iterator();
        
        if (dataValues.size() != dateValues.size()) {
            System.out.println("Length of lists do not match!");
        }
        else {
            while(dataIterator.hasNext()) {
                String date = dateIterator.next();
                double data = Double.parseDouble(dataIterator.next());
                
                if (rawValueMap.containsKey(date)){
                    ArrayList<Double> tempList = rawValueMap.get(date);
                    tempList.add(data);
                    rawValueMap.put(date, tempList);
                }
                else {
                    ArrayList<Double> tempList = new ArrayList<>();
                    tempList.add(data);
                    rawValueMap.put(date, tempList);
                }
            }
        }
    }
    
   /* private void getStockNames() {
        switch (countryName) {
            case "Indices":
                stockNames = Indices.IndicesNamesArray;
                break;
            case "Stocks":
                stockNames = Indices.StocksNamesArray;
                break;
            case "Currencies":
                stockNames = Indices.CurrenciesNamesArray;
                break;
            case "HongKong":
                stockNames = Indices.HongKongNamesArray;
                break;                
            case "Germany":
                stockNames = Indices.GermanyNamesArray;
                break;        
            case "UnitedKingdom":
                stockNames = Indices.UnitedKingdomNamesArray;
                break; 
            case "India":
                stockNames = Indices.IndiaNamesArray;
                break;                 
            case "Brazil":
                stockNames = Indices.BrazilNamesArray;
                break;  
            case "China":
                stockNames = Indices.ChinaNamesArray;
                break; 
            case "Japan":
                stockNames = Indices.JapanNamesArray;
                break; 
            case "Australia":
                stockNames = Indices.AustraliaNamesArray;
                break; 
            case "Argentina":
                stockNames = Indices.ArgentinaNamesArray;
                break; 
            case "SouthKorea":
                stockNames = Indices.SouthKoreaNamesArray;
                break; 
            case "Israel":
                stockNames = Indices.IsraelNamesArray;
                break;                  
            case "Singapore":
                stockNames = Indices.SingaporeNamesArray;
                break;                
            case "Italy":
                stockNames = Indices.ItalyNamesArray;
                break;                            
            case "Mexico":
                stockNames = Indices.MexicoNamesArray;
                break;                                       
            case "Indonesia":
                stockNames = Indices.IndonesiaNamesArray;
                break;                       
            case "France":
                stockNames = Indices.FranceNamesArray;
                break;
            case "Canada":
                stockNames = Indices.CanadaNamesArray;
                break;                             
            case "Taiwan":
                stockNames = Indices.TaiwanNamesArray;
                break;             
            case "Austria":
                stockNames = Indices.AustriaNamesArray;
                break;  
            case "Denmark":
                stockNames = Indices.DenmarkNamesArray;
                break;                  
            case "Netherlands":
                stockNames = Indices.NetherlandsNamesArray;
                break;              
            case "NewZealand":
                stockNames = Indices.NewZealandNamesArray;
                break;              
            case "Norway":
                stockNames = Indices.NorwayNamesArray;
                break;              
            case "Spain":
                stockNames = Indices.SpainNamesArray;
                break;              
            case "Sweden":
                stockNames = Indices.SwedenNamesArray;
                break;              
            case "Switzerland":
                stockNames = Indices.SwitzerlandNamesArray;
                break;              
            case "Russia":
                stockNames = Indices.RussiaNamesArray;
                break;              
            case "Dubai":
                stockNames = Indices.DubaiNamesArray;
                break;              
            case "Greece":
                stockNames = Indices.GreeceNamesArray;
                break;              
            case "Baltic":
                stockNames = Indices.BalticNamesArray;
                break;              
            case "Peru":
                stockNames = Indices.PeruNamesArray;
                break;              
            case "Venezuela":
                stockNames = Indices.VenezuelaNamesArray;
                break;   
            case "Chile":
                stockNames = Indices.ChileNamesArray;
                break;  
            case "ETF":
                stockNames = Indices.ETFNamesArray;
                break;  
            case "Other":
                stockNames = Indices.OtherNamesArray;
                break;                  
            default:
                stockNames = Indices.CommoditiesNamesArray;                
                break;
        }
    }
   */
    
    void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    
    void setDayWindow(int[] dayWindow) {
        this.dayWindow = dayWindow;
    }
    
    void setQuantileValues(double[] quantileValues) {
        this.quantileValues = quantileValues;
    }

    private void calculateQuantileValues(int dayWindow, String savePath, String d3savePath) {

        for (int i = 0; i < quantileValues.length; i++) {
            
            String quantileString = String.valueOf((int)(quantileValues[i]*100));
            Table<String, Integer, Double> quantileTable;
            
            if (d3Tables.containsKey(quantileString)) {
                quantileTable = d3Tables.get(quantileString);
            }
            
            else {
                quantileTable = TreeBasedTable.create();
            }
            
            HashMap<String, Double> quantileIndex = new HashMap();
            
            //iterate over hashmap
            for (Map.Entry<String, ArrayList<Double>> entry : rawValueMap.entrySet()) {
                String key = entry.getKey();
                ArrayList<Double> tempList = entry.getValue();
                double quantileValue = calculateQuantile(quantileValues[i], tempList);
                quantileIndex.put(key, quantileValue);
            }
            Map<String, Double> sorted = new TreeMap<>(quantileIndex);
            try {
                System.out.println("Save Path: " + savePath + "Quantile" + quantileString + ".csv");
                InputData.writetoFile(sorted, savePath + "Quantile" + quantileString + ".csv");
                //InputData.createD3File(sorted, d3savePath + "Composite" + quantiles.get((int)(quantileValues[i]*100)) + Indices.filePathSymbol
                  //      + this.countryName + Indices.filePathSymbol + this.countryName + ".tsv");
                addToTable(quantileTable, sorted, dayWindow);
                if (dayWindow == 1764) {
                    System.out.println("Save Path: " + d3savePath + "Composite" + getQuantileName((int)(quantileValues[i]*100)) + CompositeFiles.filePathSymbol +
                        countryName + CompositeFiles.filePathSymbol + countryName + ".tsv");

                    InputData.createD3File(quantileTable, d3savePath + "Composite" + getQuantileName((int)(quantileValues[i]*100)) + CompositeFiles.filePathSymbol +
                        countryName + CompositeFiles.filePathSymbol + countryName + ".tsv");
                }
                else {
                    d3Tables.put(quantileString, quantileTable);
                }
            } catch (IOException ex) {
                //ex.printStackTrace();
                System.out.println("Error writing the file:" + ex);
            }
        }
        
    }

    private double calculateQuantile(double quantileValue, ArrayList<Double> tempList) {
        
        //remove all zeros
        int i = 0;
        final double EPSILON = 0.01;
        final int SIZE = this.minSize;
        while (i < tempList.size()) {
            if (tempList.get(i) < EPSILON) {
                tempList.remove(i);
            }
            else {
                i++;
            }
        }
        
        
        if (tempList.size() < SIZE)
            return 0.0;
        else {
            int indexValue = (int) Math.round(tempList.size() * quantileValue);
            Collections.sort(tempList);
            if (indexValue == tempList.size()) {
                return tempList.get(indexValue - 1);
            }
            else {
                return tempList.get(indexValue);
            }
        }
    }

    private void addToTable(Table<String, Integer, Double> quantileTable, Map<String, Double> sorted, int dayWindow) {
        for (Map.Entry<String, Double> entry : sorted.entrySet()) {
            if (entry.getValue() > 0)
                quantileTable.put(entry.getKey(), dayWindow, entry.getValue());
        }
    }

    private String getQuantileName(int i) {

        switch (i) {
            case 50:
                return "Fifty";
            case 80:
                return "Eighty";
            case 90:
                return "Ninety";
            case 95:
                return "NinetyFive";
            case 99:
                return "NinetyNine";
            default:
                return "";
        }     
    }
    
}
