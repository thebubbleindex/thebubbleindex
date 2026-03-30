# CreateD3Files

A Maven utility (Java 8) that reads the Bubble Index output files from the `ProgramData` directory and generates D3.js-compatible time-series data files for use in D3 JavaScript visualisations on the website.

## Prerequisites

- Java 8 JDK
- Apache Maven 3.6+

## Building

```bash
mvn clean compile assembly:single
```

The executable jar is placed in `target/`.

## Usage

Place `CreateD3Files.jar` in the same directory as the `ProgramData` folder, then run:

```bash
java -jar CreateD3Files.jar
```

The utility will scan all time-series output in `ProgramData` and write the corresponding D3 data files alongside them.

## See Also

- [d3plots](../d3plots/) — generates D3 plot HTML files from templates
- [composite-files](../composite-files/) — generates composite index files and their associated D3 data
