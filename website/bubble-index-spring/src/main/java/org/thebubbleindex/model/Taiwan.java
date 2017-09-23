package org.thebubbleindex.model;

public class Taiwan extends BubbleIndexTimeseries {
    
	protected Taiwan() {}
	
	public Taiwan(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
