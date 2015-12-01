package businessLogicLayer;

import java.util.HashMap;
import java.util.Map;

import modelLayer.Grid;
import modelLayer.InvertedIndex;
import modelLayer.Restaurant;
import modelLayer.TweetStorage;

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
		wordScore.put(r, result);;
		return result;
	}
	
	public static double combinedScore(Restaurant r, Grid grid, InvertedIndex ii) {
		if(!geoScore.containsKey(r)) {
			geotaggedScore(r, grid);
		}
		if(!wordScore.containsKey(r)) {
			keywordScore(r, ii);
		}
		double result = geoScore.get(r) + wordScore.get(r);
		return result;
	}
}