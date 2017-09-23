package org.thebubbleindex.model;

public class Russia extends BubbleIndexTimeseries {
    
	protected Russia() {}
	
	public Russia(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
