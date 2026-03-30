# Application

This directory contains the Java source code for The Bubble Index application.

## Structure

```
application/
├── thebubbleindex-standalone/   # Main application (Maven, Java 21)
└── original-standalone/         # Legacy NetBeans project (reference only)
```

## Subdirectories

**[thebubbleindex-standalone](thebubbleindex-standalone/)** — The current, actively maintained Maven project. Builds a self-contained executable jar (`Bubble_Index.jar`) that runs The Bubble Index in both GUI and command-line modes. Requires Java 21 JDK and Maven 3.6+.

**[original-standalone](original-standalone/)** — A historical NetBeans project kept for reference. This is the original version of the application before it was migrated to Maven. It is not actively maintained.

## Building

See the [thebubbleindex-standalone README](thebubbleindex-standalone/README.md) or the top-level [README](../README.md#TOC-Compiling-from-Source) for build instructions.
