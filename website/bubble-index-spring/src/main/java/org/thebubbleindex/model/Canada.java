package org.thebubbleindex.model;

public class Canada extends BubbleIndexTimeseries {
    
	protected Canada() {}
	
	public Canada(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
