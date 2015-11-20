package StableMarriage;

import java.util.ArrayList;

public class Person {
	
	private String name;
	private ArrayList<String> priorities = new ArrayList<String>();
	public String partner = null;
	
	public Person(String name, ArrayList<String> priorities) {
		this.name  = name;
		this.setPriorities(priorities);
	}

	public ArrayList<String> getPriorities() {
		return priorities;
	}

	public void setPriorities(ArrayList<String> priorities) {
		this.priorities = priorities;
	}
	
	//取出某志願序內的名字(String)
	public String getName(int i){
		return priorities.get(i);		
	}

}
