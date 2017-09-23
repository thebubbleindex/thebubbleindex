package org.thebubbleindex.model;

import javax.persistence.Entity;

@Entity
public class UnitedKingdom extends BubbleIndexTimeseries {
	
	protected UnitedKingdom() {}
    
	public UnitedKingdom(final String name, final String symbol, final String type, final String keywords) {
		this.symbol = symbol;
		this.type = type;		
		this.name = name;
		this.keywords = keywords;
	}
	
}
