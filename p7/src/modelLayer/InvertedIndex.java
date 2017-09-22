package modelLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/** 
 * @author Mads
 * InvertedIndex to index tweets eg. Mcdonalds -> tweet1, tweet2..
 * 
 * keys are always resturant names  
 * 
 * A tweet is only added if any tweet text mathces
 * the regex in the precomputeResturantPatterns() function
 */
public class InvertedIndex extends HashMap<String, Set<Tweet>> {
	// This field has to be there. We don't use it.
	private static final long serialVersionUID = -9190464032994889522L;
    private Pattern[] patterns; 
    private ArrayList<String> stringList = new ArrayList<String>(); 
	private ArrayList<String> spaceRemovedList = new ArrayList<String>();

	public void addEntry(Restaurant restaurant) {
    	if(!(this.containsKey(restaurant.getName()))) {
        	Set<Tweet> tweetSet = new HashSet<Tweet>();
        	put(restaurant.getName(), tweetSet);
    	}
    }
 
    public void precomputeResturantPatterns(){
    	patterns = new Pattern[this.keySet().size()];
    	stringList.addAll(this.keySet()); 
    	int counter = 0; 
    	
    	for(String word : stringList){
    		String newWord = word;
    		String regex = "(@|#|the|cafe|restaurant|\\s)" 
    				        + "(" + newWord + "|" + newWord.replaceAll("\\s+","") + ")" +
    				       "(restaurant|cafe|nyc|ny|\\s?)\\s";
    		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    		patterns[counter] = p; 
    		spaceRemovedList.add(word.replaceAll("\\s+", ""));
    		counter++; 
    	}
    }
    
    public void addIndex(Tweet tweet){
    	int counter = 0; 
    	String lastString = "";
    	int lastCounter = 0; 
    	
    	// First determine whether the tweet includes one of the key names (resturant names) 
    	// Only match the longest resturant name
    	for(String word : stringList)
    	{
    		if(tweet.getTweetText().contains(word) || tweet.getTweetText().contains(spaceRemovedList.get(counter))){	
    				if(word.length() > lastString.length()){
    					lastString = word;
    					lastCounter = counter;
    				}
			}
    		counter++; 
    	}
    	
    	//Determine wheter the tweet matches the resurant regex
    	if(!lastString.isEmpty()){
    		Pattern p = patterns[lastCounter];
    		Matcher m = p.matcher(tweet.getTweetText());	
    		if(m.find())
    			this.get(lastString).add(tweet); 
    	}
    }
    
    public void addIndices(TweetStorage tweets){
    	for(Tweet tweet : tweets) {
    		addIndex(tweet);
    	}
    }

	public void removeTweet(Tweet tweet){
		for(String s: this.keySet()){
			if(this.get(s).contains(tweet)){
				this.get(s).remove(tweet);
			}
		}
	}

	public void removeTweets(TweetStorage ts){
		for(Tweet t: ts){
			removeTweet(t);
		}
	}

	public TweetStorage nameQuery(Restaurant restaurant) {
	 TweetStorage values = new TweetStorage(); 
	 if(this.get(restaurant.getName()) == null)
		 return values; 
	 values.addAll(this.get(restaurant.getName()));
	 return values; 
	}


}
