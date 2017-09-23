package org.thebubbleindex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bubble_index_timeseries")
public class BubbleIndexTimeseries {

	public Long id;
	public String name;
	public String symbol;
	public String keywords;
	public String dtype;
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}
	
	@Column(name = "symbol")
	public String getSymbol() {
		return symbol;
	}

	@Column(name = "keywords")
	public String getKeywords() {
		return keywords;
	}

	@Column(name = "dtype")
	public String getDtype() {
		return dtype;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public void setName(final String name) {
		this.name = name;
	}

	public void setSymbol(final String symbol) {
		this.symbol = symbol;
	}

	public void setKeywords(final String keywords) {
		this.keywords = keywords;
	}

	public void setDtype(final String dtype) {
		this.dtype = dtype;
	}
}
