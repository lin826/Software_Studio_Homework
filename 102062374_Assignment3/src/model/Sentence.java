package model;

import java.util.ArrayList;
import java.util.List;

public class Sentence extends AbstractSentence{
	
	public void addElement(SentenceElement e)
	{
		this.elementList.add(e);
	}
	
		
	public int getElementsSize()
	{
		return this.elementList.size();
	}
	
	@Override
	public List<String> getRandomizedChineseSpans() {
		List<String> rndChiSpans = new ArrayList<String>();
		for(int index : this.randomIndexes()){
			rndChiSpans.add(this.elementList.get(index).getChi());
		}
		
		return rndChiSpans;
	}

	@Override
	public List<String> getRandomizedChineseSpansWithLevel(int level) {
		List<String> rndEngSpans = new ArrayList<String>();
//		int i=0;
		for(int index : this.randomIndexes()){
			rndEngSpans.add(this.elementList.get(index).getChi());
//			System.out.println(rndEngSpans.get(i));
//			i++;
		}
		
		return rndEngSpans;
	}

	@Override
	public int score(List<String> candidate) {
		// TODO Auto-generated method stub
		return 0;
	}

}
