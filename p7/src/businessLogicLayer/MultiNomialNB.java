package businessLogicLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import modelLayer.Probability;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class MultiNomialNB {
	public double ApplyMultinomialNB(String[] classVariables, Probability probability, Tweet tweet) {
		List<String> tokensFromTweet = extractTokens(probability.getVocabulary(), tweet);
		Double[] scores = new Double[classVariables.length];
		for(int i = 0; i < classVariables.length; i++) {
			scores[i] = Math.log10(probability.getPriorProbability()[0]);
			for(int n = 0; n < tokensFromTweet.size(); n++) {
				scores[i] += Math.log10(probability.getConditionalProbability()[n][i]);
			}
		}
		List<Double> temp = Arrays.asList(scores);
		return Collections.max(temp);
	}
	
	public Probability TrainMultinomialNB(String[] classVariables, TweetStorage tweets) {
		List<String> vocabulary = extractWords(tweets);
		double numberOfTweets = tweets.size();
		double[] priorProbabilityOfClass = new double[classVariables.length];
		double[][] conditionalProbabilityOfTokenInClass = new double[vocabulary.size()][classVariables.length];
		for(int i = 0; i < classVariables.length; i++)
		{
			double totalTokenOccurencesInClass = 0;
			double numberOfTweetsInClass = countTweetsInClass(tweets, classVariables[i]);
			priorProbabilityOfClass[i] = numberOfTweetsInClass / numberOfTweets;
			String textInClass = concatenateTextOfAllTweetsInClass(tweets, classVariables[i]);
			for(String token : vocabulary) {
				totalTokenOccurencesInClass += countTokensInTextInClass(textInClass, token) + 1;
			}
			for(int n = 0; n < vocabulary.size(); n++) {
				double numberOfTokenOccurencesInClass = countTokensInTextInClass(textInClass, vocabulary.get(n));
				conditionalProbabilityOfTokenInClass[n][i] = numberOfTokenOccurencesInClass / totalTokenOccurencesInClass;
			}
		}
		
		return new Probability(vocabulary, priorProbabilityOfClass, conditionalProbabilityOfTokenInClass);
	}
	
	private List<String> extractTokens(List<String> vocabulary, Tweet tweet) {
		String[] tweetWords = tweet.getTweetText().split(" ");
		List<String> newVocabulary = new ArrayList<String>();
		for(int i = 0; i < tweetWords.length; i++) {
			for(String word : vocabulary) {
				if(tweetWords[i].equals(word))
					newVocabulary.add(tweetWords[i]);
			}
		}
		
		return newVocabulary;
	}
	
	private List<String> extractWords(TweetStorage tweets) {
		List<String> vocabulary = new ArrayList<String>();
		for(Tweet tweet : tweets) {
			String[] tweetWords = tweet.getTweetText().split(" ");
			for(String tweetWord : tweetWords)
			{
				if(!(vocabulary.contains(tweetWord)))
					vocabulary.add(tweetWord);
			}
		}
		
		return vocabulary;
	}
	
	private double countTweetsInClass(TweetStorage tweets, String classVariable) {
		double counter = 0;
		for(Tweet tweet : tweets) {
			if(tweet.getClassLabel().equals(classVariable))
				counter++;
		}
		
		return counter;
	}
	
	private String concatenateTextOfAllTweetsInClass(TweetStorage tweets, String classVariable) {
		String concatenatedText = "";
		for(Tweet tweet : tweets) {
			if(tweet.getClassLabel().equals(classVariable))
				concatenatedText += tweet.getTweetText() + " ";
		}
		
		return concatenatedText.substring(0, concatenatedText.length() - 1);
	}
	
	private double countTokensInTextInClass(String text, String token) {
		double counter = 0;
		String[] words = text.split(" ");
		for(String word : words) {
			if(word.equals(token))
				counter++;
		}
		
		return counter;
	}
}
