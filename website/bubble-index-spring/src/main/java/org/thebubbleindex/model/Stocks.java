package org.thebubbleindex.model;

public class Stocks extends BubbleIndexTimeseries {
	    
	protected Stocks() {}
	
	public Stocks(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
	
}
