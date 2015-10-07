package streaming;

import modelLayer.Tweet;
import modelLayer.TweetStorage;
import twitter4j.*;

public class OurStatusListener implements StatusListener {
    TweetStorage tweets = new TweetStorage();

    public void onStatus(Status status) {
    	GeoLocation geo = status.getGeoLocation();
        //status.isRetweet()

    	if (geo != null && status.getRetweetedStatus() == null) {
    		tweets.add(Tweet.createTweet(status));
    		// Removes tweets older than 3 days
    		tweets.removeOldTweets(3);
    	
    		System.out.println("\n" + status.getUser().getScreenName() + " wrote: ");
    		System.out.println(status.getText());
       	}
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