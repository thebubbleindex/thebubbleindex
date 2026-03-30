# composite-files

A Maven utility (Java 8) that reads the individual Bubble Index output files from `ProgramData` and computes composite index files — aggregated views across multiple time series — together with their associated D3 time-series data files.

## Prerequisites

- Java 8 JDK
- Apache Maven 3.6+

## Building

```bash
mvn clean compile assembly:single
```

The executable jar and any required resource files are placed in `target/CreateCompositeFiles/`.

## Usage

Place `CreateCompositeFiles.jar` in the same directory as the `ProgramData` folder, then run:

```bash
java -jar CreateCompositeFiles.jar
```

## Dependencies

- [Google Guava](https://github.com/google/guava)
- [Apache Commons Math 3](https://commons.apache.org/proper/commons-math/)

## See Also

- [CreateD3Files](../CreateD3Files/) — generates D3 data files for individual time series
