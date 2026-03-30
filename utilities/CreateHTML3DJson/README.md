# CreateHTML3DJson

A Maven utility (Java 8) that reads Bubble Index output files and generates 3D HTML/JSON visualisation files. It uses DXF-to-JSON conversion libraries to produce browser-ready 3D representations of the data.

## Prerequisites

- Java 8 JDK
- Apache Maven 3.6+
- Several custom JARs must be installed into your local Maven repository before building (see below)

## Installing Custom Dependencies

The following JARs are bundled in `src/main/resources/` and must be installed manually:

```bash
mvn install:install-file -Dfile=src/main/resources/jReality.jar \
  -DgroupId=de.raida.cad.dex -DartifactId=jReality -Dversion=1.0 -Dpackaging=jar

mvn install:install-file -Dfile=src/main/resources/RaidaCADAdapter.jar \
  -DgroupId=de.raida.cad.dex -DartifactId=RaidaCADAdapter -Dversion=1.0 -Dpackaging=jar

mvn install:install-file -Dfile=src/main/resources/RaidaDXFLoader.jar \
  -DgroupId=de.raida.cad.dex -DartifactId=RaidaDXFLoader -Dversion=1.0 -Dpackaging=jar

mvn install:install-file -Dfile=src/main/resources/RaidaJSONExporter.jar \
  -DgroupId=de.raida.cad.dex -DartifactId=RaidaJSONExporter -Dversion=1.0 -Dpackaging=jar

mvn install:install-file -Dfile=src/main/resources/velocity-1.7-dep.jar \
  -DgroupId=de.raida.cad.dex -DartifactId=velocity-1.7-dep -Dversion=1.0 -Dpackaging=jar
```

## Building

After installing the custom dependencies:

```bash
mvn clean compile assembly:single
```

The executable jar is placed in `target/CreateJSON3DFiles.jar`.

## Usage

Place `CreateJSON3DFiles.jar` in the same directory as the `ProgramData` folder, then run:

```bash
java -jar CreateJSON3DFiles.jar
```

## See Also

- [CreateXYZFiles](../CreateXYZFiles/) — creates (x, y, z) format files used as input for 3D programs
- [3DFieldPro](../../website/3DFieldPro/) — VBScript for batch-processing data with 3DFieldPro
