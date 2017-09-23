package org.thebubbleindex.model;

public class CompositeNinety extends BubbleIndexTimeseries {
    
	protected CompositeNinety() {}
	
	public CompositeNinety(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
