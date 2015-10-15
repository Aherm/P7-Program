package main;

import dataAccessLayer.DBInsert;
import modelLayer.TweetStorage;
import java.util.TimerTask;

public class RunMeTask extends TimerTask
{
    TweetStorage newTweets;
    
    public RunMeTask(TweetStorage tweets) {
    	this.newTweets = tweets;
    }

    @Override
    public void run() {
        try {
            System.out.println("Run Me ~");

            if (newTweets.size() != 0) {
                DBInsert.insertTweets(newTweets);
                newTweets.clear();
            }
        }
        catch (Exception exh){
            System.out.println(exh);
        }
    }
}