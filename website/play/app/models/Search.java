package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class Search extends Model {
	public String query;
	public Search () {
		this.query = null;

	}
}
