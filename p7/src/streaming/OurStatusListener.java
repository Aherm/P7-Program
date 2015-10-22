package streaming;

import java.util.Date;
import java.util.List;

import businessLogicLayer.Filter;
import businessLogicLayer.Preprocessor;
import businessLogicLayer.TwitterRest;
import modelLayer.Cluster;
import modelLayer.Tweet;
import modelLayer.TweetStorage;
import twitter4j.*;

public class OurStatusListener implements StatusListener {
    TweetStorage newTweets = new TweetStorage();
    TweetStorage allTweets = new TweetStorage();
    List<Cluster> clusters = null;
    TwitterRest restAPI = new TwitterRest(); 
    
    public void onStatus(Status status) {
        Tweet tweet = Tweet.createTweet(status);
        newTweets.add(tweet);
        
        tweet = tweet.clone();
        Preprocessor.processTweet(tweet);
    	if(Filter.filterTweet(tweet))
    	{
    		allTweets.add(tweet);
    		try {
				allTweets.addAll(restAPI.getUserTimeline3days(tweet.getUserID(),new Date(),tweet));
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
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