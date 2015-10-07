package businessLogicLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class Clustering {
	
	public List<Cluster> tweetClustering (TweetStorage tweets, double facilityCost) {
		List<Cluster> clusters = new ArrayList<Cluster>();
		clusters = initialSolution(tweets, facilityCost);
		
		// TODO I'm not sure if it's log of cluster size or tweets size. The paper didn't make this clear.
		for (int i = 0; i < Math.log(clusters.size()); i++) {
			// TODO tweets.randomize();
			for (int j = 0; j < tweets.size(); j++) {
				double gain = getGainAndReassign(tweets.get(j), clusters, tweets, facilityCost);
				if (gain > 0) {
					clusters.add(createCluster(tweets.get(j)));
					
					
				}
			}
		}
		
		return clusters;
	}
	
	public List<Cluster> initialSolution (TweetStorage tweets, double facilityCost) {		
		// TODO tweets.randomize();
		List<Cluster> clusters = new ArrayList<Cluster>();
		Tweet tweet = tweets.getFirst();
		
		Cluster cluster = createCluster(tweet);		
		clusters.add(cluster);
		
		Random rand = new Random();
		
		for (int i = 1; i < tweets.size(); i++) {
			tweet = tweets.get(i);
			
			double dist = getNearestCluster(clusters, tweet);
			double prob = dist / facilityCost;			
			
			double r = rand.nextDouble();
			if (r >= prob) {
				cluster = createCluster(tweet);
				clusters.add(cluster);
			}
		}
		return clusters;
	}
	
	// The gain is the largest decrease in facility + service costs if we add the tweet as a facility
	private static double getGainAndReassign(Tweet tweet, List<Cluster> clusters, TweetStorage tweets, double facilityCost) {
		double cost = -facilityCost;
		TweetStorage rl = new TweetStorage(); // Reassignment list
		
		for (int i = 0; i < tweets.size(); i++) {
			Tweet t = tweets.get(i);
			double dist = getDist(t, t.getCluster().getCenter()) - getDist(t, tweet);
			if (dist > 0) {
				cost += dist;
				rl.add(t);
			}
		}
		// TODO Needs to take into consideration that some empty clusters must be removed
		
		// TODO Perform reassignments and closures
		// Assign nodes to cluster j if their distance to it is closer than the distance to their currently assigned cluster
		// Remove clusters from the solution if it no longer has any tweets assigned to it
		
		return cost;
	}
	
	private static double getNearestCluster(List<Cluster> clusters, Tweet tweet) {
		double dist = Double.POSITIVE_INFINITY;
		
		for (int i = 0; i < clusters.size(); i++) {
			Cluster cluster = clusters.get(i);
			Double currentDist = getDist(cluster.getCenter(), tweet);
			if (currentDist < dist) {
				dist = currentDist;
				tweet.setCluster(cluster);
			}
		}
		
		tweet.getCluster().addTweet(tweet);
		
		return dist;
	}
	
	//TODO Maybe convert to meter. Currently uses Manhattan distance.
	public static double getDist(Tweet t1, Tweet t2) {
		double x1 = t1.getLon();
		double x2 = t2.getLon();
		double y1 = t1.getLat();
		double y2 = t2.getLat();
		
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}
	
	public Cluster createCluster(Tweet center) {
		if (center.getCluster() != null) {
			center.getCluster().removeTweet(center);
		}		
		Cluster c = new Cluster(center);
		center.setCluster(c);
		c.addTweet(center);
		
		return c;
	}
}
