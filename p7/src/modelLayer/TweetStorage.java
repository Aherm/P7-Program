package modelLayer;

import java.util.Collections;
import java.util.ArrayList;

public class TweetStorage extends ArrayList<Tweet> {
	// This field has to be there. We don't use it.
	private static final long serialVersionUID = -6057556249495829151L;

	public TweetStorage(){

	}

	public TweetStorage(ArrayList<Tweet> tweets){
		super(tweets);
	}

	public TweetStorage clone() {
		return (TweetStorage) super.clone();
	}

	public TweetStorage getRandomizedCopy() {
		TweetStorage copy = this.clone();
		Collections.shuffle(copy);

		return copy;
	}

	public TweetStorage getReverseCopy() {
		TweetStorage copy = this.clone();
		Collections.reverse(copy);

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
	
	public TweetStorage getSickTweets() {
		TweetStorage res = new TweetStorage();
		for (Tweet t : this) {
			if (t.isSick()) {
				res.add(t);
			}
		}
		return res;
	}

	public static TweetStorage getDifference(TweetStorage ts1, TweetStorage ts2) {
		TweetStorage res = ts1.clone();
		res.removeAll(ts2);
		return res;
	}
	
	public static TweetStorage getUnion(TweetStorage ts1, TweetStorage ts2) {
		TweetStorage res = ts1.clone();
		res.removeAll(ts2);
		res.addAll(ts2);
		return res;
	}
}
