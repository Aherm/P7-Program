package modelLayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import twitter4j.Status;

public class Tweet {

    private long tweetID, userID, responseID, retweetID;
    private String tweetText;
    private Date createdAt;
    private double lat, lon;
    private int score = -1;
    private List<String> keywords;    
    private Cluster assignedCluster = null;

    public Tweet() {
    }

    public Tweet(long tweetID, long userID, long responseID, long retweetID, String tweetText, Date createdAt) {
        this(tweetID, userID, responseID, retweetID, tweetText, createdAt, 0.0, 0.0);
    }

    public Tweet(long tweetID, long userID, long responseID, long retweetID, String tweetText, Date createdAt, double lat, double lon) {
        this(tweetID, userID, responseID, retweetID, tweetText, createdAt, lat, lon, new ArrayList<String>());
    }
    
    public Tweet(long tweetID, long userID, long responseID, long retweetID, String tweetText, Date createdAt, double lat, double lon, List<String> keywords) {
        this.tweetID = tweetID;
        this.userID = userID;
        this.responseID = responseID;
        this.retweetID = retweetID;
        this.tweetText = tweetText;
        this.createdAt = createdAt;
        this.lat = lat;
        this.lon = lon;
        this.keywords = keywords;
    }

    public static Tweet createTweet(Status status) {
        //In case the tweet is not geotagged
        if (status.getGeoLocation() == null){
            return new Tweet(status.getId(), status.getUser().getId(), status.getInReplyToUserId(), status.getCurrentUserRetweetId(),
                    status.getText(), status.getCreatedAt());
        }
        else return new Tweet(status.getId(), status.getUser().getId(), status.getInReplyToUserId(), status.getCurrentUserRetweetId(),
                status.getText(), status.getCreatedAt(), status.getGeoLocation().getLatitude(), status.getGeoLocation().getLongitude());
    }
    
    public long getTweetID() {
        return tweetID;
    }

    public void setTweetID(long tweetID) {
        this.tweetID = tweetID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getResponseID() {
        return responseID;
    }

    public void setResponseID(long responseID) {
        this.responseID = responseID;
    }

    public long getRetweetID() {
        return retweetID;
    }

    public void setRetweetID(long retweetID) {
        this.retweetID = retweetID;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getScore() {
    	return score;
    }
    
    public void setScore(int score) {
    	this.score = score;
    }
    
    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
    
    public Cluster getCluster() {
    	return this.assignedCluster;
    }
    
    public void setCluster (Cluster c) {
    	this.assignedCluster = c;
    }
    
    public Tweet clone() {
    	Tweet t = new Tweet(this.tweetID, this.userID, this.responseID, this.retweetID, this.tweetText, this.createdAt,
    			this.lat, this.lon, this.keywords);
    	t.setCluster(this.getCluster());
    	t.setScore(this.getScore());
    	return t;
    }
}
