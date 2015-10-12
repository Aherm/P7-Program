package streaming;

import businessLogicLayer.Filter;
import businessLogicLayer.Preprocessor;
import modelLayer.Tweet;
import modelLayer.TweetStorage;
import twitter4j.*;

public class OurStatusListener implements StatusListener {
    TweetStorage tweets = new TweetStorage();

    public void onStatus(Status status) {
        Tweet tweet = Tweet.createTweet(status);
    	//Insert raw tweet into DB?
        //Preprocessor.processTweet(tweet);
    	//if(Filter.filterTweet(tweet))
    	//{
    	//	tweets.add(tweet);
    	//}
    	
    	tweets.add(tweet);
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

    public TweetStorage getTweets() {
        return tweets;
    }
}