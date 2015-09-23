package main;

import dataAccessLayer.DBConnect;
import dataAccessLayer.DBInsert;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

import java.util.Date;
import java.util.HashMap;
import java.util.TimerTask;

public class RunMeTask extends TimerTask
{
    DBConnect connection;
    HashMap<String, Tweet> tweets = new HashMap<String, Tweet>();
    TweetStorage tweets2;
    Date lastInserted;

    public RunMeTask(HashMap<String, Tweet> tweets, DBConnect connection) {
        this.tweets = tweets;
        this.connection = connection;
    }
    
    public RunMeTask(TweetStorage tweets, DBConnect connection) {
    	this.tweets2 = tweets;
    	this.connection = connection;
    	lastInserted = new Date();
    }

    @Override
    public void run() {

        try {
            System.out.println("Run Me ~");
            if (tweets2.size() != 0) {
                System.out.println("Inserting rows");
                DBInsert dbInsert = new DBInsert(connection);
                dbInsert.insertTweetStorage(tweets2, lastInserted);

                // insert the keywords
                //dbInsert.insertKeywordsPreparedStatement(tweets);

                // clear the hashmap
                //tweets.clear();
                lastInserted = new Date();
            }
        }
        catch (Exception exh){
            System.out.println(exh);
        }
    }
}