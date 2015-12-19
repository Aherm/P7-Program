package main;

import dataAccessLayer.DBConnect;
import dataAccessLayer.DBGetRestaurants;
import modelLayer.ClusterStorage;
import modelLayer.Grid;
import modelLayer.InvertedIndex;
import modelLayer.Restaurant;
import modelLayer.TweetStorage;
import streaming.Oauth;
import streaming.OurStatusListener;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.*;

import streaming.TweetQueryThread;

public class Program {

    public static void main(String[] args) {
        
    	//SETTING UP TWITTER STREAM 
    	TwitterStreamFactory tsf = new TwitterStreamFactory(Oauth.createConfigBuilder().build());
        TwitterStream stream = tsf.getInstance();

        OurStatusListener listener = new OurStatusListener();
        stream.addListener(listener);;

        //bounding box for new york
        double[][] locations = new double[][]{
                {-74, 40}, //lon, lat
                {-73, 41}
        };

       
        FilterQuery query = new FilterQuery();
        query.locations(locations);
        stream.filter(query);

        //Datbase stuff commented out 
       // DBConnect connection = DBConnect.getInstance();
        //connection.connectToLocal("world", "postgres", "21");

        TweetStorage newTweets = listener.getDBTweets();
        TweetStorage allTweets = listener.getTweets();
        Grid grid = listener.getGrid();
        List<Restaurant> restaurants = DBGetRestaurants.getRestaurants().x;
        InvertedIndex invertedIndex = listener.getInvertedIndex();
        invertedIndex.init();
        //A minute in ms: 60000
        //An hour in ms: 3600000
        //TimerTask insertTweetsTask = new RunMeTask(newTweets, "new_york_tweets");
        //Timer timer = new Timer();
        //timer.schedule(insertTweetsTask, 1000, 60000);
        TweetQueryThread t = new TweetQueryThread(allTweets, restaurants,invertedIndex,grid);
        t.start();

        //connection.closeConnection();
    }
}
