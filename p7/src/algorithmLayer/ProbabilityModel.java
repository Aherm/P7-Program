package algorithmLayer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProbabilityModel {
	List<String> vocabulary;
	Map<String, BigDecimal> prior;
	Map<ArrayList<String>, BigDecimal> condprop;

	public ProbabilityModel(List<String> vocabulary, Map<String, BigDecimal> prior, Map<ArrayList<String>, BigDecimal> condprop) {
		this.vocabulary = vocabulary;
		this.prior = prior;
		this.condprop = condprop;
	}
	
	public List<String> getVocabulary() {
		return this.vocabulary;
	}
	
	public BigDecimal getPriorProbability(String c) {
		BigDecimal value = new BigDecimal(0);
		for (Map.Entry<String, BigDecimal> entry : this.prior.entrySet()){
			String key = entry.getKey();
			if (c.equals(key)){
				value = entry.getValue();
				return value;
			}
		}
		return value;
	}
	
	public BigDecimal getConditionalProbability(String t, String c) {
		BigDecimal value = new BigDecimal(0);
		for (Map.Entry<ArrayList<String>, BigDecimal> entry : this.condprop.entrySet()){
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
