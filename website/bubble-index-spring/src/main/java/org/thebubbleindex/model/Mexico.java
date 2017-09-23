package org.thebubbleindex.model;

public class Mexico extends BubbleIndexTimeseries {
    
	protected Mexico() {}
	
	public Mexico(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
