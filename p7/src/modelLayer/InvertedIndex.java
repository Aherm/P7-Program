package modelLayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    		String regex1 = "(@|\\s?|#)"+ word +"(nyc|ny|\\s?)"; 
    		Pattern p = Pattern.compile(regex1, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(tweet.getTweetText()); 
            boolean test1 = m.find(); 
            String regex2 = "(@|\\s?|#)"+ word.replaceAll("\\s+", "") +"(nyc|ny|\\s?)"; 
            p = Pattern.compile(regex2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            m = p.matcher(tweet.getTweetText()); 
            boolean test2 = m.find(); 
    		
            if(test1 || test2)
    			this.get(word).add(tweet);
    	}
    }
    
    public void addIndices(TweetStorage tweets) {
    	for(Tweet tweet : tweets) {
    		addIndex(tweet);
    	}
    }
}
