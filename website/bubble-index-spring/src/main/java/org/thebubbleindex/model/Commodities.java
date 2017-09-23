package org.thebubbleindex.model;

public class Commodities extends BubbleIndexTimeseries {
	    
	protected Commodities() {}
	
	public Commodities(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
