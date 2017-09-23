package org.thebubbleindex.model;

public class BubbleIndex extends BubbleIndexTimeseries {
	public String type;
	public String name;
	public String symbol;
	public String pdfLocation;
	public String plotLocation;
	public String contourPDFLocation;
	public String contourPNGLocation;
	public String threejsLocation;

	public BubbleIndex() {
		this.type = null;
		this.name = null;
		this.symbol = null;
		this.pdfLocation = null;
		this.plotLocation = null;
		this.contourPDFLocation = null;
		this.contourPNGLocation = null;
		this.threejsLocation = null;
	}

	public void findLocation() {

		this.threejsLocation = "//bigttrott-thebubbleindex.netdna-ssl.com/WebGL/" + this.type + "/" + this.symbol + "/"
				+ this.symbol + ".html";

		this.pdfLocation = "//bigttrott-thebubbleindex.netdna-ssl.com/PDF/" + this.type + "/" + this.symbol + "/"
				+ this.symbol + ".pdf";

		this.contourPDFLocation = "//bigttrott-thebubbleindex.netdna-ssl.com/" + this.type + "/" + this.symbol + "/"
				+ this.symbol + "Contour.pdf";

		this.contourPNGLocation = "//bigttrott-thebubbleindex.netdna-ssl.com/" + this.type + "/" + this.symbol + "/"
				+ this.symbol + "Contour.png";
	}

	public void findPlotLocation() {
		this.plotLocation = "/public/ProgramData/" + this.type + "/" + this.symbol + "/" + this.symbol + ".tsv";
	}
}
