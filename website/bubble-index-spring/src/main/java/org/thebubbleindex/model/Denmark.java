package org.thebubbleindex.model;

public class Denmark extends BubbleIndexTimeseries {
	    
	protected Denmark() {}
	
	public Denmark(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
