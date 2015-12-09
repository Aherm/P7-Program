package businessLogicLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelLayer.*;
import naiveBayes.MultinomialBigDecimal;
import naiveBayes.ProbabilityModelBigDecimal;

public class Scoring {
	public static Map<Restaurant,Double> geoScore = new HashMap<Restaurant,Double>();
	public static Map<Restaurant,Double> wordScore = new HashMap<Restaurant,Double>();
	public static Map<Restaurant,Double> combinedScore = new HashMap<Restaurant,Double>(); 
	private static Map<String,Integer> RestaurantNameCounter = new HashMap<String,Integer>(); 
	public  int   locTotalVisits = 0; 
	public  int   locTotalSickVisits = 0; 
	public  int   nameTotalVisits = 0; 
	public  int   nameTotalSickVisits = 0; 
 
	private void init(List<Restaurant> restaurants){
		for(Restaurant r : restaurants){
			if(RestaurantNameCounter.containsKey(r.getName())){
				  Integer value = RestaurantNameCounter.get(r.getName()); 
				  value = new Integer(value.intValue() + new Integer(1).intValue()); 
				  RestaurantNameCounter.put(r.getName(), value); 
			}
			else{
				RestaurantNameCounter.put(r.getName(), new Integer(1)); 
			}
			
			initScores(restaurants, geoScore);
			initScores(restaurants, wordScore);
			initScores(restaurants, combinedScore);
		}
		
	}
	
	private void initScores(List<Restaurant> restaurants, Map<Restaurant, Double> map){
		for(Restaurant r : restaurants){
			map.put(r, new Double(0)); 
		}
	}
	
	
	private double geotaggedScore(Restaurant r, Grid grid) {
		double result = 0;
		TweetStorage tweets = grid.rangeQuery(r, 25);
		TweetStorage sickTweets = tweets.getSickTweets();
		if(tweets.isEmpty()){
			return 0; 
		}
		
		for(Tweet t: tweets){
			t.setLocRes(r);
			if(t.getLocRes() != null){
				System.out.println("Changes restaurant oops");
			}
		}
		
		locTotalSickVisits += sickTweets.size(); 
		locTotalVisits += tweets.size(); 
		result = (double)sickTweets.size() / (double)tweets.size();
		
		geoScore.put(r, result);
		
		return result;
	}
	
	private double nameScore(Restaurant r, InvertedIndex ii) {
		
		double result = 0;
		TweetStorage tweets = ii.nameQuery(r);
		TweetStorage sickTweets = tweets.getSickTweets();
		
		if(tweets.isEmpty()){
			return 0; 
		}
		
		for(Tweet t: tweets){
			t.setNameRes(r);
		}
		nameTotalSickVisits += sickTweets.size(); 
		nameTotalVisits += tweets.size(); 
		
		double adjustedVisit = (double)tweets.size() / RestaurantNameCounter.get(r.getName()).doubleValue(); 
		double adjustedSickVisit = (double) sickTweets.size() / RestaurantNameCounter.get(r.getName()).doubleValue(); 
		result = adjustedSickVisit / adjustedVisit; 
		
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
	
	
	
	public void ScoreSystem(Grid grid, InvertedIndex ii,TweetStorage allTweets, List<Restaurant> allRestaurants){
		init(allRestaurants); 
		for(Restaurant r : allRestaurants ){
			geotaggedScore(r, grid); 
			nameScore(r, ii); 
		}
		
		combinedScore(allTweets); 
	}

	
	private void combinedScore(TweetStorage ts){
		for(Tweet t : ts){
			if(t.hasVisited()){
				if(t.getLocRes() == null)
					updateCounter(t.getNameRes());
				else if(t.getNameRes() == null)
					updateCounter(t.getLocRes());
				else if(!t.conflict())
					updateCounter(t.getLocRes()); 
				else{
					if(t.nameResWithin())
						updateCounter(t.getNameRes());
					else
						updateCounter(t.getLocRes());
					}
			}
		}
	}
	
	private void updateCounter(Restaurant r){
		Double value = combinedScore.get(r); 
		value = new Double(value.doubleValue() + 1);
		combinedScore.put(r, value); 
	}

}