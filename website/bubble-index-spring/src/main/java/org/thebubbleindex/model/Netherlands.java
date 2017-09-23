package org.thebubbleindex.model;

import javax.persistence.*;

@Entity
public class Netherlands extends BubbleIndexTimeseries {
	
	protected Netherlands() {}
	
	public Netherlands(final String name, final String symbol, final String type, final String keywords) {
		this.symbol = symbol;
		this.type = type;		
		this.name = name;
		this.keywords = keywords;
	}
}
