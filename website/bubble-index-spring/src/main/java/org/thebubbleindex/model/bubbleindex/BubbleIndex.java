package org.thebubbleindex.model.bubbleindex;

public class BubbleIndex {
	public String dtype;
	public String name;
	public String symbol;
	public String pdfLocation;
	public String plotLocation;
	public String contourPDFLocation;
	public String contourPNGLocation;
	public String threejsLocation;

	public BubbleIndex() {
		this.dtype = null;
		this.name = null;
		this.symbol = null;
		this.pdfLocation = null;
		this.plotLocation = null;
		this.contourPDFLocation = null;
		this.contourPNGLocation = null;
		this.threejsLocation = null;
	}

	public void findLocation() {

		this.threejsLocation = "https://cdn.thebubbleindex.com/TheBubbleIndex/" + this.dtype + "/" + this.symbol + "/"
				+ this.symbol + ".html";

		this.pdfLocation = "https://cdn.thebubbleindex.com/TheBubbleIndex/" + this.dtype + "/" + this.symbol + "/"
				+ this.symbol + ".pdf";

		this.contourPDFLocation = "//cdn.thebubbleindex.com/" + this.dtype + "/" + this.symbol + "/"
				+ this.symbol + "Contour.pdf";

		this.contourPNGLocation = "//cdn.thebubbleindex.com/" + this.dtype + "/" + this.symbol + "/"
				+ this.symbol + "Contour.png";
	}

	public void findPlotLocation() {
		this.plotLocation = "/plot?type=" + this.dtype + "&symbol=" + this.symbol + "&name=" + this.name;
	}
}
