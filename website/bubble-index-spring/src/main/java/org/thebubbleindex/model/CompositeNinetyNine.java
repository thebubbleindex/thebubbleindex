package org.thebubbleindex.model;

public class CompositeNinetyNine extends BubbleIndexTimeseries {
    
	protected CompositeNinetyNine() {}
	
	public CompositeNinetyNine(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
