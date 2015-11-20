package visualization;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.googlecode.jcsv.annotations.MapToColumn;

public class Country {
	// <country name, immigrate people>
	private String countryName;
	private HashMap<String, Integer> immigrate = new HashMap<String, Integer>();
	private HashMap<String, Integer> emigration = new HashMap<String, Integer>();
	private int people=0;
	
	public Country(String firstname) {
		this.countryName = firstname;
	}
	
	public void putImmigrate(String string, String string2) {
		immigrate.put(string, Integer.parseInt(string2) );
	}
	
	public void putEmigration(String string, String string2) {
		emigration.put(string, Integer.parseInt(string2) );
	}
	
	public int getPeople(){
		people=0;
		for(int plus :immigrate.values())
			people += plus;
		//System.out.println(countryName+" : "+people);
		for(int minus :emigration.values())
			people -= minus;
		//System.out.println(countryName+" : "+people); 
		return people;
	}

	public Set<String> getEmigrationName() {
		return emigration.keySet();
	}

	public int getEmigration(String out) {
		return emigration.get(out);
	}
	
	public Set<String> getImmigrateName() {
		return immigrate.keySet();
	}

	public int getImmigrate(String out) {
		return immigrate.get(out);
	}


	// getter, equals, toString, ...

	// private HashMap<String, Integer> immigration = new HashMap<String,
	// Integer>();
	// private HashMap<String, Integer> emigration = new HashMap<String,
	// Integer>();
}
