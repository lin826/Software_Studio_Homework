package StableMarriage;

import java.util.ArrayList;

public class Man extends Person {

	private int proposal_index = 0;
	
	public Man(String name, ArrayList<String> priorities) {
		super(name, priorities);
	}
	
	// change woman to propose according to the proposal_index
	public String proposalGoal() {
		return this.getPriorities().get(proposal_index++);
	}

}
