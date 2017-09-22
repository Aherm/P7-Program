package main;

import java.util.TimerTask;

import dataAccessLayer.DBInsert;
import modelLayer.TweetStorage;

public class RunMeTask extends TimerTask
{
    TweetStorage newTweets;
    String tableName;
    
    public RunMeTask(TweetStorage tweets, String tableName) {
    	this.newTweets = tweets;
        this.tableName = tableName;
    }

    @Override
    public void run() {
        try {
            System.out.println("Run Me ~");

            if (newTweets.size() != 0) {
                DBInsert.insertTweets(newTweets, tableName);
                newTweets.clear();
            }
        }
        catch (Exception exh){
            System.out.println(exh);
        }
    }
}