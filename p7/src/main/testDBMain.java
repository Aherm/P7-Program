package main;
import java.sql.Statement;
import java.util.Date;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import dataAccessLayer.DBConnect;
import dataAccessLayer.DBGetTweets;
import dataAccessLayer.DBInsert;
import modelLayer.Tweet;
import streaming.OurStatusListener;

public class TestDBMain {
	public static void main(String[] args) 
	{
		DBConnect connection = new DBConnect();
		connection.connectToLocal("postgres", "postgres", "21");

		DBGetTweets dbGetTweets = new DBGetTweets(connection);
		List<Tweet> tweets = dbGetTweets.getTweets();

		OurStatusListener statusListener = new OurStatusListener();
		List<String> containedKeywords = statusListener.containsKeywords(tweets.get(0).getTweetText());

		System.out.println("tweetID: " + tweets.get(0).getTweetID());
		for (String kw : containedKeywords)
			System.out.println("contained keyword: " + kw);

		DBConnect.closeConnection();
	}

}