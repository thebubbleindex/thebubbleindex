package org.thebubbleindex.model;

import javax.persistence.*;

@Entity
public class Search {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
	public String query;
	public Search () {
		this.query = null;
	}
}
