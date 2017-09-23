package org.thebubbleindex.model;

public class Australia extends BubbleIndexTimeseries {
	
	protected Australia() {}
	
	public Australia(final String name, final String symbol, final String type, final String keywords) {
		this.symbol = symbol;
		this.type = type;		
		this.name = name;
		this.keywords = keywords;
	}
}
