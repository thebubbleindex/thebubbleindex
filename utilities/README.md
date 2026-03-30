# Utilities

Post-processing utilities for The Bubble Index. Each subdirectory is an independent Maven project (Java 8) that transforms the output produced by the main application into formats suitable for visualisation or further analysis.

## Structure

```
utilities/
├── CreateD3Files/       # Generates D3.js time-series data files
├── composite-files/     # Generates composite index files
├── CreateXYZFiles/      # Converts output to (x, y, z) format for 3D tools
├── CreateHTML3DJson/    # Generates 3D HTML/JSON visualisation files
├── d3plots/             # Generates D3 plot HTML files (Apache Velocity templates)
└── legacy-netbeans/     # Legacy NetBeans versions of the utilities (reference only)
```

## Building

Each utility is a self-contained Maven project. Build any of them with:

```bash
cd <utility-directory>
mvn clean compile assembly:single
```

The resulting executable jar is placed in `target/`.

## Usage

Place the built jar in the same directory as the `ProgramData` folder that was populated by the main application, then run:

```bash
java -jar <UtilityName>.jar
```

Refer to each utility's own README for specific instructions.
