# d3plots

A Maven utility (Java 8) that uses [Apache Velocity](https://velocity.apache.org/) templates to generate D3.js plot HTML files from Bubble Index output data.

## Prerequisites

- Java 8 JDK
- Apache Maven 3.6+

## Building

```bash
mvn clean compile assembly:single
```

The executable jar is placed in `target/CreateD3PlotFiles.jar`.

## Usage

Place `CreateD3PlotFiles.jar` in the same directory as the `ProgramData` folder, then run:

```bash
java -jar CreateD3PlotFiles.jar
```

## See Also

- [CreateD3Files](../CreateD3Files/) — generates the underlying D3 time-series data files consumed by these plots
