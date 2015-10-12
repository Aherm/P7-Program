package main;

import dataAccessLayer.DBInsert;
import modelLayer.Cluster;
import modelLayer.TweetStorage;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import businessLogicLayer.Clustering;
import businessLogicLayer.Filter;
import businessLogicLayer.Preprocessor;

public class RunMeTask extends TimerTask
{
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
                Preprocessor.processTweets(tweets, lastInserted);
                tweets = Filter.filterTweets(tweets, lastInserted);
                //Create or add to cluster
                List<Cluster> clusters = Clustering.tweetClustering(tweets, 0.2);
                tweets.removeOldTweets(3);
                //tweets.removeOldTweets(3, clusters);
                lastInserted = new Date();
            }
        }
        catch (Exception exh){
            System.out.println(exh);
        }
    }
}