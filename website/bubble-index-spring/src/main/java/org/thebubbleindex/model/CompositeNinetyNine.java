package org.thebubbleindex.model;

import javax.persistence.*;

@Entity
public class CompositeNinetyNine extends BubbleIndexTimeseries {
    
	protected CompositeNinetyNine() {}
	
	public CompositeNinetyNine(final String name, final String symbol, final String type, final String keywords) {
		this.symbol = symbol;
		this.type = type;		
		this.name = name;
		this.keywords = keywords;
	}
}
