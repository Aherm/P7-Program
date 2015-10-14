package streaming;

import java.util.List;

import businessLogicLayer.Filter;
import businessLogicLayer.Preprocessor;
import modelLayer.Cluster;
import modelLayer.Tweet;
import modelLayer.TweetStorage;
import twitter4j.*;

public class OurStatusListener implements StatusListener {
    TweetStorage newTweets = new TweetStorage();
    TweetStorage allTweets = new TweetStorage();
    List<Cluster> clusters = null;

    public void onStatus(Status status) {
        Tweet tweet = Tweet.createTweet(status);
        newTweets.add(tweet);
        
        tweet = tweet.clone();
        Preprocessor.processTweet(tweet);
    	if(Filter.filterTweet(tweet))
    	{
    		allTweets.add(tweet);
    	}
    	allTweets.removeOldTweets(3, clusters);
    }

    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        //System.out.println("User: " + statusDeletionNotice.getUserId() + " deleted");
    }

    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
    }

    public void onException(Exception ex) {
        ex.printStackTrace();
    }

    public void onScrubGeo(long x, long y) {
    }

    public void onStallWarning(StallWarning warning) {
    }

    public TweetStorage getNewTweets() {
        return newTweets;
    }
    
    public TweetStorage getAllTweets() {
    	return allTweets;
    }
    
    public void setClusters(List<Cluster> clusters) {
    	this.clusters = clusters;
    }
}