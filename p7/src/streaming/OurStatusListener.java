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
import modelLayer.Grid;
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
	// TODO: Determine the best number of rows and columns. Maybe pass them as argument instead.
	private Grid grid = new Grid(-74, -73, 40, 41, 1000, 1000);
			
	public void onStatus(Status status) {
		Tweet tweet = Tweet.createTweet(status);
		dbTweets.add(tweet);

		tweet = tweet.clone();
		Preprocessor.processTweet(tweet);
		if(Filter.passesFilter(tweet)) {
			tweets.add(tweet);
			grid.addTweet(tweet);
			invertedIndex.addIndex(tweet);
			try {
				TweetStorage ts = restAPI.getUserTimeline3days(tweet.getUserID(),new Date(),tweet);
				removeSeenTweets(ts);
				invertedIndex.addIndices(ts);
				tweets.addAll(ts);
				for (Tweet t : ts) {
					grid.addTweet(t);
				}
			}
			catch (TwitterException e) {
				if (e.getStatusCode() == 420 || e.getStatusCode() == 429){
					System.out.println("Too many requests");
					e.printStackTrace();
				}
				// server overloaded
				if (e.getStatusCode() == 503){
					System.out.println("Twitter is overloaded");
					e.printStackTrace();
				}

				return;
			}
		}
		removeOldTweetsFromTweetStorage(3);
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
	
	private void removeSeenTweets(TweetStorage ts){
		for(int i = 0 ; i < ts.size(); i++){
			ts.get(i).setSick(true);
			if(tweets.contains(ts.get(i))){
				tweets.remove(ts.get(i));
			}
		}
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
	
	public Grid getGrid() {
		return grid;
	}
	
	public InvertedIndex getInvertedIndex() {
		return invertedIndex;
	}
}