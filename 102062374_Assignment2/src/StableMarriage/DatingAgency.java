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
		
		//����䳣�S�������t��
		while(engagedNum!=men.size()&&engagedNum!=women.size()){
			for (Object key : men.keySet()) { //��key�@�@�N��C��man
				//�OmanName�r��Okey��name
				String manName = (String)key;
				//�Oman����O�o��key��man
				Man man = men.get(manName);
				//�OwomanName�r��O�o��man�̨Τk��
				String womanName = man.proposalGoal();
				//�Owoman����O�o��man�̨Τk��
				Woman woman = women.get(womanName);
				//�p�Gwoman�惡man���N�@�t��
				if(woman.evaluateProposal(manName)) {
					if(woman.partner != null){ //�p�Gwoman�w����H
						//����A�æ�engagedNum(�t�令�\��)
						men.get(woman.partner).partner = null;
						engagedNum--;
					}
					//�N��������H�]�����
					woman.partner = manName;	
					man.partner =  womanName;
					engagedNum++;
				}
		    }
		}			
	}
	
	public void printResult() {
		//��key�@�@�N��C��man
		for (Object key : men.keySet()) {
			//�OmanName�r��Okey��name
			String manName = (String)key;
			//print�X�t�ﵲ�G
			System.out.println(manName+" <-> "+men.get(key).partner);
		}
	}
}
