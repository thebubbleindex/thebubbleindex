package org.thebubbleindex.model;

public class France extends BubbleIndexTimeseries {
	
	protected France() {}
	
	public France(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
