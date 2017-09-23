package org.thebubbleindex.model;

public class NewZealand extends BubbleIndexTimeseries {
	    
	protected NewZealand() {}
	
	public NewZealand(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
