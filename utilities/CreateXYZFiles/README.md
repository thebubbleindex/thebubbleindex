# CreateXYZFiles

A Maven utility (Java 8) that converts combined Bubble Index output files into `(x, y, z, description)` format. The resulting files are used with 3D visualisation programs such as [3DFieldPro](../../website/3DFieldPro/).

## Prerequisites

- Java 8 JDK
- Apache Maven 3.6+

## Building

```bash
mvn clean compile assembly:single
```

The executable jar is placed in `target/`.

## Usage

Place `CreateXYZFiles.jar` in the same directory as the `ProgramData` folder, then run:

```bash
java -jar CreateXYZFiles.jar
```

The utility reads the combined window output files and writes new files containing all entries in `(x, y, z, string)` format, which is the format preferred by most 3D visualisation tools.

## See Also

- [3DFieldPro](../../website/3DFieldPro/) — VBScript for batch-processing the XYZ files with 3DFieldPro
- [CreateHTML3DJson](../CreateHTML3DJson/) — generates browser-based 3D HTML/JSON visualisations
