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
import utility.Utils;

import java.util.*;

import javax.rmi.CORBA.Util;



import streaming.TweetQueryThread;

public class Program {

    public static void main(String[] args) {
        
    	//SETTING UP TWITTER STREAM 
    	TwitterStreamFactory tsf = new TwitterStreamFactory(Oauth.createConfigBuilder().build());
        TwitterStream stream = tsf.getInstance();
        InvertedIndex invertedIndex = new InvertedIndex();
        List<Restaurant> restaurants = Utils.getRestaurantsFromFile("restaurantData/resData.csv");
        for(Restaurant r: restaurants){
            r.setName(r.getName().toLowerCase());
            if (!r.getName().toLowerCase().contains("{iv}") && !r.getName().toLowerCase().contains("floor)"))
                invertedIndex.addEntry(r);

        }

        invertedIndex.init();
        OurStatusListener listener = new OurStatusListener();
        listener.setInvertedIndex(invertedIndex);
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

        TweetStorage allTweets = listener.getTweets();
        Grid grid = listener.getGrid();

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
