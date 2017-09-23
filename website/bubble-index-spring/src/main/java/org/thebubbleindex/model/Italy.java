package org.thebubbleindex.model;

import javax.persistence.*;

@Entity
public class Italy extends BubbleIndexTimeseries {
	    
	protected Italy() {}
	
	public Italy(final String name, final String symbol, final String type, final String keywords) {
		this.symbol = symbol;
		this.type = type;		
		this.name = name;
		this.keywords = keywords;
	}
}
