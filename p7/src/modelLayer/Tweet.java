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
    private double score = -1;
    private int counter = 0;
    private List<Keyword> matchedKeywords = new ArrayList<Keyword>();    
    private Cluster assignedCluster = null;
    private boolean addedToStorage = false;

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
        this.matchedKeywords = matchedKeywords;
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
    
    public void add(Keyword keyword)
    {
    	matchedKeywords.add(keyword);
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

    public double getScore()
    {
    	score = 0;
    	for(Keyword keyword : matchedKeywords)
    	{
    		this.score += keyword.getWeight();
    	}
    	
    	return score;
    }
    
    public void resetScore()
    {
    	score = -1;
    }
    
    public void setScore(int score) {
    	this.score = score;
    }
    
    public Cluster getCluster() {
    	return this.assignedCluster;
    }
    
    public void setCluster (Cluster c) {
    	this.assignedCluster = c;
    }
    
    public boolean isGeotagged(){
    	if(lat == 0 && lon == 0)
    		return false; 
    	else 
    		return true; 
    }

    public boolean isAddedToStorage() {
        return addedToStorage;
    }

    public void setAddedToStorage(boolean addedToStorage) {
        this.addedToStorage = addedToStorage;
    }
}
