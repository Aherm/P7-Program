package evaluationLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import Processing.Stopwords;
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
		
		for(Restaurant r: restaurants){
    		if(!r.getName().contains("{iv}") && !r.getName().contains("floor)"))	
    				invertedIndex.addEntry(r);

    	}
		invertedIndex.init();
		System.out.println("Starting");
		long startTime = System.nanoTime(); 
		int counter = 0; 
		List<Long> sickUsers = new ArrayList<>();
		for(Tweet t : allTweet){
			if(!tweets.contains(t))
				onTweet(t,sickUsers);
			counter++;
			if(counter % 1000 == 0){
				System.out.println("Done: " + counter);
			}
				
		}
		
		for(Tweet t : allTweet){
			if(sickUsers.contains(t.getUserID())){
				t.setSick(true);
			}
		}
		
		
		long endTime = System.nanoTime();
		System.out.println(endTime - startTime);
		System.out.println(tweets.getSickTweets().size());
		System.out.println("Starting Scoring");
		evaluate();
		connection.closeConnection();
	}

	private void onTweet(Tweet tweet,List<Long> sick) throws Exception{
	
		Preprocessor.processTweet(tweet);
		tweets.add(tweet); 
		grid.addTweet(tweet);
		invertedIndex.addIndex(tweet);
		
		if(Filter.passesFilter(tweet)){
			System.out.println("found this guy: " + tweet.getTweetText());
			sick.add(tweet.getUserID());
		}
		
	}
	
	private void removeSeenTweets(TweetStorage ts){
		for(int i = 0 ; i < ts.size(); i++){
			if(tweets.contains(ts.get(i))){
				tweets.getTweet(ts.get(i)).setSick(true);
				ts.remove(ts.get(i));
			}
			else 
				ts.get(i).setSick(true);
		}
	}
	
	private void evaluate(){
		Scoring score = new Scoring(); 
		
		score.ScoreSystem(grid, invertedIndex, tweets, restaurants);
		
		List<Rank> geoRanks = new ArrayList<Rank>();
		List<Rank> nameRanks = new ArrayList<Rank>(); 
		List<Rank> combined = new ArrayList<Rank>();
		List<Rank> augCombined = new ArrayList<Rank>(); 
		List<Rank> conservative = new ArrayList<Rank>(); 
		
		
		for(Restaurant r : score.geoScore.keySet()){
			if(score.geoScore.get(r).doubleValue() > 0){
				geoRanks.add(new Rank(r,score.geoScore.get(r).doubleValue())); 
			}
			
		}
		
		System.out.println("NAMES-----------------------------------------------");
		for(Restaurant r : score.nameScore.keySet()){
			if(score.nameScore.get(r).doubleValue() > 0)
				nameRanks.add(new Rank(r, score.nameScore.get(r).doubleValue()));
			
		}
		System.out.println("COMBINED---------------------------------------------"); 
		for(Restaurant r: score.combinedScore.keySet()){
			if(score.combinedScore.get(r).doubleValue() > 0)
				combined.add(new Rank(r,score.combinedScore.get(r)));
		
		}
		
		for(Restaurant r : score.noMcombinedScore.keySet()){
			if(score.noMcombinedScore.get(r).doubleValue() > 0)
				augCombined.add(new Rank(r, score.noMcombinedScore.get(r)));
		}
		
		for(Restaurant r : score.conservative.keySet()){
			if(score.conservative.get(r).doubleValue() > 0)
				conservative.add(new Rank(r, score.conservative.get(r)));
				
		}
		
		RankHandler handler = new RankHandler(ranks,combined);
		RankHandler handler2 = new RankHandler(ranks,nameRanks);
		RankHandler handler3 = new RankHandler(ranks,geoRanks); 
		RankHandler handler4 = new RankHandler(ranks, augCombined); 
		RankHandler handler5 = new RankHandler(ranks, conservative); 
		handler.printRanks("combineRanks.csv");
		handler2.printRanks("nameRanks.csv");
		handler3.printRanks("geoRanks.csv");
		handler4.printRanks("otherCombineRanks.csv");
		handler5.printRanks("conservativeRanks.csv");
		
	
	}
	

	
}
