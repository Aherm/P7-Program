package businessLogicLayer;

import java.util.ArrayList;
import java.util.List;

import modelLayer.Tweet;

public class Filter {

	private static String newTweetText = "";
    public static List<String> containsKeywords(String tweetText) {
        tweetText = tweetText.toLowerCase();
        List<String> matchedKeys = new ArrayList<String>();
        List<String> keywords = new ArrayList<String>();
        keywords.add("food");
        keywords.add("poison");
        keywords.add("restaurant");
        keywords.add("sick");
        keywords.add("soup");
        keywords.add("drink");
        keywords.add("bed");
        keywords.add("hungry");
        keywords.add("soda");
        keywords.add("chinese food");
        keywords.add("chipotle");
        keywords.add("mcdonald");
        keywords.add("mc donald");
        keywords.add("burgerking");
        keywords.add("burger king");
        keywords.add("stomach pain");
        keywords.add("diarrhea");
        keywords.add("the shits");

        for (String keyword : keywords) {
            if (tweetText.contains(keyword))
                matchedKeys.add(keyword);
        }
        return matchedKeys;
    }
    
    public static void filterTweet(Tweet tweet)
    {
    	String[] text = tweet.getTweetText().split(" ");
    	String newTweetText = "";
    	for(String s : text)
    	{
    		for(int i = 0; i < s.length(); i++)
    		{
    			if(Character.isLetter(s.charAt(i)) || Character.isDigit(s.charAt(i)))
    			{
    				newTweetText += s.charAt(i);
    			}
    		}
    		newTweetText += " ";
    	}
    	
    	tweet.setTweetText(newTweetText);
    }
}
