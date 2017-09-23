package org.thebubbleindex.model;

public class Indices extends BubbleIndexTimeseries {
    
	protected Indices() {}
	
	public Indices(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
