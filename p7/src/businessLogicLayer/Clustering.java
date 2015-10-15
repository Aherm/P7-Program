package businessLogicLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import modelLayer.Cluster;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class Clustering {
	
	public static List<Cluster> tweetClustering (TweetStorage tweets, double facilityCost) {
		System.out.println("Start of clustering. Doing initial solution.");
		List<Cluster> clusters = initialSolution(tweets, facilityCost);
		System.out.println("Initial solution done. Starting refinement process.");
		
		// TODO I'm not sure if it's log of cluster size or clonedTweets size. The paper didn't make this clear.
		// Maybe move this loop to refineClusters. Should give more accurate updates of clusters, but worse performance.
		double iterations = Math.log(clusters.size());
		for (int i = 0; i < iterations; i++) {
			System.out.println("Doing refinement step " + (i + 1) + " of " + (int)iterations + ".");
			refineClusters(tweets, tweets, facilityCost, clusters);
		}
		System.out.println("Clustering done.");
		return clusters;
	}

	public static void updateClusters (List<Cluster> clusters, TweetStorage newTweets, TweetStorage tweets, double facilityCost) {
		for (Tweet t : newTweets) {
			getNearestCluster(clusters, t);
		}
		refineClusters(newTweets, tweets, facilityCost, clusters);
	}
	
	private static void refineClusters(TweetStorage newTweets, TweetStorage allTweets, double facilityCost, List<Cluster> clusters) {
		
		// Checks for all new tweets if the solution would be better if a new cluster was made there.
		for (Tweet t : newTweets.getRandomizedCopy()) {
			checkGainAndReassign(t, clusters, allTweets, facilityCost);
		}
	}
	
	// Gets an initial cluster. Assigns a tweet to the nearest cluster, and randomly creates new clusters based on distance.
	private static List<Cluster> initialSolution (TweetStorage tweets, double facilityCost) {
		TweetStorage randomizedTweets = tweets.getRandomizedCopy();
		Random rand = new Random();
		List<Cluster> clusters = new ArrayList<Cluster>();
		
		Cluster cluster = Cluster.createCluster(randomizedTweets.getFirst());
		clusters.add(cluster);
		
		for (int i = 1; i < randomizedTweets.size(); i++) {
			Tweet tweet = randomizedTweets.get(i);
			
			double dist = getNearestCluster(clusters, tweet);
			double prob = dist / facilityCost;
			
			double r = rand.nextDouble();
			if (r <= prob) {
				cluster = Cluster.createCluster(tweet);
				clusters.add(cluster);
			}
		}
		return clusters;
	}
	
	// Check the gain for creating a new cluster with a center at tweet, and reassigning all other tweets.
	// Gain is based on the cost of creating a new cluster and total distance from tweets to their cluster.
	private static double checkGainAndReassign(Tweet tweet, List<Cluster> clusters, TweetStorage tweets, double facilityCost) {

		// The gain from creating a new cluster with tweet as center is the sum of distance-decreases after
		// reassigning tweets that are closer to tweet than their cluster, minus the facility cost.		
		TweetStorage reassignmentList = new TweetStorage();
		List<Cluster> removalList = new ArrayList<Cluster>();
		double gain = -facilityCost;
		
		for (Tweet t : tweets) {
			double dist = getDist(t, t.getCluster().getCenter()) - getDist(t, tweet);
			if (dist > 0) {
				gain += dist;
				reassignmentList.add(t);
			}
		}
		
		// It may increase gain to remove some clusters as well
		for (Cluster c : clusters) {
			double removalGain = facilityCost;
			for (Tweet t : c.getTweets().getDifference(reassignmentList)) {
				removalGain += getDist(t, t.getCluster().getCenter()) - getDist(t, tweet);
			}
			if (removalGain > 0) {
				gain += removalGain;
				removalList.add(c);
				reassignmentList.addAll(c.getTweets());
			}
		}
		
		// If gain is greater than 0, reassign all tweets to the new cluster and remove some clusters
		if (gain > 0) {
			Cluster c = Cluster.createCluster(tweet);
			for (Tweet t : reassignmentList) {
				Cluster.reassignTweet(t, c);
			}
			clusters.removeAll(removalList);
			clusters.add(c);
		}

		return gain;
	}
	
	public static double getNearestCluster(List<Cluster> clusters, Tweet tweet) {
		double dist = Double.POSITIVE_INFINITY;
		
		for (Cluster cluster : clusters) {
			double currentDist = getDist(cluster.getCenter(), tweet);
			if (currentDist < dist) {
				dist = currentDist;
				tweet.setCluster(cluster);
			}
		}
		
		tweet.getCluster().addTweet(tweet);
		
		return dist;
	}
	
	public static double getDist(Tweet t1, Tweet t2) {
		double R = 6371;	//Earths radius
		double deltaLat = toRadians(t2.getLat() - t1.getLat());
		double deltaLon = toRadians(t2.getLon() - t1.getLon());
		double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
				Math.cos(toRadians(t1.getLat())) * Math.cos(toRadians(t2.getLat())) *
				Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
		
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return (R * c) * 1000;
	}
	
	private static double toRadians(double degree)
	{
		return degree * Math.PI / 180;
	}
}
