package org.thebubbleindex.model;

import javax.persistence.*;

@Entity
public class Taiwan extends BubbleIndexTimeseries {
    
	protected Taiwan() {}
	
	public Taiwan(final String name, final String symbol, final String type, final String keywords) {
		this.symbol = symbol;
		this.type = type;		
		this.name = name;
		this.keywords = keywords;
	}
}
