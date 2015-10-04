package main;

import businessLogicLayer.Filter;
import dataAccessLayer.DBConnect;
import dataAccessLayer.DBGetTweets;
import modelLayer.TweetStorage;

import java.util.List;

public class TestDBMain {
	public static void main(String[] args) 
	{
		DBConnect connection = DBConnect.getInstance();
		connection.connectTo("postgres", "postgres", "21");

		DBGetTweets dbGetTweets = new DBGetTweets();
		TweetStorage tweets = dbGetTweets.getTweets();
		/*List<String> containedKeywords = Filter.containsKeywords(tweets.get(0).getTweetText());

		System.out.println("tweetID: " + tweets.get(0).getTweetID());
		for (String kw : containedKeywords)
			System.out.println("contained keyword: " + kw);
		*/
		connection.closeConnection();
	}

}