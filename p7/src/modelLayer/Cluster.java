package modelLayer;

public class Cluster {
	private Tweet center;
	private TweetStorage tweets = new TweetStorage();

	public Cluster(Tweet centerTweet) {
		center = centerTweet;
		addTweet(centerTweet);
	}

	public Tweet getCenter() {
		return center;
	}

	public void setCenter(Tweet tweet) {
		center = tweet;
	}

	public TweetStorage getTweets() {
		return tweets;
	}
	
	public void addTweet(Tweet tweet) {
		if (tweet.getCluster() != null) {
			tweet.getCluster().removeTweet(tweet);
		}
		tweet.setCluster(this);
		tweets.add(tweet);
	}
	
	public void removeTweet(Tweet tweet) {
		// TODO Check if tweet is center of the cluster
		tweet.setCluster(null);
		tweets.remove(tweet);
	}
	
	public double getScore() {
		double score = 0;
		for (Tweet t : tweets) {
			score += t.getScore();
		}
		return score / tweets.size();
	}
	
	public int size() {
		return getTweets().size();
	}
}