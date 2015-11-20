package StableMarriage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DatingAgency {
	
	private HashMap<String, Man> men;
	private HashMap<String, Woman> women;
	
	public DatingAgency(HashMap<String, Man> men, HashMap<String, Woman> women) {
		this.men = men;
		this.women = women;
	}
	
	public void matching() {
		int PartnerCheck = 0;
		int engagedNum=0;
		
		//當兩邊都沒有完成配對
		while(engagedNum!=men.size()&&engagedNum!=women.size()){
			for (Object key : men.keySet()) { //讓key一一代表每個man
				//令manName字串是key的name
				String manName = (String)key;
				//令man物件是這個key的man
				Man man = men.get(manName);
				//令womanName字串是這個man最佳女伴
				String womanName = man.proposalGoal();
				//令woman物件是這個man最佳女伴
				Woman woman = women.get(womanName);
				//如果woman對此man有意願配對
				if(woman.evaluateProposal(manName)) {
					if(woman.partner != null){ //如果woman已有對象
						//先拆散，並扣engagedNum(配對成功數)
						men.get(woman.partner).partner = null;
						engagedNum--;
					}
					//將彼此的對象設為對方
					woman.partner = manName;	
					man.partner =  womanName;
					engagedNum++;
				}
		    }
		}			
	}
	
	public void printResult() {
		//讓key一一代表每個man
		for (Object key : men.keySet()) {
			//令manName字串是key的name
			String manName = (String)key;
			//print出配對結果
			System.out.println(manName+" <-> "+men.get(key).partner);
		}
	}
}
