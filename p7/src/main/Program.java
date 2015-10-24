package main;

import dataAccessLayer.DBConnect;
import modelLayer.ClusterStorage;
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
		TwitterStreamFactory tsf = new TwitterStreamFactory(Oauth.createConfigBuilder().build());
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
        connection.connectToLocal("world", "postgres", "21");
        
        TweetStorage newTweets = listener.getDBTweets();
        TweetStorage allTweets = listener.getTweets();
        ClusterStorage clusters = new ClusterStorage();

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
