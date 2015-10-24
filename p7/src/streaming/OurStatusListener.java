package streaming;

import java.util.Date;
import businessLogicLayer.Filter;
import businessLogicLayer.Preprocessor;
import businessLogicLayer.TwitterRest;
import modelLayer.Tweet;
import modelLayer.TweetStorage;
import twitter4j.*;

public class OurStatusListener implements StatusListener {
	TweetStorage dbTweets = new TweetStorage();
	TweetStorage tweets = new TweetStorage();
	TwitterRest restAPI = new TwitterRest(); 

	public void onStatus(Status status) {
		Tweet tweet = Tweet.createTweet(status);
		dbTweets.add(tweet);

		tweet = tweet.clone();
		Preprocessor.processTweet(tweet);
		if(Filter.passesFilter(tweet)) {
			tweets.add(tweet);
			try {
				// TODO: Do we want to add user timeline to database?
				tweets.addAll(restAPI.getUserTimeline3days(tweet.getUserID(),new Date(),tweet));
			}
			catch (TwitterException e) {
				e.printStackTrace();
				System.out.println("Stopped because of rateLimit");
				return; 
			}
		}
		tweets.removeOldTweets(3);
	}

	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
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

	public TweetStorage getDBTweets() {
		return dbTweets;
	}

	public TweetStorage getTweets() {
		return tweets;
	}
}