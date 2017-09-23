package org.thebubbleindex.model;

public class CompositeNinetyFive extends BubbleIndexTimeseries {
    
	protected CompositeNinetyFive() {}
	
	public CompositeNinetyFive(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
