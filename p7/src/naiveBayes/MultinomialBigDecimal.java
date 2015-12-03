package naiveBayes;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class MultinomialBigDecimal extends NaiveBayes {
    /**
     *
     * @param C : set of possible classes
     * @param D : the trainingset, aka the document set D, in this case a list of tweet
     * @return Probability object : the model used to later classify new stweets
     */
    public ProbabilityModelBigDecimal trainBigDecimal(ArrayList<String> C, TweetStorage D) {
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
                totalT_ct = totalT_ct.add(countTokensInTextInClass(text_c, t));
            //totalT_ct += countTokensInTextInClass(text_c, t) + 1; // this is an alternative to adding the vocab size at the computation

            for (String t : V) {
                BigDecimal T_ct = countTokensInTextInClass(text_c, t);
                condprob.put(new ArrayList<String>(Arrays.asList(t, c)),
                        (T_ct.add(new BigDecimal(1))).divide(totalT_ct.add(new BigDecimal(V.size())), 2, RoundingMode.HALF_UP));
            }
        }
        return new ProbabilityModelBigDecimal(V, prior, condprob);
    }

    /**
     *
     * @param C : set of possible classes
     * @param probability : the learned probabilistic model
     * @param tweet : is the tweet to classify
     * @return c : the class the provided tweet is set to
     */

    public String applyBigDecimal(ArrayList<String> C, ProbabilityModelBigDecimal probability, Tweet tweet) {
        Map<String, BigDecimal> score = new HashMap<String, BigDecimal>();
        List<String> W = extractTokens(probability.getVocabulary(), tweet);

        for (String c : C) {
            score.put(c, probability.getPriorProbability(c));
            for (String t : W) {
                score.put(c, score.get(c).multiply(probability.getConditionalProbability(t, c)));
            }
        }
        return classWHighestProbability(score);
    }

    public Tweet applyGetProbability(ArrayList<String> C, ProbabilityModelBigDecimal probability, Tweet tweet) {
        Map<String, BigDecimal> score = new HashMap<String, BigDecimal>();
        List<String> W = extractTokens(probability.getVocabulary(), tweet);

        for (String c : C) {
            score.put(c, probability.getPriorProbability(c));
            for (String t : W) {
                score.put(c, score.get(c).multiply(probability.getConditionalProbability(t, c)));
            }
        }

        for (Map.Entry<String, BigDecimal> entry : score.entrySet()){
            if (entry.getKey().equals("1"))
                tweet.setProbabilityTrue(entry.getValue());
        }
        return tweet;
    }

    protected BigDecimal countTweetsInClass(TweetStorage tweets, String c) {
        double counter = 0;
        for (Tweet tweet : tweets) {
            if (tweet.getExpectedClassLabel().equals(c))
                counter++;
        }
        return new BigDecimal(counter);
    }

    protected static String classWHighestProbability(Map<String, BigDecimal> score) {
        String c = null;
        for (Map.Entry<String, BigDecimal> entry : score.entrySet()) {
            BigDecimal value = entry.getValue();
            if (c == null)
                c = entry.getKey();
            else if (value.equals(score.get(c)))
                c = entry.getKey();
        }
        return c;
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


    @Override
    public ProbabilityModel train(ArrayList<String> C, TweetStorage D) {
        return null;
    }

    @Override
    public String apply(ArrayList<String> C, ProbabilityModel probability, Tweet tweet) {
        return null;
    }

    @Override
    public Map<String, Double> applyGetScore(ArrayList<String> C, ProbabilityModel probability, Tweet tweet) {
        return null;
    }

}
