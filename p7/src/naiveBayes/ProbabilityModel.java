package naiveBayes;

import Processing.Stopwords;

import java.util.*;

public class ProbabilityModel implements java.io.Serializable {
    List<String> vocabulary;
    Map<String, Double> prior;
    Stopwords stopwords = new Stopwords();

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
        for (Map.Entry<String, Double> entry : this.prior.entrySet()) {
            String key = entry.getKey();
            if (c.equals(key)) {
                value = entry.getValue();
                return value;
            }
        }
        return value;
    }

    public double getConditionalProbability(String t, String c) {
        double value = 0;
        for (Map.Entry<ArrayList<String>, Double> entry : this.condprop.entrySet()) {
            //arraylist of two elements, the token and the classLabel
            ArrayList<String> key = entry.getKey();
            String token = key.get(0);
            String classLabel = key.get(1);
            if (c.equals(classLabel) && t.equals(token)) {
                value = entry.getValue();
                return value;
            }
        }
        return value;
    }

    public void setVocabulary(List<String> v) {
        this.vocabulary = v;
    }

    public static List<ConditionalProbability> getTopKProbabilities(Map<ArrayList<String>, Double> conditionalProbabilities, int k) {
        // get top k
        //fill this with 10, save the token of the lowest and compare against it
        List<ConditionalProbability> allConditionalProbabilities = new ArrayList<ConditionalProbability>();
        List<ConditionalProbability> topK = new ArrayList<ConditionalProbability>();
        ConditionalProbability lowestCondProb = new ConditionalProbability("", "", 0);
        for (Map.Entry<ArrayList<String>, Double> entry : conditionalProbabilities.entrySet()) {
            ArrayList<String> key = entry.getKey();
            String token = key.get(0);
            String probCls = key.get(1);
            double probability = entry.getValue();
            ConditionalProbability condProb = new ConditionalProbability(token, probCls, probability);
            allConditionalProbabilities.add(condProb);
        }
        Collections.sort(allConditionalProbabilities, new ConditionalProbabilityCompare());
        for (int i = 0; i < k; i++) {
            topK.add(allConditionalProbabilities.get(i));
        }
        return topK;
    }

    public Map<ArrayList<String>, Double> getConditionalProbabilitiesForClass(String cls) {
        Map<ArrayList<String>, Double> condProbForCls = new HashMap<ArrayList<String>, Double>();
        for (Map.Entry<ArrayList<String>, Double> entry : condprop.entrySet()) {
            ArrayList<String> key = entry.getKey();
            double probability = entry.getValue();
            String token = key.get(0);
            String propCls = key.get(1);

            if (propCls.equals(cls)) {
                if (this.stopwords.contains(token))
                    continue;
                else
                    condProbForCls.put(key, probability);
            }
        }
        return condProbForCls;
    }

    public Stopwords getStopwords() {
        return stopwords;
    }

    public void setStopwords(Stopwords stopwords) {
        this.stopwords = stopwords;
    }
    /*
	}
	public void setPriorProbability(Map<String, Double> pp) {
		this.prior = pp;
	}

	public void setConditionalProbability (Map<ArrayList<String>, Double> cp) {
		this.condprop = cp;
	}
	*/
}
