package businessLogicLayer;

import java.util.HashMap;
import java.util.Map;

import modelLayer.Restaurant;
import modelLayer.TweetStorage;

public class Scoring {
	private static Map<Restaurant,Double> geoScore = new HashMap<Restaurant,Double>();
	private static Map<Restaurant,Double> wordScore = new HashMap<Restaurant,Double>();
	
	public static double geotaggedScore(Restaurant r, TweetStorage tweets) {
		double result = 0;
		geoScore.put(r, result);
		return result;
	}
	
	public static double keywordScore(Restaurant r, TweetStorage tweets) {
		double result = 0;
		wordScore.put(r, result);;
		return result;
	}
	
	public static double combinedScore(Restaurant r, TweetStorage tweets) {
		double result = geoScore.get(r) + wordScore.get(r);
		return result;
	}
}
