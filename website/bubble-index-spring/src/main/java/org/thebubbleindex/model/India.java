package org.thebubbleindex.model;

public class India extends BubbleIndexTimeseries {

	protected India() {}
	
	public India(final String name, final String symbol, final String dtype, final String keywords) {
		this.symbol = symbol;
		this.dtype = dtype;		
		this.name = name;
		this.keywords = keywords;
	}
}
