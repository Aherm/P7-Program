package streaming;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.Days;

import businessLogicLayer.Filter;
import businessLogicLayer.Preprocessor;
import businessLogicLayer.TwitterRest;
import modelLayer.Cluster;
import modelLayer.ClusterStorage;
import modelLayer.InvertedIndex;
import modelLayer.Tweet;
import modelLayer.TweetStorage;
import twitter4j.*;

public class OurStatusListener implements StatusListener {
	private TweetStorage dbTweets = new TweetStorage();
	private TweetStorage tweets = new TweetStorage();
	private ClusterStorage clusters = new ClusterStorage();
	private TwitterRest restAPI = TwitterRest.getInstance(); 
	private InvertedIndex invertedIndex = new InvertedIndex();

	public void onStatus(Status status) {
		Tweet tweet = Tweet.createTweet(status);
		dbTweets.add(tweet);

		tweet = tweet.clone();
		Preprocessor.processTweet(tweet);
		if(Filter.passesFilter(tweet)) {
			tweets.add(tweet);
			invertedIndex.extractWords(tweet);
			try {
				TweetStorage ts = restAPI.getUserTimeline3days(tweet.getUserID(),new Date(),tweet);
				invertedIndex.extractWords(ts);
				tweets.addAll(ts);
			}
			catch (TwitterException e) {
				e.printStackTrace();
				System.out.println("Stopped because of rateLimit");
				return; 
			}
		}
		removeOldTweetsFromInvertedIndex(3);
		removeOldTweetsFromTweetStorage(3);
	}

	public void removeOldTweetsFromInvertedIndex(int days) {
		Set<Tweet> removalList = new HashSet<Tweet>();
		Date today = new Date();
		
		for(String word : invertedIndex.keySet())
		{
			for(Tweet tweet : invertedIndex.get(word))
			{
				int tweetAge = Days.daysBetween(new DateTime(tweet.getCreatedAt()), new DateTime(today)).getDays();
				if (tweetAge >= days)
					removalList.add(tweet);
			}
			
			invertedIndex.get(word).removeAll(removalList);
			removalList.clear();
		}
	}
	
	public void removeOldTweetsFromTweetStorage(int days) {
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
	
	public InvertedIndex getInvertedIndex() {
		return invertedIndex;
	}
}