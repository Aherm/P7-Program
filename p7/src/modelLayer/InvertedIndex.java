package modelLayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InvertedIndex extends HashMap<String, Set<Tweet>> {
	// This field has to be there. We don't use it.
	private static final long serialVersionUID = -9190464032994889522L;
    
    public void addEntry(Restaurant restaurant) {
    	if(!(this.containsKey(restaurant.getName()))) {
        	Set<Tweet> tweetSet = new HashSet<Tweet>();
        	put(restaurant.getName(), tweetSet);
    	}
    }
    
    public void addIndex(Tweet tweet) {
    	for(String word : this.keySet())
    	{
    		if(tweet.getTweetText().contains(" " + word + " "))
    			this.get(word).add(tweet);
    	}
    }
    
    public void addIndices(TweetStorage tweets) {
    	for(Tweet tweet : tweets) {
    		addIndex(tweet);
    	}
    }
}
