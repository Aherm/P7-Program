package main;

import dataAccessLayer.DBConnect;
import dataAccessLayer.DBGetRestaurants;
import modelLayer.ClusterStorage;
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
        TwitterStreamFactory tsf = new TwitterStreamFactory(Oauth.createConfigBuilder().build());
        TwitterStream stream = tsf.getInstance();

        OurStatusListener listener = new OurStatusListener();
        stream.addListener(listener);
        //stream.sample();

        //bounding box for new york
        double[][] locations = new double[][]{
                {-74, 40},
                {-73, 41}
        };

        //bounding box for the whole of us		
        //double[][] locations = new double[][]{
        //    {-167.276413, 5.49955},
        //    {-52.23304, 83.162102}
        //};

        FilterQuery query = new FilterQuery();
        //query.language("en");
        query.locations(locations);
        stream.filter(query);

        DBConnect connection = DBConnect.getInstance();
        connection.connectToLocal("world", "postgres", "21");

        TweetStorage newTweets = listener.getDBTweets();
        TweetStorage allTweets = listener.getTweets();
        ClusterStorage clusters = listener.getClusters();
        List<Restaurant> restaurants = DBGetRestaurants.getRestaurants();
        InvertedIndex invertedIndex = listener.getInvertedIndex();
        //A minute in ms: 60000
        //An hour in ms: 3600000
        TimerTask insertTweetsTask = new RunMeTask(newTweets, "new_york_tweets");
        Timer timer = new Timer();
        timer.schedule(insertTweetsTask, 1000, 60000);
        //TweetQueryThread t = new TweetQueryThread(allTweets, clusters, restaurants, invertedIndex);
        TweetQueryThread t = new TweetQueryThread(allTweets, clusters);
        t.start();

        //connection.closeConnection();
    }
}
