package main;

import dataAccessLayer.DBConnect;
import modelLayer.TweetStorage;

import streaming.Oauth;
import streaming.OurStatusListener;

import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.Timer;
import java.util.TimerTask;

public class Program {

	public static void main(String[] args) {

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
        //A minute in ms: 60000
        //An hour in ms: 3600000

        //connection.closeConnection();
    }
}
