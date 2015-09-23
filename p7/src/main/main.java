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
        //-74,40,-73,41
        stream.filter(query);



        /*
            Iterator itr = tweets.entrySet().iterator();
        while(itr.hasNext()) {
                Map.Entry pair = (Map.Entry)itr.next();
                System.out.println(pair.getKey());

        }
        */

        HashMap<String, Tweet> tweets = listener.getTweets();
        DBConnect connection = new DBConnect();
        connection.connectToLocal("postgres", "postgres", "21");

        TweetStorage tweets2 = listener.getTweets2();
        
        TimerTask task = new RunMeTask(tweets2, connection);

        Timer timer = new Timer();
        timer.schedule(task, 1000, 3600000);
        //A minute in ms: 60000
        //An hour in ms: 3600000

        //DBConnect.closeConnection();

    }
}
