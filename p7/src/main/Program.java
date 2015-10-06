package main;

import businessLogicLayer.Filter;
import dataAccessLayer.DBConnect;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

import streaming.Oauth;
import streaming.OurStatusListener;

import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.*;

import businessLogicLayer.Preprocessor;
import businessLogicLayer.TweetQueryThread;

public class Program {

    public static void main(String[] args) {


        Tweet tweet1 = new Tweet(0, 2, 3, 4, "asfafas f463t fsahe8Mads90ath9", new Date(), 0.2, 0.5);
        Tweet tweet2 = new Tweet(0, 2, 3, 4, "asfafas Mathias f463t fssafasfasfahe890ath9", new Date(), 0.2, 0.5);
        List<Tweet> tweets = new ArrayList<Tweet>();
        tweets.add(tweet1);
        tweets.add(tweet2);


        List<String> patterns = new ArrayList<String>();
        //patterns.add("[\\w*mads\\w*]");
        patterns.add("(\\w|\\s)*(Mathias)(\\w|\\s)*");

        for (Tweet tweet : tweets) {
            Preprocessor.processTweet(tweet);

            if (Filter.filterTweetFromPatterns(tweet, patterns))
            {
                System.out.println(tweet.getTweetText() + " is approved");
            }
            else {
                System.out.println(tweet.getTweetText() + " is not approved");
            }
        }


        //connection to twitters streaming api
        /*
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
		*/
        //connection.closeConnection();
    }
}
