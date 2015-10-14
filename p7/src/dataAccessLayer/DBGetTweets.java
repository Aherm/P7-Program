package dataAccessLayer;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBGetTweets {

    public DBGetTweets() {
    }

    public TweetStorage tsQuery(String query){
    	
    	TweetStorage tweets = new TweetStorage();
        
        try {
        	Connection con = DBConnect.getInstance().getCon();

            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery(query);

            tweets = initializeTweets(res);
        } catch (Exception exh) {
            System.out.println(exh);
        }
        return tweets;
    }
    

    public TweetStorage getKTweets (int k) {
    	return tsQuery("SELECT * FROM tweets LIMIT " + k);
    }
    
    public TweetStorage getTweets() {
        return tsQuery("SELECT * FROM tweets");
    }
    
    public TweetStorage getKLastTweets(int k){
    	return tsQuery("SELECT * FROM tweets ORDER BY tweetid DESC LIMIT " + k);
    }
    public TweetStorage getInterval(int start,int size){
    	return tsQuery("SELECT * FROM tweets ORDER BY id LIMIT " + size +" OFFSET " + start);
    }
    
    public long countQuery(String query){
        long numTweets = 0;

        try {
            Connection con = DBConnect.getInstance().getCon();
            
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery(query);

            res.next();
            numTweets = res.getLong("count");

        } catch (Exception exh) {
            System.out.println(exh);
        }
        return numTweets;
    }
    
    public long getNumTweets() {
       return countQuery("SELECT count(*) FROM tweets");
    }
    
    private TweetStorage initializeTweets(ResultSet res) {
        TweetStorage tweets = new TweetStorage();
        try {
            while (res.next()) {
                Tweet newTweet = new Tweet();
                newTweet.setTweetID(res.getLong("tweetID"));
                newTweet.setUserID(res.getLong("userid"));
                newTweet.setResponseID(res.getLong("responseid"));
                newTweet.setRetweetID(res.getLong("retweetid"));
                newTweet.setTweetText(res.getString("tweetText"));
                newTweet.setCreatedAt(convertStringToDate(res.getString("createdat")));
                newTweet.setLat(res.getDouble("lat"));
                newTweet.setLon(res.getDouble("lon"));
                tweets.add(newTweet);
            }
        } catch (Exception exh) {
            System.out.println(exh);
        }
        return tweets;
    }

    //New select query
    //SELECT * FROM tweets Order By id LIMIT 9000 OFFSET 4000

    private static Date convertStringToDate(String string) {
        Date date = new Date();
        try {
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            date = format.parse(string);
            //System.out.println(date); // Sat Jan 02 00:00:00 GMT 2010
        } catch (Exception ex){
            System.out.println(ex);
        }
        return date;

    }
}
