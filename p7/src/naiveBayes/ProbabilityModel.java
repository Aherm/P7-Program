package naiveBayes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProbabilityModel {
	List<String> vocabulary;
	Map<String, Double> prior;
	Map<ArrayList<String>, Double> condprop;

	public ProbabilityModel(List<String> vocabulary, Map<String, Double> prior, Map<ArrayList<String>, Double> condprop) {
		this.vocabulary = vocabulary;
		this.prior = prior;
		this.condprop = condprop;
	}
	
	public List<String> getVocabulary() {
		return this.vocabulary;
	}
	
	public double getPriorProbability(String c) {
		double value = 0;
		for (Map.Entry<String, Double> entry : this.prior.entrySet()){
			String key = entry.getKey();
			if (c.equals(key)){
				value = entry.getValue();
				return value;
			}
		}
		return value;
	}
	
	public double getConditionalProbability(String t, String c) {
		double value = 0;
		for (Map.Entry<ArrayList<String>, Double> entry : this.condprop.entrySet()){
			//arraylist of two elements, the token and the classLabel
			ArrayList<String> key = entry.getKey();
			String token = key.get(0);
			String classLabel = key.get(1);
			if (c.equals(classLabel) && t.equals(token)){
				value = entry.getValue();
				return value;
			}
		}
		return value;
	}
	
	public void setVocabulary(List<String> v) {
		this.vocabulary = v;
	}

	/*
	public void setPriorProbability(Map<String, Double> pp) {
		this.prior = pp;
	}

	public void setConditionalProbability (Map<ArrayList<String>, Double> cp) {
		this.condprop = cp;
	}
	*/
}
