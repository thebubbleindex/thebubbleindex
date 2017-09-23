package org.thebubbleindex.model;

public class SouthKorea extends BubbleIndexTimeseries {
    
	protected SouthKorea() {}
	
	public SouthKorea(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
