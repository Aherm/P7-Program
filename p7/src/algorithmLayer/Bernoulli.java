package algorithmLayer;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

import java.util.*;

public class Bernoulli extends NaiveBayes {

    @Override
    public ProbabilityModel train(ArrayList<String> C, TweetStorage D) {
        Map<String, Double> prior = new HashMap<String, Double>();
        Map<ArrayList<String>, Double> condprob = new HashMap<ArrayList<String>, Double>();

        List<String> V = extractVocabulary(D);
        double N = D.size();
        for (String c : C) {
            double N_c = countTweetsInClass(D, c);
            prior.put(c, N_c / N);
            for (String t : V) {
                double N_ct = countDocsInClassContainingTerm(D, c, t);
                condprob.put(new ArrayList<String>(Arrays.asList(t, c)), (N_ct + 1) / (N_c + 2));
            }
        }
        return new ProbabilityModel(V, prior, condprob);
    }

    @Override
    public String apply(ArrayList<String> C, ProbabilityModel probability, Tweet d) {
        Map<String, Double> score = new HashMap<String, Double>();
        List<String> V_d = extractTokens(probability.getVocabulary(), d);

        for (String c : C) {
            //score[c] <- log prior(c)
            score.put(c, Math.abs(Math.log10(probability.getPriorProbability(c))));
            for (String t : probability.getVocabulary()) {
                if (V_d.contains(t))
                    score.put(c, score.get(c) + Math.abs(Math.log10(probability.getConditionalProbability(t, c))));
                else
                    score.put(c, score.get(c) + Math.abs(Math.log10(1 - probability.getConditionalProbability(t, c))));
            }
        }
        // return the class with the highest probability value
        return classWHighestProbability(score);
    }
    
    @Override
    public Map<String, Double> applyGetProbability(ArrayList<String> C, ProbabilityModel probability, Tweet tweet) {
    	return new HashMap<String, Double>();
    }

    private static double countDocsInClassContainingTerm(TweetStorage D, String classLabel, String token){
        double counter = 0;
        for (Tweet tweet : D)
            if (tweet.getClassLabel().equals(classLabel) && tweet.getTweetText().contains(token))
                counter++;
        //return num docs of the class that contain token
        return counter;
    }
}
