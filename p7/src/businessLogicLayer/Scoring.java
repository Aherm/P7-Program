package businessLogicLayer;

import modelLayer.Restaurant;
import modelLayer.TweetStorage;

public class Scoring {
	private static double geoScore;
	private static double wordScore;
	
	public static double geotaggedScore(Restaurant r, TweetStorage tweets) {
		double result = 0;
		geoScore = result;
		return result;
	}
	
	public static double keywordScore(Restaurant r, TweetStorage tweets) {
		double result = 0;
		wordScore = result;
		return result;
	}
	
	public static double combinedScore(Restaurant r, TweetStorage tweets) {
		double result = geoScore + wordScore;
		return result;
	}
}
