# pdf

R scripts for generating PDF plots and visualisations of Bubble Index output data.

## Scripts

| Script | Description |
|---|---|
| `r/allPDF.r` | Generates PDF plots for all time series in the `ProgramData` directory |
| `r/compositePDF.r` | Generates PDF plots for the composite index files |
| `r/windowAvg.r` | Computes and plots window-averaged Bubble Index values |

## Prerequisites

- [R](https://www.r-project.org/) (version 3.x or later)

## Usage

Run any script from the command line or from an R console:

```bash
Rscript r/allPDF.r
```

or from within R:

```r
source("r/allPDF.r")
```

Ensure that the working directory contains (or points to) the `ProgramData` folder produced by the main application before running.
