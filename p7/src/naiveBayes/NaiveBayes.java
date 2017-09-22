package naiveBayes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

public abstract class NaiveBayes {
    public abstract void train(ArrayList<String> C, TweetStorage D);
    public abstract String apply(Tweet tweet) throws Exception;
    public abstract Map<String, Double> applyGetScore(ProbabilityModel probability, Tweet tweet) throws Exception;

    protected List<String> extractTokens(List<String> vocabulary, Tweet tweet) {
        String[] tweetWords = tweet.getTweetText().split(" ");
        List<String> vocabularyContained = new ArrayList<String>();
        for (int i = 0; i < tweetWords.length; i++) {
            for (String word : vocabulary) {
                if (tweetWords[i].equals(word))
                    vocabularyContained.add(tweetWords[i]);
            }
        }
        return vocabularyContained;
    }

    //bug: this doesnt consider upper and lowercase of the same words at the same
    protected List<String> extractVocabulary(TweetStorage tweets) {
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

    protected String concatenateTextOfAllTweetsInClass(TweetStorage tweets, String c) {
        String concatenatedText = "";
        for (Tweet tweet : tweets) {
            if (tweet.getExpectedClassLabel().equals(c))
                concatenatedText += tweet.getTweetText() + " ";
        }
        return concatenatedText.substring(0, concatenatedText.length() - 1);
    }
}
