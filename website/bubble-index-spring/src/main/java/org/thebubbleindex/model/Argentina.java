package org.thebubbleindex.model;

public class Argentina extends BubbleIndexTimeseries {
    
	protected Argentina() {}
	
	public Argentina(final String name, final String symbol, final String type, final String keywords) {
		this.symbol = symbol;
		this.type = type;		
		this.name = name;
		this.keywords = keywords;
	}
}
