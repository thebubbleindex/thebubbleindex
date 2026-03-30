# lpplmarketwatch

A [Hugo](https://gohugo.io/) static website for the **LPPL Market Watch** blog — a companion publication to The Bubble Index. The site is hosted on [IPFS](https://ipfs.tech/).

## Prerequisites

- [Hugo](https://gohugo.io/installation/) (static site generator)

## Building

```bash
cd hugo
hugo
```

Static output is written to `hugo/public/`.

## Development server

```bash
cd hugo
hugo server
```

Then open [http://localhost:1313](http://localhost:1313) in your browser.

## Configuration

Site settings (title, base URL, social links, analytics) are in `hugo/config.toml`.

The site is configured to publish to:
```
https://gateway.ipfs.io/ipns/QmPC1xcsMTDKFTWHLrGEaNTCEdceUf4wWRJhM5P4kDeUsv
```

## See Also

- [bubble-index-spring](../bubble-index-spring/) — the main Spring Boot website application
