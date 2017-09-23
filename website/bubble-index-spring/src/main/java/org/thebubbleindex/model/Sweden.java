package org.thebubbleindex.model;

import javax.persistence.*;

@Entity
public class Sweden extends BubbleIndexTimeseries {
    
	protected Sweden() {}
	
	public Sweden(final String name, final String symbol, final String type, final String keywords) {
		this.symbol = symbol;
		this.type = type;		
		this.name = name;
		this.keywords = keywords;
	}
}
