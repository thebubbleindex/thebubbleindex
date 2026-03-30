# Website

This directory contains web application code and related assets for The Bubble Index website ([thebubbleindex.com](https://www.thebubbleindex.com)).

## Structure

```
website/
├── bubble-index-spring/   # Spring Boot web application (current)
├── lpplmarketwatch/       # Hugo static site — "LPPL Market Watch" blog
├── 3DFieldPro/            # VBScript for batch 3DFieldPro DXF processing
├── pdf/                   # R scripts for generating PDF visualisations
└── play/                  # Legacy Play Framework web application (reference only)
```

## Subdirectories

**[bubble-index-spring](bubble-index-spring/)** — The current Spring Boot (Java 8) web application that serves the Bubble Index website. Uses Groovy templates and an H2 database.

**[lpplmarketwatch](lpplmarketwatch/)** — A Hugo-based static site for the "LPPL Market Watch" blog, hosted on IPFS.

**[3DFieldPro](3DFieldPro/)** — A VBScript (`recursive-json-dxf-files.vbs`) that batch-processes Bubble Index XYZ/CSV output files using the 3DFieldPro application to produce DXF contour files.

**[pdf](pdf/)** — R scripts used to generate PDF plots and visualisations of Bubble Index data.

**[play](play/)** — A legacy Play Framework web application kept for historical reference.
