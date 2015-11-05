package modelLayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class InvertedIndex extends HashMap<String, Set<Tweet>> {
    public void extractWords(Tweet tweet) {
        for (String word : tweet.getTweetText().split(" ")){
            addIndex(word, tweet);
        }
    }

    private void addIndex(String word, Tweet newTweet) {
        // if contains => true then add tweet to set
        if (this.containsKey(word)) {
            Set<Tweet> tweetSet = get(word);
            tweetSet.add(newTweet);
            put(word, tweetSet);
        }
        // if contains => false then new word -> put word + new Set
        else {
            Set<Tweet> tweetSet = new HashSet<Tweet>();
            tweetSet.add(newTweet);
            put(word, tweetSet);
        }
    }
}
