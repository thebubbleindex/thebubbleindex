package org.thebubbleindex.model;

public class Indices extends BubbleIndexTimeseries {
    
	protected Indices() {}
	
	public Indices(final String name, final String symbol, final String type, final String keywords) {
		this.symbol = symbol;
		this.type = type;		
		this.name = name;
		this.keywords = keywords;
	}
}
