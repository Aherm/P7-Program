package businessLogicLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import modelLayer.Probability;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class MultiNomialNB {
	
	//THE FUCK, WHY DO I GET DIFFERENT RESULTS IN C#, EVERYTHING IS THE SAME!?!?! (still returns the correct result, however, is this by chance???)
	public Probability trainMultinomialNB(String[] classVariables, TweetStorage tweets) {
		List<String> vocabulary = extractWords(tweets);
		for(String s : vocabulary)
		{
			System.out.print(s + " ");
		}
		System.out.print("\n");
		double numberOfTweets = tweets.size();
		double[] priorProbabilityOfClass = new double[classVariables.length];
		double[][] conditionalProbabilityOfTokenInClass = new double[vocabulary.size()][classVariables.length];	
		for(int c = 0; c < classVariables.length; c++)
		{
			System.out.println("Current class: " + classVariables[c]);
			double totalTokenOccurencesInClass = 0;
			double numberOfTweetsInClass = countTweetsInClass(tweets, classVariables[c]);
			priorProbabilityOfClass[c] = numberOfTweetsInClass / numberOfTweets;
			String textInClass = concatenateTextOfAllTweetsInClass(tweets, classVariables[c]);
			System.out.println("Text: " + textInClass);
			for(String token : vocabulary) {
				totalTokenOccurencesInClass += countTokensInTextInClass(textInClass, token) + 1;
			}
			for(int t = 0; t < vocabulary.size(); t++) {
				double numberOfTokenOccurencesInClass = countTokensInTextInClass(textInClass, vocabulary.get(t));
				conditionalProbabilityOfTokenInClass[t][c] = (numberOfTokenOccurencesInClass + 1) / totalTokenOccurencesInClass;
				System.out.println("Tokens      : " + (numberOfTokenOccurencesInClass + 1));
				System.out.println("Total tokens: " + totalTokenOccurencesInClass);
				System.out.println("Conditional : " + (numberOfTokenOccurencesInClass + 1) / totalTokenOccurencesInClass);
			}
		}

		return new Probability(vocabulary, priorProbabilityOfClass, conditionalProbabilityOfTokenInClass);
	}

	public String applyMultinomialNB(String[] classVariables, Probability probability, Tweet tweet) {
		List<String> tokensFromTweet = extractTokens(probability.getVocabulary(), tweet);
		Double[] scores = new Double[classVariables.length];
		double highestScore = Double.NEGATIVE_INFINITY;
		int highestScoreIndex = -1;
		for(int c = 0; c < classVariables.length; c++) {
			scores[c] = Math.log10(probability.getPriorProbability()[c]);
			System.out.println("\nPrior: " + probability.getPriorProbability()[c] + " for class: " + classVariables[c]);
			System.out.println("Log10 of Prior: " + Math.log10(probability.getPriorProbability()[c]) + " for class: " + classVariables[c] + "\n");
			for(int t = 0; t < tokensFromTweet.size(); t++) {
				scores[c] += Math.log10(probability.getConditionalProbability()[t][c]);
				System.out.println("CondProb: " + probability.getConditionalProbability()[t][c] + " for class: " + classVariables[c]);
				System.out.println("Log 10 of CondProb: " + Math.log10(probability.getConditionalProbability()[t][c]) + " for class: " + classVariables[c] + "\n");
			}

			if(scores[c] > highestScore)
			{
				highestScore = scores[c];
				highestScoreIndex = c;
			}
		}
		for(double score : scores)
		{
			System.out.println("Score: " + score);
		}
		return classVariables[highestScoreIndex];
	}

	private List<String> extractTokens(List<String> vocabulary, Tweet tweet) {
		String[] tweetWords = tweet.getTweetText().split(" ");
		List<String> newVocabulary = new ArrayList<String>();
		for(int w = 0; w < tweetWords.length; w++) {
			for(String word : vocabulary) {
				if(tweetWords[w].equals(word))
					newVocabulary.add(tweetWords[w]);
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
