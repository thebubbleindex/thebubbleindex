package org.thebubbleindex.model;

public class Germany extends BubbleIndexTimeseries {

	protected Germany() {}
	
	public Germany(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
	
}
