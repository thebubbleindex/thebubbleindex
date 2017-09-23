package org.thebubbleindex.model;

public class Italy extends BubbleIndexTimeseries {
	    
	protected Italy() {}
	
	public Italy(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
