package streaming;

import modelLayer.Tweet;
import modelLayer.TweetStorage;
import twitter4j.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OurStatusListener implements StatusListener {
    TweetStorage tweets = new TweetStorage();

    public void onStatus(Status status) {
    	GeoLocation geo = status.getGeoLocation();
    	
    	if (geo != null) {
    		tweets.add(Tweet.createTweet(status));
    		// Removes tweets older than 3 days
    		tweets.removeOld(3);
    	
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

    public List<String> containsKeywords(String tweetText) {
        tweetText = tweetText.toLowerCase();
        List<String> matchedKeys = new ArrayList<String>();
        List<String> keywords = new ArrayList<String>();
        keywords.add("food");
        keywords.add("poison");
        keywords.add("restaurant");
        keywords.add("sick");
        keywords.add("soup");
        keywords.add("drink");
        keywords.add("bed");
        keywords.add("hungry");
        keywords.add("soda");
        keywords.add("chinese food");
        keywords.add("chipotle");
        keywords.add("mcdonald");
        keywords.add("mc donald");
        keywords.add("burgerking");
        keywords.add("burger king");
        keywords.add("stomach pain");
        keywords.add("diarrhea");
        keywords.add("the shits");

        // mads trollolol
        //btc temp
        //keywords.add("the");
        //keywords.add("a");

        for (String keyword : keywords) {
            if (tweetText.contains(keyword))
                matchedKeys.add(keyword);
        }
        return matchedKeys;
    }
    
    public TweetStorage getTweets() {
    	return tweets;
    }
}