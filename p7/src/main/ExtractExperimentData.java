package main;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import businessLogicLayer.Filter;
import streaming.TwitterRest;
import dataAccessLayer.DBConnect;
import dataAccessLayer.DBGetTweets;
import dataAccessLayer.DBInsert;
import modelLayer.Tweet;
import modelLayer.TweetStorage;
import twitter4j.TwitterException;

public class ExtractExperimentData {

	public static void main(String[] args) {
		DBConnect connection = DBConnect.getInstance();
		connection.connectToServer("jdbc:postgresql://172.25.26.208/", "world", "postgres", "21");
		
		TweetStorage tweets = DBGetTweets.getTweetsFromLastThreeDays();
		TwitterRest restAPI = TwitterRest.getInstance();
		TweetStorage result = tweets.clone();
		System.out.println("Done fetching from DB");
		int counter = 0; 
		while (counter < tweets.size()) {
			 Tweet tweet = tweets.get(counter);
			if (Filter.passesFilter(tweet)) {
				if (restAPI.limitReached) {
					try {
						System.out.println("Limit reached. Waiting 15 minutes.");
						TimeUnit.MINUTES.sleep(16);
						System.out.println("Continuing");
					} 
					catch (InterruptedException e1) {
						System.out.println("Sleeping interrupted");
					}
				}
				try {
					TweetStorage ts = restAPI.getUserTimeline3days(tweet.getUserID(),new Date(),tweet);
					result = TweetStorage.getUnion(result, ts);
				}
				catch (TwitterException e) {
					if (e.getStatusCode() == 420 || e.getStatusCode() == 429){
						System.out.println("Too many requests");
						e.printStackTrace();
					}
					// server overloaded
					if (e.getStatusCode() == 503){
						System.out.println("Twitter is overloaded");
						e.printStackTrace();
					}

					continue;
				}
			}
		}
		
		System.out.println("Done getting user timelines");
		
		connection.closeConnection();
		connection.connectToServer("jdbc:postgresql://172.25.26.208/", "world", "postgres", "21");
		DBInsert.insertTweets(result, "experiment");
		connection.closeConnection();
		System.out.println("Done");

	}

}
