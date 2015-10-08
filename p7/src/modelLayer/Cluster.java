package modelLayer;

public class Cluster {
	private Tweet center;
	private TweetStorage tweets;
	
	public Cluster(Tweet center) {
		this.center = center;
	}
	
	public void setCenter(Tweet tweet) {
		this.center = tweet;
	}
	
	public Tweet getCenter() {
		return center;
	}
	
	public void setTweets(TweetStorage tweets) {
		this.tweets = tweets;
	}
	
	public TweetStorage getTweets() {
		return tweets;
	}
	
	public void addTweet(Tweet tweet) {
		tweets.add(tweet);
	}
	
	public void removeTweet(Tweet tweet) {
		tweets.remove(tweet);
	}
}
