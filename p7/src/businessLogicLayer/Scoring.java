package businessLogicLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import modelLayer.*;
import naiveBayes.MultinomialBigDecimal;
import naiveBayes.ProbabilityModelBigDecimal;

public class Scoring {
	private static Map<Restaurant,Double> geoScore = new HashMap<Restaurant,Double>();
	private static Map<Restaurant,Double> wordScore = new HashMap<Restaurant,Double>();
	
	public static double geotaggedScore(Restaurant r, Grid grid) {
		double result = 0;
		TweetStorage tweets = grid.rangeQuery(r, 25);
		TweetStorage sickTweets = tweets.getSickTweets();
		
		result = sickTweets.size() / tweets.size();
		
		geoScore.put(r, result);
		return result;
	}
	
	public static double keywordScore(Restaurant r, InvertedIndex ii) {
		double result = 0;
		TweetStorage tweets = ii.nameQuery(r);
		//TODO: Put tweets through classification
		TweetStorage sickTweets = tweets.getSickTweets();
		
		result = sickTweets.size() / tweets.size();
		
		wordScore.put(r, result);;
		return result;
	}

	private static TweetStorage filterVisitedTweets(ProbabilityModelBigDecimal classifier, TweetStorage tweetsToClassify){
		return getVisitedTweets(classifyTweets(classifier, tweetsToClassify));
	}

	private static TweetStorage classifyTweets(ProbabilityModelBigDecimal classifier, TweetStorage tweetsToClassify){
		TweetStorage classificationResults = new TweetStorage();
		MultinomialBigDecimal multinomialNB = new MultinomialBigDecimal();
		for (Tweet tweet : tweetsToClassify) {
			//String predictedClass = multinomialNB.applyBigDecimal(classLabels, classifier, tweet);
			Tweet classifiedTweet = multinomialNB.applyGetProbability(new ArrayList<String>(Arrays.asList("0","1")), classifier, tweet);
			classificationResults.add(classifiedTweet);
		}
		return classificationResults;
	}

	private static TweetStorage getVisitedTweets(TweetStorage tweets){
		TweetStorage tS = new TweetStorage();
		for (Tweet t : tweets)
			if (t.getAssignedClassLabel().equals("1"))
				tS.add(t);
		return tS;
	}

	public static double combinedScore(Restaurant r, Grid grid, InvertedIndex ii) {
		double result = 0;
		TweetStorage geoTweets = grid.rangeQuery(r, 25);
		TweetStorage wordTweets = ii.nameQuery(r);

		//Need find a way to store the learned classifier so that it doesn't need to be trained each time the program is run
		ProbabilityModelBigDecimal classifier = new ProbabilityModelBigDecimal();
		TweetStorage visitedTweets = filterVisitedTweets(classifier, wordTweets);

		TweetStorage tweets = TweetStorage.getUnion(geoTweets, visitedTweets);
		TweetStorage sickTweets = tweets.getSickTweets();
		
		result = sickTweets.size() / tweets.size();
		
		return result;
	}
}