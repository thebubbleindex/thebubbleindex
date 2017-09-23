package org.thebubbleindex.model;

public class Sweden extends BubbleIndexTimeseries {
    
	protected Sweden() {}
	
	public Sweden(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
