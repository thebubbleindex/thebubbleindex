package models;

public class BubbleIndex {
	public String type;
	public String name;
	public String symbol;
	public String pdfLocation;
	public String plotLocation;
	public String threejsLocation;

	public BubbleIndex() {
		this.type = null;
		this.name = null;
		this.symbol = null;
		this.pdfLocation = null;
		this.plotLocation = null;
		this.threejsLocation = null;
	}

	public void findLocation() {
		this.threejsLocation = "//cdn.thebubbleindex.com/WebGL/" + this.type + "/" + this.symbol + "/"
				+ this.symbol + ".html";
		this.pdfLocation = "//cdn.thebubbleindex.com/PDF/" + this.type + "/" + this.symbol + "/"
				+ this.symbol + ".pdf";
	}

	public void findPlotLocation() {
		this.plotLocation = "/public/ProgramData/" + this.type + "/" + this.symbol + "/" + this.symbol + ".tsv";
	}
}
