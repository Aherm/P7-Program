package algorithmLayer;

import modelLayer.Probability;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

import java.util.*;

public class MultinomialNBUpdate {

    /**
     *
     * @param C : set of possible classes
     * @param D : the trainingset, aka the document set D, in this case a list of tweet
     * @return Probability object : the model used to later classify new stweets
     */
    //Possible issue: All tweets in the tweetstorage needs to be associated to a class
    public ProbabilityUpdate trainMultinomialNB(ArrayList<String> C, TweetStorage D) {
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
        return new ProbabilityUpdate(V, prior, condprob);
    }

    /**
     *
     * @param C : set of possible classes
     * @param probability : the learned probabilistic model
     * @param tweet : is the tweet to classify
     * @return c : the class the provided tweet is set to
     */
    public String applyMultinomialNB(String[] C, ProbabilityUpdate probability, Tweet tweet) {
        Map<String, Double> score = new HashMap<String, Double>();
        List<String> W = extractTokens(probability.getVocabulary(), tweet);

        for (String c : C) {
            score.put(c, Math.log10(probability.getPriorProbability(c)));
            for (String t : W) {
                score.put(c, probability.getPriorProbability(c) + Math.log10(probability.getConditionalProbability(t, c)));
            }
        }
        // return the class with the highest probability value
        return classWHighestProbability(score);
    }

    private List<String> extractTokens(List<String> vocabulary, Tweet tweet) {
        String[] tweetWords = tweet.getTweetText().split(" ");
        List<String> newVocabulary = new ArrayList<String>();
        for (int i = 0; i < tweetWords.length; i++) {
            for (String word : vocabulary) {
                if (tweetWords[i].equals(word))
                    newVocabulary.add(tweetWords[i]);
            }
        }

        return newVocabulary;
    }

    private List<String> extractVocabulary(TweetStorage tweets) {
        List<String> vocabulary = new ArrayList<String>();
        for (Tweet tweet : tweets) {
            String[] tweetWords = tweet.getTweetText().split(" ");
            for (String tweetWord : tweetWords) {
                if (!(vocabulary.contains(tweetWord)))
                    vocabulary.add(tweetWord);
            }
        }

        return vocabulary;
    }

    private double countTweetsInClass(TweetStorage tweets, String c) {
        double counter = 0;
        for (Tweet tweet : tweets) {
            if (tweet.getClassLabel().equals(c))
                counter++;
        }
        return counter;
    }

    private String concatenateTextOfAllTweetsInClass(TweetStorage tweets, String c) {
        String concatenatedText = "";
        for (Tweet tweet : tweets) {
            if (tweet.getClassLabel().equals(c))
                concatenatedText += tweet.getTweetText() + " ";
        }
        return concatenatedText.substring(0, concatenatedText.length() - 1);
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

    private static String classWHighestProbability(Map<String, Double> score) {
        //Map<String, Double> curBest = new HashMap<String, Double>();
        String c = null;
        for (Map.Entry<String, Double> entry : score.entrySet()) {
            if (c == null)
                c = entry.getKey();
            else if (entry.getValue() > score.get(c))
                c = entry.getKey();
        }
        return c;
    }

}
