package org.thebubbleindex.model;

public class Currencies extends BubbleIndexTimeseries {
	
	protected Currencies() {}
	
	public Currencies(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
