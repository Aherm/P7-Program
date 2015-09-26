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
    TweetStorage tweets;
    Date lastInserted;
    
    public RunMeTask(TweetStorage tweets) {
    	this.tweets = tweets;
    	lastInserted = new Date();
    }

    @Override
    public void run() {
        try {
            System.out.println("Run Me ~");
            if (tweets.size() != 0) {
                DBInsert dbInsert = new DBInsert();
                dbInsert.insertTweet(tweets, lastInserted);

                // insert the keywords
                //dbInsert.insertKeywordsPreparedStatement(tweets);

                lastInserted = new Date();
            }
        }
        catch (Exception exh){
            System.out.println(exh);
        }
    }
}