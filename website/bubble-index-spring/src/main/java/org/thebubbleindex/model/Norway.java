package org.thebubbleindex.model;

public class Norway extends BubbleIndexTimeseries {
    
	protected Norway() {}
	
	public Norway(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
