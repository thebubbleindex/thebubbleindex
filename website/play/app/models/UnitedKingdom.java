package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class UnitedKingdom extends Model {
	public String name;
	public String symbol;
	public String keywords;
    
	public UnitedKingdom(String name, String symbol, String keywords) {
		this.symbol = symbol;
		this.name = name;
		this.keywords = keywords;
	}
	
}
