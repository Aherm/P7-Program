package algorithmLayer;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;

public class Multinomial extends NaiveBayes {
    /**
     *
     * @param C : set of possible classes
     * @param D : the trainingset, aka the document set D, in this case a list of tweet
     * @return Probability object : the model used to later classify new stweets
     */
    @Override
    public ProbabilityModel train(ArrayList<String> C, TweetStorage D) {
        Map<String, BigDecimal> prior = new HashMap<String, BigDecimal>();
        Map<ArrayList<String>, BigDecimal> condprob = new HashMap<ArrayList<String>, BigDecimal>();

        List<String> V = extractVocabulary(D);
        BigDecimal N = new BigDecimal(D.size());
        for (String c : C) {
        	BigDecimal totalT_ct = new BigDecimal(0);

            BigDecimal N_c = countTweetsInClass(D, c);
            prior.put(c, N_c.divide(N));
            String text_c = concatenateTextOfAllTweetsInClass(D, c);

            for (String t : V)
                totalT_ct = totalT_ct.add(countTokensInTextInClass(text_c, t)) ;
            //totalT_ct += countTokensInTextInClass(text_c, t) + 1; // this is an alternative to adding the vocab size at the computation

            for (String t : V) {
                double T_ct = countTokensInTextInClass(text_c, t);
                //totalT_ct + |V|
                condprob.put(new ArrayList<String>(Arrays.asList(t, c)), (T_ct + 1) / (totalT_ct + V.size()));
            }
        }
        return new ProbabilityModel(V, prior, condprob);
    }

    /**
     *
     * @param C : set of possible classes
     * @param probability : the learned probabilistic model
     * @param tweet : is the tweet to classify
     * @return c : the class the provided tweet is set to
     */
    
    @Override
    public String apply(ArrayList<String> C, ProbabilityModel probability, Tweet tweet) {
        Map<String, Double> score = new HashMap<String, Double>();
       
        //consider whether this is right correctly. Store the same words more than once
        List<String> W = extractTokens(probability.getVocabulary(), tweet);

        for (String c : C) {
            //score[c] <- log prior(c)
            score.put(c, Math.log10(probability.getPriorProbability(c)));
            for (String t : W) {
                //score[c] += log condprod[t][c]
                score.put(c, score.get(c) + Math.log10(probability.getConditionalProbability(t, c)));
            }
        }
        // return the class with the highest probability value
        return classWHighestProbability(score);
    }

    public Map<String, Double> applyGetProbability(ArrayList<String> C, ProbabilityModel probability, Tweet tweet) {
        Map<String, BigDecimal> score = new HashMap<String, BigDecimal>();

        //consider whether this is right correctly. Store the same words more than once
        List<String> W = extractTokens(probability.getVocabulary(), tweet);

        for (String c : C) {
            //score[c] <- log prior(c)
            score.put(c, probability.getPriorProbability(c));
            for (String t : W) {
                //score[c] += log condprod[t][c]
                score.put(c, score.get(c).multiply(probability.getConditionalProbability(t, c)));
            }
        }
        // return the class with the highest probability value
        return score;
    }
    
    private BigDecimal countTokensInTextInClass(String text, String token) {
        double counter = 0;
        String[] words = text.split(" ");
        for (String word : words) {
            if (word.equals(token))
                counter++;
        }
        return new BigDecimal(counter);
    }
}
