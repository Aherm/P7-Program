package modelLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    
    public void init(){
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
    		counter++; 
    	}
    	
    	for(String s: stringList){
    		spaceRemovedList.add(s.replaceAll("\\s+", ""));
    	}
    }
    
       
    public void addIndex(Tweet tweet) {
    	
    	int counter = 0; 
    	String lastString = ""; 
    	for(String word : stringList)
    	{
    		if(tweet.getTweetText().toLowerCase().contains(word) || tweet.getTweetText().toLowerCase().contains(spaceRemovedList.get(counter))){
    			Matcher m = patterns[counter].matcher(tweet.getTweetText()); 
    			if(m.find()){
    				if(word.length() > lastString.length()){
    					lastString = word; 
    				}
    			}
    		}
    		counter++; 
    	}
    	
    	if(!lastString.isEmpty()){
    		this.get(lastString).add(tweet); 
    	}
    }
    
    
    
    public void addIndices(TweetStorage tweets) {
    	for(Tweet tweet : tweets) {
    		addIndex(tweet);
    	}
    }


	public TweetStorage nameQuery(Restaurant restaurant) {
	    List<Set<Tweet>> tweetSet = new ArrayList<Set<Tweet>>();
	    TweetStorage tweetStorage = new TweetStorage();
	    String[] restaurantWords = restaurant.getName().split(" ");
	    for (String restaurantWord : restaurantWords) {
	        String resWord = restaurantWord;
	        for (String word : this.keySet()) {
	            String w = word;
	            if (resWord.equals(w)) {
	                tweetSet.add(this.get(word));
	                break;
	            }
	        }
	    }
	    
	    //if no words in inverted index matched a word of a restaurant
	    if (restaurantWords.length != tweetSet.size()) 
	        return tweetStorage;
	
	    for (int i = 1; i < tweetSet.size(); i++) {
	        tweetSet.get(0).retainAll(tweetSet.get(i));
	    }
	
	    //if the intersection results in an empty list
	    if (tweetSet.isEmpty())
	        return tweetStorage;
	
	    for (Tweet tweet : tweetSet.get(0)) {
	        if (tweet.getTweetText().contains(restaurant.getName()))
	        	tweetStorage.add(tweet);
	    }
	
	    return tweetStorage;
	}
}
