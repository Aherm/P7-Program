package businessLogicLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import modelLayer.Cluster;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class Clustering {
	
	public List<Cluster> tweetClustering (TweetStorage tweets, double facilityCost) {
		List<Cluster> clusters = new ArrayList<Cluster>();
		clusters = initialSolution(tweets, facilityCost);
		
		// TODO I'm not sure if it's log of cluster size or tweets size. The paper didn't make this clear.
		for (int i = 0; i < Math.log(clusters.size()); i++) {
			// TODO tweets.randomize();
			for (Tweet t : tweets) {
				checkGainAndReassign(t, clusters, tweets, facilityCost);
			}
		}
		
		return clusters;
	}
	
	public List<Cluster> initialSolution (TweetStorage tweets, double facilityCost) {
		// TODO tweets.randomize();
		Random rand = new Random();
		List<Cluster> clusters = new ArrayList<Cluster>();
		
		Cluster cluster = createCluster(tweets.getFirst());
		clusters.add(cluster);
		
		for (int i = 1; i < tweets.size(); i++) {
			Tweet tweet = tweets.get(i);
			
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
	private static double checkGainAndReassign(Tweet tweet, List<Cluster> clusters, TweetStorage tweets, double facilityCost) {
		List<Cluster> clustersCopy = new ArrayList<Cluster>();
		for (Cluster c : clusters) {
			clustersCopy.add(c.clone());
		}
		
		TweetStorage tweetsCopy = new TweetStorage();
		for (Cluster c : clustersCopy) {
			for (Tweet t : c.getTweets()){
				tweetsCopy.add(t);
			}
		}
		
		Tweet tweetCopy = tweet.clone();
		
		// Done with copying here
		
		double gain = -facilityCost;
		TweetStorage reassignmentList = new TweetStorage();
		
		for (Tweet t : tweetsCopy) {
			double dist = getDist(t, t.getCluster().getCenter()) - getDist(t, tweetCopy);
			if (dist > 0) {
				gain += dist;
				reassignmentList.add(t);
			}
		}
		
		Cluster cluster = new Cluster(tweetCopy);
		clustersCopy.add(cluster);
		
		for (Tweet t : reassignmentList) {
			reassignTweet(t, cluster);
		}
		
		for (int i = 0; i < clustersCopy.size(); i++) {
			if (clustersCopy.get(i).getTweets().size() <= 1) {
				gain += facilityCost;
				clustersCopy.remove(i);
			}
		}
		
		if (gain > 0) {
			tweet = tweetCopy;
			clusters = clustersCopy;
			tweets = tweetsCopy;
		}
		
		return gain;
	}
	
	public static void reassignTweet(Tweet t, Cluster c) {
		if (t.getCluster() != null) {
			t.getCluster().removeTweet(t);
		}
		t.setCluster(c);
		c.addTweet(t);
	}
	
	private static double getNearestCluster(List<Cluster> clusters, Tweet tweet) {
		double dist = Double.POSITIVE_INFINITY;
		
		for (Cluster cluster : clusters) {
			Double currentDist = getDist(cluster.getCenter(), tweet);
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
		
		/*
		double x1 = t1.getLon();
		double x2 = t2.getLon();
		double y1 = t1.getLat();
		double y2 = t2.getLat();
		
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
		*/
	}
	
	private static double toRadians(double degree)
	{
		return degree * Math.PI / 180;
	}
	
	public Cluster createCluster(Tweet center) {
		if (center.getCluster() != null) {
			center.getCluster().removeTweet(center);
		}		
		Cluster cluster = new Cluster(center);
		center.setCluster(cluster);
		cluster.addTweet(center);
		
		return cluster;
	}
}
