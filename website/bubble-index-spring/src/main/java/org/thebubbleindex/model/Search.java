package org.thebubbleindex.model;

import javax.persistence.*;

@Entity
@Table(name = "search_query")
public class Search {
	
	public Long id;
	public String query;
	
	public Search () {
		this.query = null;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	@Column(name = "query")
	public String getQuery() {
		return query;
	}
	
	public void setQuery(final String query) {
		this.query = query;
	}
}
