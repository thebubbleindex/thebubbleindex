package org.thebubbleindex.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BubbleIndexTimeseries {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
	public String name;
	public String symbol;
	public String keywords;
	public String type;
}
