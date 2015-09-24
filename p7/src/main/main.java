package main;

import dataAccessLayer.DBConnect;
import dataAccessLayer.DBInsert;
import modelLayer.Tweet;
import modelLayer.TweetStorage;
import streaming.OurStatusListener;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.*;
import java.util.concurrent.*;

public class main {

	public static void main(String[] args) {

		OurStatusListener listener = new OurStatusListener();
		TwitterStream stream = new TwitterStreamFactory().getInstance();
		stream.addListener(listener);
		
		//stream.sample();

        double[][] locations = new double[][]{
                {-74,40},
                {-73,41}
        };

        FilterQuery query = new FilterQuery();
        query.locations(locations);
        stream.filter(query);

        DBConnect connection = new DBConnect();
        connection.connectToLocal("postgres", "postgres", "21");

        TweetStorage tweets = listener.getTweets();
        
        TimerTask task = new RunMeTask(tweets, connection);

        Timer timer = new Timer();
        timer.schedule(task, 30000);
        //A minute in ms: 60000
        //An hour in ms: 3600000

        //DBConnect.closeConnection();
    }
}
