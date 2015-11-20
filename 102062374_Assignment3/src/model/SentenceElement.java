package model;

import java.util.List;

public class SentenceElement extends AbstractSentence {
	int valueField;
	String engField;
	String chiField;
	
	public SentenceElement(String valueField, String engField, String chiField) {
		this.valueField = Integer.parseInt(valueField);
		this.engField = engField;
		this.chiField = chiField;
	}
	
	public String getEng(){ return engField;}
	public String getChi(){ return chiField;}
	public int getValue(){ return valueField;}

	@Override
	public List<String> getRandomizedChineseSpans() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getRandomizedChineseSpansWithLevel(int level) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int score(List<String> candidate) {
		// TODO Auto-generated method stub
		return 0;
	}
}
