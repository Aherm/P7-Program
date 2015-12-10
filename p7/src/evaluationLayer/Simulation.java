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
import naiveBayes.Multinomial;
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
	private List<Rank> ranks = new ArrayList<Rank>(); 
	Scoring score = new Scoring(); 
	Multinomial nominal;   
	
	public void stream() throws Exception{
		DBConnect connection = DBConnect.getInstance();
        connection.connectToServer("jdbc:postgresql://172.25.26.208/", "world" + "", "postgres", "21");
		TweetStorage allTweet = DBGetTweets.getAllTweetsExperiment(); 
		Tuple<List<Restaurant>,List<Rank>> tuple = DBGetRestaurants.getRestaurants(); 
		restaurants = tuple.x; 
		ranks = tuple.y; 
		nominal = Multinomial.loadClassifier("./classifiers/sickNaiveBayes.model");
		/*
		Restaurant r1 = new Restaurant("diner",40.726992, -74.000600); 
		Restaurant r2 = new Restaurant("tasty",40.727048, -74.000688);
		Restaurant r3 = new Restaurant("stuff",40.727007, -74.002197); 
		Restaurant r4 = new Restaurant("last",40.728976, -74.001082);
		Restaurant r5 = new Restaurant("stuff",1,1); 
		
		restaurants = new ArrayList<Restaurant>();
		restaurants.add(r1);
		restaurants.add(r2); 
		restaurants.add(r3);
		restaurants.add(r4);
		restaurants.add(r5);
		
		TweetStorage allTweet = new TweetStorage(); 
		Tweet t1 = new Tweet(1,1,1,1,"eating lol and feeling sick ",new Date(),40.727092, -74.000982); 
		Tweet t2 = new Tweet(1,1,1,1,"eating at the tasty lol feeling sick",new Date(),40.727091, -74.000533);
		Tweet t3 = new Tweet(1,1,1,1,"eating at stuff xD and feeling sick ",new Date());
		Tweet t4 = new Tweet(1,1,1,1,"hating feeling sick the diner right now",new Date(),40.728976, -74.0010820);
		allTweet.add(t1); 
		allTweet.add(t2); 
		allTweet.add(t3);
		allTweet.add(t4);
		
		ranks.add(new Rank(r1, 1)); 
		ranks.add(new Rank(r2,2)); 
		ranks.add(new Rank(r3,3)); 
		ranks.add(new Rank(r4,4)); 
	
		*/
		
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
		//tweets.addAll(allTweet);
		long endTime = System.nanoTime();
		System.out.println(endTime - startTime);
		System.out.println(tweets.getSickTweets().size());
		System.out.println("Starting Scoring");
		evaluate();
		connection.closeConnection();
	}

	private void onTweet(Tweet tweet) throws Exception{
	
		Preprocessor.processTweet(tweet);
		tweets.add(tweet); 
		grid.addTweet(tweet);
		invertedIndex.addIndex(tweet);
		if(Filter.passesFilter(tweet)){
			tweet.setSick(true);
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
			//System.out.println(r.getName() + ":" + score.geoScore.get(r).doubleValue());
		}
		
		System.out.println("NAMES-----------------------------------------------");
		for(Restaurant r : score.nameScore.keySet()){
			if(score.nameScore.get(r).doubleValue() > 0)
				nameRanks.add(new Rank(r, score.nameScore.get(r).doubleValue()));
			//System.out.println(r.getName() + ":" + score.nameScore.get(r).doubleValue());
		}
		System.out.println("COMBINED---------------------------------------------"); 
		for(Restaurant r: score.combinedScore.keySet()){
			if(score.combinedScore.get(r).doubleValue() > 0)
				combined.add(new Rank(r,score.combinedScore.get(r)));
			//System.out.println(r.getName() + ":" + score.combinedScore.get(r).doubleValue());
		}
		
		RankHandler handler = new RankHandler(ranks,combined);
		RankHandler handler2 = new RankHandler(ranks,nameRanks);
		RankHandler handler3 = new RankHandler(ranks,geoRanks); 
		handler.printRanks("combineRanks.csv");
		handler2.printRanks("nameRanks.csv");
		handler3.printRanks("georanks.csv");
	}
	

	
}
