package main;

import dataAccessLayer.DBConnect;
import dataAccessLayer.DBInsert;
import modelLayer.Tweet;

import java.util.HashMap;
import java.util.TimerTask;

public class RunMeTask extends TimerTask
{
    DBConnect connection;
    HashMap<String, Tweet> tweets = new HashMap<String, Tweet>();

    public RunMeTask(HashMap<String, Tweet> tweets, DBConnect connection) {
        this.tweets = tweets;
        this.connection = connection;
    }

    @Override
    public void run() {

        try {
            System.out.println("Run Me ~");
            if (tweets.size() != 0) {
                System.out.println("Inserting rows");
                DBInsert dbInsert = new DBInsert(connection);
                dbInsert.insertTweet(tweets);

                // insert the keywords
                dbInsert.insertKeywords(tweets);

                // clear the hashmap
                tweets.clear();
            }
        }
        catch (Exception exh){
            System.out.println(exh);
        }
    }
}