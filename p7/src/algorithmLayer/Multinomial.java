package algorithmLayer;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

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
        Map<String, Double> prior = new HashMap<String, Double>();
        Map<ArrayList<String>, Double> condprob = new HashMap<ArrayList<String>, Double>();

        List<String> V = extractVocabulary(D);
        double N = D.size();
        for (String c : C) {
            double totalT_ct = 0;

            double N_c = countTweetsInClass(D, c);
            prior.put(c, N_c / N);
            String text_c = concatenateTextOfAllTweetsInClass(D, c);

            for (String t : V)
                totalT_ct += countTokensInTextInClass(text_c, t) + 1; // this could be done by simply instead adding the length of vocatbil

            for (String t : V) {
                double T_ct = countTokensInTextInClass(text_c, t) + 1;
                condprob.put(new ArrayList<String>(Arrays.asList(t, c)), T_ct / totalT_ct);
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
        List<String> W = extractTokens(probability.getVocabulary(), tweet);

        for (String c : C) {
            //score[c] <- log prior(c)
            score.put(c, Math.abs(Math.log10(probability.getPriorProbability(c))));
            for (String t : W) {
                //score[c] += log condprod[t][c]
                score.put(c, score.get(c) + Math.log10(probability.getConditionalProbability(t, c)));
            }
        }
        // return the class with the highest probability value
        return classWHighestProbability(score);
    }

    private double countTokensInTextInClass(String text, String token) {
        double counter = 0;
        String[] words = text.split(" ");
        for (String word : words) {
            if (word.equals(token))
                counter++;
        }
        return counter;
    }
}
