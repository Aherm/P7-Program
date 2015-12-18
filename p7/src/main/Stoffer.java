package main;

import businessLogicLayer.Filter;
import dataAccessLayer.DBConnect;
import dataAccessLayer.DBGetTweets;
import dataAccessLayer.DBInsert;
import modelLayer.Tweet;
import modelLayer.TweetStorage;
import streaming.TwitterRest;
import twitter4j.TwitterException;

public class Stoffer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBConnect connection = DBConnect.getInstance();
        connection.connectToServer("jdbc:postgresql://172.25.26.208/", "world" + "", "postgres", "21");
        TweetStorage allTweets = DBGetTweets.lastThing(); 
        System.out.println(allTweets.size());
        TwitterRest rest = TwitterRest.getInstance(); 
        TweetStorage usertimelines = new  TweetStorage(); 
        int counter = 0; 
        for(Tweet T : allTweets){
        	if(counter % 10000 == 0){
    			System.out.println("Made it to:"  + counter + "\n" +"TimeLine" + usertimelines.size());		
    		}
        	if(Filter.passesFilter(T)){
        		usertimelines.add(T); 
        	}
        	counter++;
        }
        
        TweetStorage allTimeLines = new TweetStorage();
        for(Tweet t : usertimelines){
        	 try {
        		 allTimeLines.addAll(rest.getUserTimeline3days(t.getUserID(), t.getCreatedAt(), t));
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

     			if(e.getStatusCode() == 401){
     				e.printStackTrace();
     				System.out.println("processing tweet ");
     			}
     		}
        }
        
        for(Tweet t : allTweets){
        	if(!allTweets.contains(t)){
        		allTweets.add(t);
        	}
        }
        
        DBInsert.insertTweets(allTweets,"experiment2");
        connection.closeConnection();
	}

}
