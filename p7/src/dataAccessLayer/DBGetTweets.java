package dataAccessLayer;

import modelLayer.Tweet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBGetTweets {

    public DBGetTweets() {}

    public long getNumTweets() {
        long numTweets = 0;

        try {
        	Connection con = DBConnect.getInstance().getCon();
            String query = "SELECT count(*) AS count FROM tweets";

            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery(query);

            res.next();
            numTweets = res.getLong("count");

        } catch (Exception exh) {
            System.out.println(exh);
        }
        return numTweets;
    }

    public List<Tweet> getTweets() {
        List<Tweet> tweets = new ArrayList<Tweet>();
        try {
        	Connection con = DBConnect.getInstance().getCon();
            String query = "SELECT * FROM tweets";

            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery(query);

            tweets = initializeTweets(res);
        } catch (Exception exh) {
            System.out.println(exh);
        }
        return tweets;
    }


    private List<Tweet> initializeTweets(ResultSet res){
        List<Tweet> tweets = new ArrayList<Tweet>();
        try{
            while (res.next()) {
                Tweet newTweet = new Tweet();
                newTweet.setTweetID(res.getLong("tweetID"));
                newTweet.setUserID(res.getLong("userid"));
                newTweet.setResponseID(res.getLong("responseid"));
                newTweet.setRetweetID(res.getLong("retweetid"));
                newTweet.setTweetText(res.getString("tweet"));
                //newTweet.setCreatedAt(res.getDate("createdat"));
                newTweet.setLat(res.getDouble("lat"));
                newTweet.setLon(res.getDouble("lon"));
                tweets.add(newTweet);
            }
        } catch (Exception exh){
            System.out.println(exh);
        }
        return tweets;
    }
}
