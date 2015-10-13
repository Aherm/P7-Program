package main;

import java.util.List;

import businessLogicLayer.Clustering;
import dataAccessLayer.DBConnect;
import dataAccessLayer.DBGetTweets;
import modelLayer.Cluster;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class TestDBMain {
	public static void main(String[] args) 
	{
		DBConnect connection = DBConnect.getInstance();
		connection.connectToServer("jdbc:postgresql://172.25.26.208/", "postgres", "guest", "42");

		DBGetTweets dbGetTweets = new DBGetTweets();
		TweetStorage tweets = dbGetTweets.getKTweets(1000);

		System.out.println("Amount of tweets: " + tweets.size());
		Tweet t1 = tweets.get(0);
		Tweet t2 = tweets.get(1);
		System.out.println("Distance: " + Clustering.getDist(t1, t2));
		
		List<Cluster> clusters = Clustering.tweetClustering(tweets, 5000);
		
		
		System.out.println("Cluster size: " + clusters.size());
		
		connection.closeConnection();
	}
}