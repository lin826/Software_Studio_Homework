package StableMarriage;

import java.util.ArrayList;
import java.util.HashMap;

public class Woman extends Person {

	// a hashmap to record the man's name and their rank
	private HashMap<String, Integer> ranking = new HashMap<String, Integer>();
	
	// rank the priorities
	public Woman(String name, ArrayList<String> priorities) {
		super(name, priorities);
		for (int i = 0; i < priorities.size(); i++) {
			ranking.put(priorities.get(i), i);
		}
	}
	
	// check the woman has a partner or not 
	public boolean hasPartner() {
		return (this.partner != null);
	}
	
	// compare the rank between current partner and new suitor
	public boolean evaluateProposal(String suitor) {
		if (this.partner == null) return true;
		return (this.ranking.get(suitor) < this.ranking.get(this.partner));
	}
}
