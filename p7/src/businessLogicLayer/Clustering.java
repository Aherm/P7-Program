package businessLogicLayer;

import java.util.Random;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class Clustering {
	public TweetStorage initialSolution (TweetStorage tweets, double facilityCost) {
		TweetStorage res = new TweetStorage();
		//TweetStorage.Randomize()
		res.add(tweets.getFirst());
		Random rand = new Random();
		for (int i = 1; i < tweets.size(); i++) {
			Tweet tweet = tweets.get(i);
			
			double dist = getNearestDist(res, tweet);
			double prob = dist / facilityCost;			
			
			double r = rand.nextDouble();
			if (r >= prob) {
				res.add(tweet);
			}
		}
		return res;
	}
	
	private static double getNearestDist (TweetStorage tweets, Tweet tweet) {
		double dist = Double.POSITIVE_INFINITY;
		
		for (int i = 0; i < tweets.size(); i++) {
			Tweet cent = tweets.get(i);
			Double currentDist = getDist(cent, tweet);
			if (currentDist < dist) {
				dist = currentDist;
			}
		}
		
		return dist;
	}
	
	//Maybe convert to meter. Currently uses manhattan distance.
	public static double getDist (Tweet t1, Tweet t2) {
		double x1 = t1.getLon();
		double x2 = t2.getLon();
		double y1 = t1.getLat();
		double y2 = t2.getLat();
		
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}
}
