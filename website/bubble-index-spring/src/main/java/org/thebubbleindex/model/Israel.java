package org.thebubbleindex.model;

public class Israel extends BubbleIndexTimeseries {
    
	protected Israel() {}
	
	public Israel(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
