package businessLogicLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import modelLayer.Cluster;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class Clustering {
	
	public static List<Cluster> tweetClustering (TweetStorage tweets, double facilityCost) {
		TweetStorage geotaggedTweets = tweets.getGeotaggedTweets();
		
		System.out.println("Start of clustering. Doing initial solution.");
		List<Cluster> clusters = initialSolution(geotaggedTweets, facilityCost);
		
		System.out.println("Initial solution done. Starting refinement process.");		
		// TODO I'm not sure if it's log of cluster size or clonedTweets size. The paper didn't make this clear.
		// Maybe move this loop to refineClusters. Should give more accurate updates of clusters, but worse performance.
		int iterations = (int) Math.log(clusters.size());
		for (int i = 0; i < iterations; i++) {
			System.out.println("Doing refinement step " + (i + 1) + " of " + iterations + ".");
			refineClusters(geotaggedTweets, geotaggedTweets, facilityCost, clusters);
		}
		
		System.out.println("Clustering done.");
		return clusters;
	}

	public static void updateClusters (List<Cluster> clusters, TweetStorage newTweets, TweetStorage allTweets, double facilityCost) {
		TweetStorage geotaggedTweets = newTweets.getGeotaggedTweets();
		for (Tweet t : geotaggedTweets) {
			t.setCluster(getNearestCluster(clusters, t));
		}
		refineClusters(geotaggedTweets, allTweets.getGeotaggedTweets(), facilityCost, clusters);
	}
	
	private static void refineClusters(TweetStorage newTweets, TweetStorage allTweets, double facilityCost, List<Cluster> clusters) {
		for (Tweet t : newTweets.getRandomizedCopy()) {
			checkGainAndReassign(t, clusters, allTweets, facilityCost);
		}
	}
	
	private static List<Cluster> initialSolution (TweetStorage tweets, double facilityCost) {
		TweetStorage randomizedTweets = tweets.getRandomizedCopy();
		Random rand = new Random();
		List<Cluster> clusters = new ArrayList<Cluster>();
		
		Cluster cluster = Cluster.createCluster(randomizedTweets.getFirst());
		clusters.add(cluster);
		
		for (int i = 1; i < randomizedTweets.size(); i++) {
			Tweet tweet = randomizedTweets.get(i);
			
			Cluster nearestCluster = getNearestCluster(clusters, tweet);
			double dist = getDist(nearestCluster.getCenter(), tweet);
			double prob = dist / facilityCost;

			double r = rand.nextDouble();
			if (r <= prob) {
				cluster = Cluster.createCluster(tweet);
				clusters.add(cluster);
			}
			else {
				Cluster.reassignTweet(tweet, nearestCluster);
			}
		}
		return clusters;
	}
	
	private static void checkGainAndReassign(Tweet tweet, List<Cluster> clusters, TweetStorage tweets, double facilityCost) {	
		TweetStorage reassignmentList = new TweetStorage();
		List<Cluster> removalList = new ArrayList<Cluster>();
		double gain = -facilityCost;
		
		for (Tweet t : tweets) {
			double d1 = getDist(t, t.getCluster().getCenter());
			double d2 = getDist(t, tweet);
			double dist =  d1 - d2;
			if (dist > 0) {
				gain += dist;
				reassignmentList.add(t);
			}
		}
		
		for (Cluster c : clusters) {
			double removalGain = facilityCost;
			TweetStorage tweetDifference = TweetStorage.getDifference(c.getTweets(), reassignmentList);
			for (Tweet t : tweetDifference) {
				double d1 = getDist(t, t.getCluster().getCenter());
				double d2 = getDist(t, tweet);
				removalGain +=  d1 - d2;
			}
			if (removalGain > 0) {
				gain += removalGain;
				removalList.add(c);
				reassignmentList.addAll(tweetDifference);
			}
		}
		
		if (gain > 0) {
			Cluster c = Cluster.createCluster(tweet);
			for (Tweet t : reassignmentList) {
				Cluster.reassignTweet(t, c);
			}
			clusters.removeAll(removalList);
			clusters.add(c);
		}
	}
	
	// TODO Maybe return both cluster and distance? And maybe private
	public static Cluster getNearestCluster(List<Cluster> clusters, Tweet tweet) {
		double dist = Double.POSITIVE_INFINITY;
		Cluster c = null;

		for (Cluster cluster : clusters) {
			double currentDist = getDist(cluster.getCenter(), tweet);
			if (currentDist < dist) {
				dist = currentDist;
				c = cluster;
			}
		}

		return c;
	}
	
	// TODO Maybe move distance methods to a different class
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
