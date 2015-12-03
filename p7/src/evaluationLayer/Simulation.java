package evaluationLayer;

import java.util.Date;

import businessLogicLayer.Filter;
import businessLogicLayer.Preprocessor;
import dataAccessLayer.DBConnect;
import dataAccessLayer.DBGetTweets;
import modelLayer.ClusterStorage;
import modelLayer.Grid;
import modelLayer.InvertedIndex;
import modelLayer.Tweet;
import modelLayer.TweetStorage;
import streaming.TwitterRest;
import twitter4j.Status;
import twitter4j.TwitterException;

public class Simulation {
	
	private TweetStorage tweets = new TweetStorage();
	private InvertedIndex invertedIndex = new InvertedIndex();
	// TODO: Determine the best number of rows and columns. Maybe pass them as argument instead.
	private Grid grid = new Grid(-74, -73, 40, 41, 1000, 1000);
	
	public void stream(){
		DBConnect connection = DBConnect.getInstance();
        connection.connectToServer("jdbc:postgresql://172.25.26.208/", "world" + "", "postgres", "21");
		TweetStorage allTweet = DBGetTweets.getAllTweetsExperiment(); 
		
		
		for(Tweet t : allTweet){
			onTweet(t);
		}
		
		connection.closeConnection();
	}

	private void onTweet(Tweet tweet){
	
		Preprocessor.processTweet(tweet);
		tweets.add(tweet); 
		grid.addTweet(tweet);
		invertedIndex.addIndex(tweet);
		
		if(Filter.passesFilter(tweet)){
			System.out.println("Found this guy: " + tweet.getTweetText());
			TweetStorage userTimeLine = DBGetTweets.getUserExperiment(tweet.getUserID());
			removeSeenTweets(userTimeLine);
			invertedIndex.addIndices(userTimeLine);
			tweets.addAll(userTimeLine); 
			grid.addTweets(userTimeLine);
		}
	}
	
	private void removeSeenTweets(TweetStorage ts){
		for(int i = 0 ; i < ts.size(); i++){
			ts.get(i).setSick(true);
			if(tweets.contains(ts.get(i))){
				tweets.getTweet(ts.get(i)).setSick(true);;
			}
		}
	}
	
	
}
