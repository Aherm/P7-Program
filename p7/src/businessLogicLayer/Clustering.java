package businessLogicLayer;

import modelLayer.Cluster;
import modelLayer.ClusterStorage;
import modelLayer.Tweet;
import modelLayer.TweetStorage;
import java.util.Random;

public class Clustering {

	public static ClusterStorage clusterTweets (TweetStorage tweets, double facilityCost) {
		TweetStorage geotaggedTweets = tweets.getGeotaggedTweets();

		System.out.println("Start of clustering. Doing initial solution.");
		ClusterStorage clusters = initialSolution(geotaggedTweets, facilityCost);

		System.out.println("Initial solution done. Starting refinement process.");
		refineClusters(clusters, geotaggedTweets, geotaggedTweets, facilityCost);

		System.out.println("Clustering done.");
		return clusters;
	}

	public static void updateClusters (ClusterStorage clusters, TweetStorage tweets, double facilityCost) {
		TweetStorage newTweets = tweets.getGeotaggedTweets().getUnclusteredTweets();
		for (Tweet t : newTweets) {
			Cluster c = getNearestCluster(clusters, t);
			c.addTweet(t);
		}
		
		refineClusters(clusters, tweets, newTweets, facilityCost);
	}

	private static void refineClusters(ClusterStorage clusters, TweetStorage allTweets, TweetStorage newTweets, double facilityCost) {
		// TODO I'm not sure if it's log of cluster size or clonedTweets size. The paper didn't make this clear.
		int iterations = (int) Math.ceil(Math.log(clusters.size()));
		for (int i = 0; i < iterations; i++) {
			System.out.println("Doing refinement step " + (i + 1) + " of " + iterations + ".");
			for (Tweet t : newTweets.getRandomizedCopy()) {
				checkGainAndReassign(clusters, allTweets, t, facilityCost);
			}
		}
	}

	private static ClusterStorage initialSolution (TweetStorage tweets, double facilityCost) {
		TweetStorage randomizedTweets = tweets.getRandomizedCopy();
		Random rand = new Random();
		ClusterStorage clusters = new ClusterStorage();

		Cluster cluster = new Cluster(randomizedTweets.get(0));
		clusters.add(cluster);

		for (int i = 1; i < randomizedTweets.size(); i++) {
			Tweet tweet = randomizedTweets.get(i);

			Cluster nearestCluster = getNearestCluster(clusters, tweet);
			double dist = getDist(nearestCluster.getCenter(), tweet);
			double prob = dist / facilityCost;

			double r = rand.nextDouble();
			if (r <= prob) {
				cluster = new Cluster(tweet);
				clusters.add(cluster);
			}
			else {
				nearestCluster.addTweet(tweet);
			}
		}
		
		return clusters;
	}

	private static void checkGainAndReassign(ClusterStorage clusters, TweetStorage tweets, Tweet tweet, double facilityCost) {	
		TweetStorage reassignmentList = new TweetStorage();
		ClusterStorage removalList = new ClusterStorage();
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
			TweetStorage notReassignedTweets = TweetStorage.getDifference(c.getTweets(), reassignmentList);
			for (Tweet t : notReassignedTweets) {
				double d1 = getDist(t, t.getCluster().getCenter());
				double d2 = getDist(t, tweet);
				removalGain +=  d1 - d2;
			}
			if (removalGain >= 0) {
				gain += removalGain;
				removalList.add(c);
				reassignmentList.addAll(notReassignedTweets);
			}
		}

		if (gain > 0) {			
			Cluster c = new Cluster(tweet);			
			for (Tweet t : reassignmentList) {
				c.addTweet(t);
			}
			
			clusters.removeAll(removalList);
			clusters.add(c);
			
			for (Tweet t : tweets.getUnclusteredTweets()) {
				t.setCluster(getNearestCluster(clusters, t));
			}
		}
	}

	private static Cluster getNearestCluster(ClusterStorage clusters, Tweet tweet) {
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

	private static double getDist(Tweet t1, Tweet t2) {
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
