package streaming;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;

import businessLogicLayer.Filter;
import businessLogicLayer.Preprocessor;
import businessLogicLayer.TwitterRest;
import modelLayer.Cluster;
import modelLayer.ClusterStorage;
import modelLayer.Tweet;
import modelLayer.TweetStorage;
import twitter4j.*;

public class OurStatusListener implements StatusListener {
	TweetStorage dbTweets = new TweetStorage();
	TweetStorage tweets = new TweetStorage();
	ClusterStorage clusters = new ClusterStorage();
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
		removeOldTweets(3);
	}

	public void removeOldTweets(int days) {
		TweetStorage removalList = new TweetStorage();
		Date today = new Date();

		for (Tweet tweet : tweets) {
			int tweetAge = Days.daysBetween(new DateTime(tweet.getCreatedAt()), new DateTime(today)).getDays();
			if (tweetAge >= days) {
				removalList.add(tweet);
				
				Cluster c = tweet.getCluster();
				if (c != null) {
					if (c.getCenter() == tweet) {
						clusters.remove(c);
					}
					c.removeTweet(tweet);
				}
			}
		}
		tweets.removeAll(removalList);
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
	
	public ClusterStorage getClusters() {
		return clusters;
	}
}