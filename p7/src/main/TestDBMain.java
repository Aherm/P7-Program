package main;
import java.sql.Statement;
import java.util.Date;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import businessLogicLayer.Filter;
import dataAccessLayer.DBConnect;
import dataAccessLayer.DBGetTweets;
import dataAccessLayer.DBInsert;
import modelLayer.Tweet;
import streaming.OurStatusListener;

public class TestDBMain {
	public static void main(String[] args) 
	{
		DBConnect connection = DBConnect.getInstance();
		connection.connectTo("postgres", "postgres", "21");

		DBGetTweets dbGetTweets = new DBGetTweets();
		List<Tweet> tweets = dbGetTweets.getTweets();
		List<String> containedKeywords = Filter.containsKeywords(tweets.get(0).getTweetText());

		System.out.println("tweetID: " + tweets.get(0).getTweetID());
		for (String kw : containedKeywords)
			System.out.println("contained keyword: " + kw);

		connection.closeConnection();
	}

}