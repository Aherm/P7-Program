package modelLayer;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;

public class TweetStorage implements Iterable<Tweet> {
	private List<Tweet> tweets = new ArrayList<Tweet>();

	public TweetStorage() {}

	public Tweet get(int n) {
		return tweets.get(n);
	}

	public void add(Tweet t) {
		tweets.add(t);
	}

	public void addAll(TweetStorage ts) {
		tweets.addAll(ts.tweets);
	}

	public void remove(Tweet tweet) {
		// TODO Do we want removal from cluster in here? Might get messy if a tweet is in multiple storages, but we only want to remove it from one.
		if (tweets.contains(tweet) && tweet.getCluster() != null) {
			tweet.getCluster().removeTweet(tweet);
		}
		tweets.remove(tweet);
	}
	
	public void removeAll(TweetStorage ts) {
		for (Tweet t : ts) {
			remove(t);
		}
	}

	public void removeOldTweets(int days) {
		TweetStorage removalList = new TweetStorage();
		Date today = new Date();

		for (Tweet tweet : tweets) {
			int tweetAge = Days.daysBetween(new DateTime(tweet.getCreatedAt()), new DateTime(today)).getDays();
			if (tweetAge >= days) {
				removalList.add(tweet);
			}
		}

		removeAll(removalList);
	}

	public void clear() {
		tweets.clear();
	}

	public int size() {
		return tweets.size();
	}
	
	public boolean contains(Tweet t) {
		return tweets.contains(t);
	}

	public TweetStorage clone() {		
		TweetStorage clone = new TweetStorage();
		for(Tweet t : this) {
			clone.add(t);
		}
		return clone;
	}

	public TweetStorage getRandomizedCopy() {
		TweetStorage copy = this.clone();
		Collections.shuffle(copy.tweets);

		return copy;
	}

	public TweetStorage getReverseCopy() {
		TweetStorage copy = this.clone();
		Collections.reverse(copy.tweets);

		return copy;
	}

	public TweetStorage getGeotaggedTweets() {
		TweetStorage res = new TweetStorage();
		for (Tweet t : this) {
			if (t.isGeotagged()) {
				res.add(t);
			}
		}
		return res;
	}

	public TweetStorage getUnclusteredTweets() {
		TweetStorage res = new TweetStorage();
		for (Tweet t : this) {
			if (t.getCluster() == null) {
				res.add(t);
			}
		}
		return res;
	}

	public static TweetStorage getDifference(TweetStorage ts1, TweetStorage ts2) {
		TweetStorage res = new TweetStorage();
		for (Tweet t : ts1) {
			if (!ts2.contains(t)) {
				res.add(t);
			}
		}

		return res;
	}

	public Iterator<Tweet> iterator() {
		return tweets.iterator();
	}
}
