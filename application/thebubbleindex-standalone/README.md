# thebubbleindex-standalone

The main Maven project for The Bubble Index application. Builds a self-contained executable jar (`Bubble_Index.jar`) that calculates LPPL (Log-Periodic Power Law) oscillation indicators for financial time series and displays results via a Swing GUI or command-line interface.

## Prerequisites

| Requirement | Version | Notes |
|---|---|---|
| Java JDK | 21 or later | Required to build; JRE 21+ is sufficient to run the pre-built jar |
| Apache Maven | 3.6+ | Required to build from source |
| OpenCL-compatible GPU | — | Optional; enables GPU-accelerated computation |

## Building

```bash
# 1. Install the bundled Yeppp! SIMD library into your local Maven repository
mvn install:install-file \
  -Dfile=src/main/resources/yeppp-bundle.jar \
  -DgroupId=yeppp \
  -DartifactId=yeppp-java \
  -Dversion=1.0 \
  -Dpackaging=jar

# 2. Build the executable jar
mvn clean compile assembly:single
```

The compiled jar and a ready-to-use sample `ProgramData` folder are placed in `target/thebubbleindex/`.

## Running

**GUI mode:**
```bash
java -jar Bubble_Index.jar
```

**Command-line (non-GUI) mode:**
```bash
java -jar Bubble_Index.jar noGUI <RunType> <Category> <Selection> <Windows> <Threads> <T_Crit> <M> <Omega> <ForceCPU>
```

See the top-level [README](../../README.md#TOC-Getting-Started) for detailed usage examples and ProgramData setup instructions.

## Testing

```bash
mvn test
```

## Project Layout

```
src/
├── main/
│   ├── java/          # Application source code
│   ├── resources/     # OpenCL kernel files and bundled Yeppp! library
│   └── external-resources/ProgramData/  # Sample ProgramData copied to build output
└── test/              # JUnit tests
```
