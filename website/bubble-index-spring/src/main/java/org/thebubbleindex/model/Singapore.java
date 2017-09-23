package org.thebubbleindex.model;

public class Singapore extends BubbleIndexTimeseries {
    
	protected Singapore() {}
	
	public Singapore(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
