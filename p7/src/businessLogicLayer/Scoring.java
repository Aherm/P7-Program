package businessLogicLayer;

import java.security.AllPermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelLayer.*;
import naiveBayes.MultinomialBigDecimal;
import naiveBayes.ProbabilityModelBigDecimal;
import utility.Constants;
import utility.Distance;

public class Scoring {
	public static Map<Restaurant,Double> geoScore = new HashMap<Restaurant,Double>();
	public static Map<Restaurant,Double> nameScore = new HashMap<Restaurant,Double>();
	public static Map<Restaurant,Double> combinedScore = new HashMap<Restaurant,Double>(); 
	private static Map<String,Integer> RestaurantNameCounter = new HashMap<String,Integer>(); 
	private static Map<String,List<Restaurant>> restaurantsWithSameName = new HashMap<String,List<Restaurant>>();
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
		}
		
		for(Restaurant r : restaurants){
			if(restaurantsWithSameName.containsKey(r.getName())){
				restaurantsWithSameName.get(r.getName()).add(r);
			}
			else{
				restaurantsWithSameName.put(r.getName(), new ArrayList<Restaurant>());
			}
		}
		initScores(restaurants, geoScore);
		initScores(restaurants, nameScore);
		initScores(restaurants, combinedScore);
		
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
			if(t.getLocRes() != null){
				System.out.println("Changed from " + t.getLocRes().getName() + " to " + r.getName());
			}
			t.setLocRes(r);
			
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
		result = calcScore(adjustedSickVisit, adjustedVisit); 
		
		nameScore.put(r, result);
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
		System.out.println("Starting init");
		init(allRestaurants); 
		System.out.println("starting geotagging score");
		for(Restaurant r : allRestaurants ){
			geotaggedScore(r, grid); 
			nameScore(r, ii); 
		}
		
	 
		
	
		
		combinedScore(allTweets,allRestaurants); 
		System.out.println("i am done now");
	}

	
	private void combinedScore(TweetStorage ts, List<Restaurant> restaurants){
		Map<Restaurant,TweetStorage> map = new HashMap<Restaurant,TweetStorage>();
		Map<String,TweetStorage> nameCounter = new HashMap<String,TweetStorage>();
		for(Restaurant r : restaurants){
			nameCounter.put(r.getName(), new TweetStorage());
		}
		//INIT MAP
		for(Restaurant r : restaurants){
			map.put(r, new TweetStorage()); 
		}
		
		for(Tweet t : ts){
			if(t.hasVisited()){
				if(t.getNameRes() == null)
					map.get(t.getLocRes()).add(t);
				else if(t.getLocRes() == null)
					nameCounter.get(t.getNameRes().getName()).add(t); 
				else if(t.conflict()){
					map.get(handleConflict(t)).add(t);
				}
				else
					map.get(t.getLocRes()).add(t);
			}		
		}
		for(Restaurant r : map.keySet()){
			TweetStorage tweets  = map.get(r);
			TweetStorage sickTweets = tweets.getSickTweets();
			if(!tweets.isEmpty()){
				double adjustedSick = 0;
				double adjustedNormal = 0; 
				if(!nameCounter.get(r.getName()).isEmpty()){
					double sick = nameCounter.get(r.getName()).getSickTweets().size(); 
					double normal = nameCounter.get(r.getName()).size();
					adjustedSick = sick / RestaurantNameCounter.get(r.getName()).doubleValue(); 
					adjustedNormal = normal / RestaurantNameCounter.get(r.getName()).doubleValue(); 
			}
			
				double results = calcScore((double)sickTweets.size() + adjustedSick,(double)tweets.size() + adjustedNormal);
				combinedScore.put(r, results);
			}
		}
	}
	
	private double calcScore(double sick, double normal){
		return  sick / normal;
	}
	
	
	private Restaurant handleConflict(Tweet t){
		//First case 
		for(Restaurant r : restaurantsWithSameName.get(t.getNameRes().getName())){
			if(t.getLocRes().equals(r))
				return r; 
		}
		//second case
		for(Restaurant r : restaurantsWithSameName.get(t.getNameRes().getName())){
			if(Distance.getDist(r,t) < Constants.restaurantDistance)
				return r; 
		}
		
		return t.getNameRes();	
	}

}