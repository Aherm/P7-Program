package main;

import dataAccessLayer.DBConnect;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

import streaming.Oauth;
import streaming.OurStatusListener;

import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import businessLogicLayer.Preprocessor;
import businessLogicLayer.TweetQueryThread;

public class Program {

	public static void main(String[] args) {

		/*
		Tweet tweet1 = new Tweet(0, 2, 3, 4, "tweet1 Mads er diarrhea,:-: @food @urdad lol headache!! http://t.co/urmom hhah l0l n1..", new Date(), 0.2, 0.5);
		System.out.println("Original Tweet1: " + tweet1.getTweetText());
		Preprocessor.processTweetLinks(tweet1);
		System.out.println("Links Processed Tweet1: " + tweet1.getTweetText());
		Preprocessor.processTweetMentions(tweet1);
		System.out.println("Mentions Processed Tweet1: " + tweet1.getTweetText());
		Preprocessor.processTweetSymbols(tweet1);
		System.out.println("Symbols Processed Tweet1: " + tweet1.getTweetText() + "\n");
		
		Tweet tweet2 = new Tweet(0, 2, 3, 4, "tweet2 Mads er diarrhea,:-: @food @urdad lol headache!! http://t.co/urmom hhah l0l n1..", new Date(), 0.2, 0.5);
		System.out.println("Original Tweet2: " + tweet2.getTweetText());
		Preprocessor.processWholeTweet(tweet2);
		System.out.println("Whole Processed Tweet2: " + tweet2.getTweetText() + "\n");
	
		Tweet tweet3 = new Tweet(0, 2, 3, 4, "tweet3 Mads er diarrhea,:-: @food @urdad lol headache!! http://t.co/urmom hhah l0l n1..", new Date(), 0.2, 0.5);
		System.out.println("Original Tweet3: " + tweet3.getTweetText());
		Preprocessor.processTweet(tweet3);;
		System.out.println("Whole Processed Tweet3: " + tweet3.getTweetText());
		*/
		
		Oauth auth = new Oauth(); 
		OurStatusListener listener = new OurStatusListener();
		TwitterStreamFactory tsf = new TwitterStreamFactory(auth.createConfigBuilder().build());
		TwitterStream stream = tsf.getInstance(); 
		stream.addListener(listener);
		
		//stream.sample();

        double[][] locations = new double[][]{
                {-74,40},
                {-73,41}
        };

        FilterQuery query = new FilterQuery();
        query.locations(locations);
        stream.filter(query);

        DBConnect connection = DBConnect.getInstance();
        connection.connectTo("postgres", "postgres", "21");

        TweetStorage tweets = listener.getTweets();
        
        TimerTask task = new RunMeTask(tweets);

        Timer timer = new Timer();
        timer.schedule(task, 1000, 60000);
        
        TweetQueryThread t = new TweetQueryThread(tweets);
        t.start();
        //A minute in ms: 60000
        //An hour in ms: 3600000

        //connection.closeConnection();
    }
}
