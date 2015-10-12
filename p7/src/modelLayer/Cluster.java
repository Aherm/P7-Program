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

	public static void reassignTweet(Tweet t, Cluster c) {
		if (t.getCluster() != null) {
			t.getCluster().removeTweet(t);
		}
		t.setCluster(c);
		c.addTweet(t);
	}

	public static Cluster createCluster(Tweet centerTweet) {
		if (centerTweet.getCluster() != null) {
			centerTweet.getCluster().removeTweet(centerTweet);
		}		
		Cluster cluster = new Cluster(centerTweet);
		centerTweet.setCluster(cluster);
		cluster.addTweet(centerTweet);
		
		return cluster;
	}
}
