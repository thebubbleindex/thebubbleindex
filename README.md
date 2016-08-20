# The Bubble Index

[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/com.google.code.gson/gson/badge.svg)](http://)

The Bubble Index, a Java application to measure the level of financial bubbles.

A Java application designed to monitor LPPL oscillations in financial markets. All results can be viewed at: https://www.thebubbleindex.com

For information on the algorithm see: http://arxiv.org/abs/cond-mat/0201458

Feel free to comment, suggest changes, or point out bugs/problems.

1. [Installation](#TOC-Overview)
2. [Getting Started](#TOC-Goals-for-Gson)
3. [The Data](#TOC-Gson-Performance-and-Scalability)
4. [The Graphs](#TOC-Gson-Users)
5. [Utilities](#TOC-Utilities)
6. [Compiling From Source](#TOC-Compiling-from-Source)

## <a name="TOC-Overview"></a>Installation

First make sure Java 8 JDK is installed and working on your computer (or JRE if not building).

1. Create the ProgramData Folder and its Subdirectories.

To run the program with a desired index, stock, currency, or commodity the user must create the following folders in the same directory as Bubble_Index.jar:

*ProgramData*

Now create folders within the ProgramData directory. These must match the name of respective .csv files which contain a list of all selections contained in the category:

*Indices - Indices.csv*
*Currencies - Currencies.csv*
*Stocks - Stocks.csv*
*Commodities - Commodities.csv*

Within each of these folders, create a folder named after the desired index. For example, within the Stocks folder have the following folders:

*TSLA*
*JCP*
*AAPL*

2. Create the "List" .csv files.

In the ProgramData folder, the .csv files listed below must contain the list of desired selection names which are displayed in the GUI. These should contain a list of all the folders in each sub-directory. The contents of these files must be in the correct format, with each item separated by a "\n" character, i.e. each item is on a separate line:

*Indices.csv*
*Currencies.csv*
*Stocks.csv*
*Commodities.csv*

3. Create the "CategoryList" .csv file.

In the ProgramData folder, the CategoryList.csv file which contains all the category types which are displayed in the GUI dropdown menu.

4. Create the "UpdateList" .csv files.

To update the data, there must be an UpdateCategories.csv file in the ProgramData directory and an UpdateSelection.csv file in each of the sub-directories. The contents and format of the UpdateCatagories.csv should be like this:

*Indices*
*Currencies*
*Stocks*
*Commodities*

And the contents and format of the UpdateSelection.csv should be like this:

*Name,DataSource,QuandlDataset,QuandlName,QuandlColumn(startindex=1),isYahooIndex*
*TSLA,Yahoo,NA,NA,0,0*

The **Name** value must be the string corresponding to the individual folder for this time series in the ProgramData sub-directories.

The **DataSource** value must be one of three values:

*Yahoo*
*FED*
*QUANDL*

The **QuandlDataset** value must be the string of the dataset in the Quandl url, or NA.

The **QuandlName** value must be the name string in the url of the time series on Quandl, or NA.

The **QuandlColumn** value must be the integer representing the column location of the closing or settle price if the data is obtained from Quandl or NA if not.

The **isYahooIndex** value must be set to 1 if the time series is a Yahoo Finance Index, i.e. its quote contains the carrot symbol ^ or set to 0 if not an index.

5. (Optional) To specify runtime GUI properties and Lomb-Scargle properties:

In the ProgramData folder, create the files *gui.properties* and *lombscargle.properties*. Examples:

**gui.properties**
```java
omega=6.28
tcrit=21.0
mcoeff=0.38
windows=52,104,153
threads=4
begdate=2012-12-31
enddate=2014-05-23
```

**lombscargle.properties**
```java
freqsize=70
qsize=18
hsize=19
```

## <a name="TOC-Goals-for-Gson"></a>Getting Started

Make sure the program directory is set up as described in Installation. There is a GUI and non-GUI command line mode to run the program. Either mode can be started from the terminal/command prompt. The Bubble_Index.jar executable must be located in the same directory as the **ProgramData** folder.

**GUI Mode**

The GUI Mode can be started by simply double clicking the Bubble_Index.jar executable jar.

To start from command, enter:
```java
java -jar Bubble_Index.jar
```
command while in the folder containing the Bubble_Index.jar executable. Or Issue:
```java
java -jar /dir_path/Bubble_Index.jar
```
To get started with The Bubble Index, the daily data must be downloaded or updated. This is all done automatically by clicking the **Update All** button.

When updating non-American stocks (such as Brazil, India, etc...) with the **Update All** button, MAKE SURE TO DELETE ALL PREVIOUS DAILY DATA CSV FILES before the update. (NOTE: with recent releases, manual deletion is not necessary because the program automatically performs the delete) If not, you will get situations where the daily data repeats itself numerous times. You can do this easily in Linux by issuing the following command while in the ProgramData folder:
```java
find . -type f -name '*dailydata.csv' -delete
```
Once the data has been updated, there are two ways to proceed: calculate a single time series or calculate the values for all the times series listed in the drop down category. Select the **Plot Graph** check box if a graph is desired. When plotting, you may specify the Beginning and End Dates via the GUI.

1. Set Model Parameters

Enter the desired model parameters for Omega, M, and T_critical. The default values are the values which are used for the website indices. These parameters are found in the LPPL Oscillation model equation: http://arxiv.org/abs/cond-mat/0201458. Enter the length of data (windows) with which to run the calculations. This is a comma separated list. Ex. 52, 104, 153, 256, 512

2. Run a Single Time Series

Choose the desired time series from the drop down selection boxes. Adjust the model window lengths if desired. Click **Run**. Wait for the program to calculate the values. If this is the first run, it may take several hours depending on the hardware available. After completion, the GUI will say "Done." If the **Plot Graph** check box was selected, a two windows should appear. One contains The Bubble Index values plotted with the daily values. The other is a graph of the derivatives. (Warning: Times series with dates before 1900 will NOT plot)

3. Run All Names

Choose the desired category. Click the **Run All Names** button. The program will Run through all the indices in the category type.

4. Run All Types

Click the **Run All Types** button. The program will Run through all the types in every category.

The **Stop** button may be used to stop the calculation of a single time series.

The **Exit** button closing the program.

**Non-GUI Command Line Mode**

1. Open a terminal or command prompt. Navigate to the directory of installation.

2. Issue the appropriate command:
```java
java -jar Bubble_Index.jar noGUI RunType Category Selection Windows Threads T_Crit M Omega ForceCPU
```
NOTE: Selection only applies if RunType=Single; Selection and Category do not apply if RunType=All

Examples:

Run all entries in the Currencies category, 100 threads with CPU:
```java
java -jar Bubble_Index.jar noGUI Category Currencies 3000,3200,3500,3700,4000 100 21.0 0.38 6.28 true
```
Run BITSTAMPUSD with 4 threads, GPU:
```java
java -jar Bubble_Index.jar noGUI Single Currencies BITSTAMPUSD 153,256 4 21.0 0.38 6.28 false
```
Run All categories and all entries with 4 threads and CPU:
```java
java -jar Bubble_Index.jar noGUI All 512,1260 4 21.0 0.38 6.28 true
```
Update data:
```java
java -jar Bubble_Index.jar noGUI Update
```
Update data with a specified Quandl API key:
```java
java -jar Bubble_Index.jar noGUI Update QuANdLkEy
```
## <a name="TOC-Gson-Performance-and-Scalability"></a>The Data

The program stores the calculated output as a comma separated file (sorry for my inconsistencies with file types -- the daily data files are labeled as .csv but they are really .tsv files) in a ProgramData sub-directory corresponding the to category and the name of the time series.

Each window length will create its own .csv file. For example, 52 days for TSLA will create a file called TSLA52days.csv.

Each of these files has the following header:

*Period, Date, Value*

The **Period** is the number index of the calculated value.

The **Date** is the date of The Bubble Index value corresponding with the time series.

The **Value** is the calculated value of The Bubble Index.

Each folder should contain a *dailydata.csv file. This is a two column tsv file with the date in the first column and the price time series in the second column. The date format is: YYYY-MM-DD

To run a single window length, simply input that number, ex. 1764, into ALL of the window length boxes.

As part of the algorithm and to make The Bubble Index values comparable for all price trajectories, all price time series are adjusted internally to begin at 100. Thus, all values of the same window are comparable. In other words, TSLA512days.csv values can be directly compared with DJIA512days.csv.

If for any reason an error or problem repeatedly occurs simple delete the .csv files and start over with the **Update All** button.

## <a name="TOC-Gson-Users"></a>The Graphs

If the **Plot Graph** box is checked, then two plots will be generated with based on methods from http://www.jfree.org/jfreechart/index.html.

As noted previously, JFreeChart does not support dates before 1900 at the time of writing this.

If you only want to graph a single window length, ex. 1764 days, simply input 1764 into all of the first four window length boxes.

If you want a custom date range, enter the correct date range in the date range boxes and check **Custom Date** and **Plot Graph**.


## <a name="TOC-Utilities"></a>Utilities

In the downloads section, there is a zip archive which contains two Java utilities. There should now be the following Utilities available: 

**combWindows.jar** - Combine Windows

This utility will take any number of output files and join them into a single .tsv file called NewStructure.tsv. This can then be processed by the xyzConversion utility below or used for other purposes. To combine the windows, place into the folder all *days.csv files of a single index (ex. DJIA52days.csv, DJIA104days.csv, ..., DJIA1764days.csv).

Now navigate to the folder and execute the following command from the terminal or command prompt:
```java
java -jar combWindows.jar *NAME*
```
where **NAME** is the name of the index, for example if the files are named DJIA52days.csv then NAME = DJIA.

**XYZConversion** - Convert the combined windows file (created above) into the (x, y, z, description) file format

This utility will take the combined file and create a new file which contains all the entries in the (x, y, z, string) format which many 3D programs prefer. Make sure the combined windows file created with the previous tool and the xyzConversion.jar file are located in the same directory.

Now navigate to the folder and execute the following command from the terminal or command prompt:
```java
java -jar xyzConversion.jar *combinedWindowName* *outputFileName*
```
where **combinedWindowName** is the name of the file created with the combWindow utility (ex. DJIANewStructure.tsv) and **outputFileName** is any file name which has the .csv file extension (ex. DJIAxyz.csv)

## <a name="TOC-Compiling-from-Source"></a>Compiling From Source

Make sure JDK and Maven is installed and issue these commands:
```java
mvn install:install-file -Dfile=${project.basedir}/src/main/resources/yeppp-bundle.jar -DgroupId=yeppp -DartifactId=yeppp-java -Dversion=1.0 -Dpackaging=jar

mvn clean compile assembly:single
```

###*License*

The Bubble Index is released under the [GNU GENERAL PUBLIC LICENSE](LICENSE).

Copyright 2016 The Bubble Index
