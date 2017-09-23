package org.thebubbleindex.model;

public class HongKong extends BubbleIndexTimeseries {
	    
	protected HongKong() {}
	
	public HongKong(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
