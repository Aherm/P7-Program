package modelLayer;

import java.util.Date;
import java.util.List;

import twitter4j.Status;

public class Tweet {

    private long tweetID;
    private long userID;
    private long responseID;
    private long retweetID;
    private String tweetText;
    private Date createdAt;
    private double lat, lon;
    private List<String> keywords;


    public Tweet() {
    }

    public Tweet(long tweetID, long userID, long responseID, long retweetID, String tweetText, Date createdAt, double lat, double lon) {
    	this.tweetID = tweetID;
        this.userID = userID;
        this.responseID = responseID;
        this.retweetID = retweetID;
        this.tweetText = tweetText;
        this.createdAt = createdAt;
        this.lat = lat;
        this.lon = lon;
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
    	return new Tweet(status.getId(), status.getUser().getId(), status.getInReplyToUserId(), status.getCurrentUserRetweetId(),
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

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }


}
