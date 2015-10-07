package businessLogicLayer;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class Cluster {
	private Tweet center;
	private TweetStorage tweets;
	
	public Cluster(Tweet c) {
		this.center = c;
	}
	
	public void setCenter(Tweet t) {
		this.center = t;
	}
	
	public Tweet getCenter() {
		return center;
	}
	
	public void setTweets(TweetStorage t) {
		this.tweets = t;
	}
	
	public TweetStorage getTweets() {
		return tweets;
	}
	
	public void addTweet(Tweet t) {
		tweets.add(t);
	}
	
	public void removeTweet(Tweet t) {
		tweets.remove(t);
	}
}
