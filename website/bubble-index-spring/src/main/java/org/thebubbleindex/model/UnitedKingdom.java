package org.thebubbleindex.model;

public class UnitedKingdom extends BubbleIndexTimeseries {
	
	protected UnitedKingdom() {}
    
	public UnitedKingdom(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
	
}
