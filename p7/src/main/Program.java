package main;

import dataAccessLayer.DBConnect;
import modelLayer.Cluster;
import modelLayer.TweetStorage;

import streaming.Oauth;
import streaming.OurStatusListener;

import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.*;
import businessLogicLayer.TweetQueryThread;

public class Program {

    public static void main(String[] args) {
    	Oauth auth = new Oauth(); 
		TwitterStreamFactory tsf = new TwitterStreamFactory(auth.createConfigBuilder().build());
		TwitterStream stream = tsf.getInstance(); 

		OurStatusListener listener = new OurStatusListener();
		stream.addListener(listener);
		//stream.sample();

		//bounding box for new york
		//double[][] locations = new double[][]{
		//{-74, 40},
		//{-73, 41}
		//};

        //bounding box for the whole of us		
		double[][] locations = new double[][]{
            {-167.276413, 5.49955},
            {-52.23304, 83.162102}
		};

        FilterQuery query = new FilterQuery();
        //query.language("en");
        query.locations(locations);
        stream.filter(query);

        DBConnect connection = DBConnect.getInstance();
        connection.connectTo("world", "postgres", "21");
        
        TweetStorage newTweets = listener.getNewTweets();
        TweetStorage allTweets = listener.getAllTweets();
        List<Cluster> clusters = new ArrayList<Cluster>();
        listener.setClusters(clusters);

        //A minute in ms: 60000
        //An hour in ms: 3600000
        TimerTask task = new RunMeTask(newTweets);
        Timer timer = new Timer();
        timer.schedule(task, 1000, 60000);
        
        TweetQueryThread t = new TweetQueryThread(allTweets, clusters);
        t.start();
        
        //connection.closeConnection();
    }
}
