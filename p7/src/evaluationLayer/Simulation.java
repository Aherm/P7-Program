package evaluationLayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import businessLogicLayer.Filter;
import businessLogicLayer.Preprocessor;
import businessLogicLayer.Scoring;
import dataAccessLayer.DBConnect;
import dataAccessLayer.DBGetRestaurants;
import dataAccessLayer.DBGetTweets;
import modelLayer.ClusterStorage;
import modelLayer.Grid;
import modelLayer.InvertedIndex;
import modelLayer.Restaurant;
import modelLayer.Tweet;
import modelLayer.TweetStorage;
import streaming.TwitterRest;
import twitter4j.Status;
import twitter4j.TwitterException;
import utility.Tuple;

public class Simulation {
	
	private TweetStorage tweets = new TweetStorage();
	private InvertedIndex invertedIndex = new InvertedIndex();
	// TODO: Determine the best number of rows and columns. Maybe pass them as argument instead.
	private Grid grid = new Grid(-78, -70, 38, 44, 1000, 1000);
	private List<Restaurant> restaurants; 
	private List<Rank> ranks; 
	Scoring score = new Scoring(); 
	
	public void stream(){
		DBConnect connection = DBConnect.getInstance();
        connection.connectToServer("jdbc:postgresql://172.25.26.208/", "world" + "", "postgres", "21");
		TweetStorage allTweet = DBGetTweets.getAllTweetsExperiment(); 
		Tuple<List<Restaurant>,List<Rank>> tuple = DBGetRestaurants.getRestaurants(); 
		restaurants = tuple.x; 
		ranks = tuple.y; 
		
		
		for(Restaurant r: restaurants){
    		if(!r.getName().contains("v {iv}") && !r.getName().contains("floor)"))
    			invertedIndex.addEntry(r);
    	}
		invertedIndex.init();
		System.out.println("Starting");
		long startTime = System.nanoTime(); 
		for(Tweet t : allTweet){
			onTweet(t);
		}
		long endTime = System.nanoTime();
		System.out.println(endTime - startTime);
		System.out.println(tweets.getSickTweets().size());
		System.out.println("Starting Scoring");
		evaluate();
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
				tweets.getTweet(ts.get(i)).setSick(true);
				ts.remove(ts.get(i));
			}
		}
	}
	
	private void evaluate(){
		Scoring score = new Scoring(); 
		
		score.ScoreSystem(grid, invertedIndex, tweets, restaurants);
		
		List<Rank> geoRanks = new ArrayList<Rank>();
		List<Rank> nameRanks = new ArrayList<Rank>(); 
		List<Rank> combined = new ArrayList<Rank>();
		for(Restaurant r : score.geoScore.keySet()){
			if(score.geoScore.get(r).doubleValue() > 0){
				geoRanks.add(new Rank(r,score.geoScore.get(r).doubleValue())); 
			}
		}
		
		for(Restaurant r : score.nameScore.keySet()){
			if(score.nameScore.get(r).doubleValue() > 0)
				nameRanks.add(new Rank(r, score.nameScore.get(r).doubleValue()));
		}
		
		for(Restaurant r: score.combinedScore.keySet()){
			if(score.combinedScore.get(r).doubleValue() > 0)
				combined.add(new Rank(r,score.combinedScore.get(r)));
		}
		
		RankHandler handler = new RankHandler(ranks,combined);
		handler.printRanks();
		
	}
	

	
}
