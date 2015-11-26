package modelLayer;

import java.util.ArrayList;
import java.util.List;

public class Probability {
	List<String> vocabulary;
	double[] priorProbability;
	double[][] conditionalProbability;
	
	public Probability(List<String> vocabulary, double[] priorProbability, double[][] conditionalProbability) {
		this.vocabulary = vocabulary;
		this.priorProbability = priorProbability;
		this.conditionalProbability = conditionalProbability; 
	}
	
	public List<String> getVocabulary() {
		return this.vocabulary;
	}
	
	public double[] getPriorProbability() {
		return this.priorProbability;
	}
	
	public double[][] getConditionalProbability() {
		return this.conditionalProbability;
	}
	
	public void setVocabulary(List<String> v) {
		this.vocabulary = v;
	}
	
	public void setPriorProbability(double[] pp) {
		this.priorProbability = pp;
	}
	
	public void setConditionalProbability (double[][] cp) {
		this.conditionalProbability = cp;
	}
}
