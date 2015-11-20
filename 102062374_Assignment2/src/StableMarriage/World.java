package StableMarriage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class World {

	public static void main(String[] args) {
		// define Hashmap men and women
		// the key is name, value is Man object
		HashMap<String, Man> men = new HashMap<String, Man>();
		HashMap<String, Woman> women = new HashMap<String, Woman>();
		
		// create object Man
		Man man1 = new Man("David", new ArrayList<String>(Arrays.asList("Jane", "Wendy", "Joana")));
		Man man2 = new Man("Jack", new ArrayList<String>(Arrays.asList("Jane", "Alice", "Wendy")));
		Man man3 = new Man("James", new ArrayList<String>(Arrays.asList("Jane", "Joana", "Wendy")));
		// put man into Hashmap
		men.put("David", man1);
		men.put("Jack", man2);
		men.put("James", man3);
		
		//for Moman is the same process
		Woman woman1 = new Woman("Jane", new ArrayList<String>(Arrays.asList("James", "Jack", "David")));
		Woman woman2 = new Woman("Joana", new ArrayList<String>(Arrays.asList("David", "Jack", "James")));
		Woman woman3 = new Woman("Wendy", new ArrayList<String>(Arrays.asList("Jack", "David", "James")));
		Woman woman4 = new Woman("Alice", new ArrayList<String>(Arrays.asList("Jack", "David", "James")));
		
		women.put("Jane", woman1);
		women.put("Joana", woman2);
		women.put("Wendy", woman3);
		women.put("Alice", woman4);
				
		DatingAgency agency = new DatingAgency(men, women);
		agency.matching();
		agency.printResult();
	}
}
