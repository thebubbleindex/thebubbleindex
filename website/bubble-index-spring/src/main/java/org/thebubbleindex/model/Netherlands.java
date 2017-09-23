package org.thebubbleindex.model;

public class Netherlands extends BubbleIndexTimeseries {
	
	protected Netherlands() {}
	
	public Netherlands(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
