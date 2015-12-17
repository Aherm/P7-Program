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

	private static String db = "tweets"; 
	
    public DBGetTweets() {
    }

    private static TweetStorage tsQuery(String query) {

        TweetStorage tweets = new TweetStorage();

        try {
            Connection con = DBConnect.getInstance().getCon();

            Statement stmt = con.createStatement();
            stmt.execute("SET datestyle = \"ISO,DMY\"");
            ResultSet res = stmt.executeQuery(query);

            tweets = initializeTweets(res);
        } catch (Exception exh) {
            System.out.println(exh);
        }

        return tweets;
    }

    public static TweetStorage getTweets() {
        return tsQuery("SELECT * FROM " + db);
    }

    public static TweetStorage getKTweets(int k) {
        return tsQuery("SELECT * FROM " + db + " LIMIT " + k);
    }


    public static TweetStorage getKLastTweets(int k) {
        return tsQuery("SELECT * FROM "+ db + " ORDER BY tweetid DESC LIMIT " + k);
    }

    public static TweetStorage getInterval(int start, int size) {
        return tsQuery("SELECT * FROM " + db + " ORDER BY id LIMIT " + size + " OFFSET " + start);
    }

    public static TweetStorage getTweetsFromLastThreeDays() {
        return tsQuery("SELECT * FROM " + db + "  WHERE EXTRACT(DAY FROM age(createdat::timestamp)) <= 3");
    }

    public static TweetStorage getGeotaggedTweets() {
        return tsQuery("SELECT * from " + db + " WHERE NOT lat = 0 AND not lon = 0");
    }

    public static TweetStorage getTweetsFromNewYork(){
        return getTweetsForBoundingBox(40, -74, 41, -73);
    }

    //btc 15112015, maybe refactor so that it takes arrays of lat lon pairs
    public static TweetStorage getTweetsForBoundingBox(int lat1, int lon1, int lat2, int lon2) {
        return tsQuery("SELECT * " +
                "FROM tweets AS t " +
                "WHERE t.lat >= " + lat1 + " AND t.lat <= " + lat2 + " AND t.lon >= " + lon1 + " -74.0 AND t.lon <= " + lon2 +
				" Order By id LIMIT 10"
		);
    }

    private static long countQuery(String query) {
        long numTweets = 0;

        try {
            Connection con = DBConnect.getInstance().getCon();

            Statement stmt = con.createStatement();
            stmt.execute("set datestyle = \"ISO,DMY\"");
            ResultSet res = stmt.executeQuery(query);

            res.next();
            numTweets = res.getLong("count");

        } catch (Exception exh) {
            System.out.println(exh);
        }
        return numTweets;
    }

    public static long getNumTweets() {
        return countQuery("SELECT count(*) FROM " + db);
    }

    public static long getNrTweetsFromDay(String date) {
        return countQuery("SELECT count(*) FROM " + db + "  WHERE createdat::date = '" + date + "'");
    }


    private static TweetStorage initializeTweets(ResultSet res) {
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
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return date;
    }
    
    
    //USED IN SIMULATION DO NOT TOUCH 
    
    public static TweetStorage getAllTweetsExperiment(){
    	return tsQuery("SELECT * FROM experiment" );
    }
    
    public static TweetStorage getUserExperiment(long userid){
    	return tsQuery("SELECT * FROM experiment WHERE userid = " + userid); 
    }
    
    public static TweetStorage getTestSet(){
    	return tsQuery("SELECT * FROM new_york_tweets WHERE id > 1200 Order BY id LIMIT 5000");
    }
    

}
