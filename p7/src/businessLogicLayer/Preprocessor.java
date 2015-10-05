package businessLogicLayer;

import modelLayer.Tweet;

public class Preprocessor {
	
	public static void processTweet(Tweet tweet)
	{
		String[] tweetText = tweet.getTweetText().split(" ");
		String newTweetText = "";
		for(String word : tweetText)
		{
			if(word.contains("@") && !(word.contains("@LINK")))
			{
				newTweetText += "@MENTION ";
				continue;
			}
			
			if(word.contains("http://t.co/"))
			{
				newTweetText += "@LINK ";
				continue;
			}
			
			for(int i = 0; i < word.length(); i++)
    		{
    			if(Character.isLetter(word.charAt(i)) || Character.isDigit(word.charAt(i)))
    			{
    				newTweetText += word.charAt(i);
    			}
    		}
			
			newTweetText += " ";
		}
		
		tweet.setTweetText(newTweetText);
	}
	
	public static void processWholeTweet(Tweet tweet)
	{
		processTweetMentions(tweet);
		processTweetLinks(tweet);
		processTweetSymbols(tweet);
	}
	
	public static void processTweetMentions(Tweet tweet)
	{
		String[] tweetText = tweet.getTweetText().split(" ");
		String newTweetText = "";
		for(String word : tweetText)
		{
			if(word.contains("@") && !(word.contains("@LINK")))
			{
				newTweetText += "@MENTION ";
				continue;
			}
			
			newTweetText += word + " ";
		}
		
		tweet.setTweetText(newTweetText);
	}
	
	public static void processTweetLinks(Tweet tweet)
	{
		String[] tweetText = tweet.getTweetText().split(" ");
		String newTweetText = "";
		for(String word : tweetText)
		{
			if(word.contains("http://t.co/"))
			{
				newTweetText += "@LINK ";
				continue;
			}
			
			newTweetText += word + " ";
		}
		
		tweet.setTweetText(newTweetText);
	}
	
	public static void processTweetSymbols(Tweet tweet)
    {
    	String[] tweetText = tweet.getTweetText().split(" ");
    	String newTweetText = "";
    	for(String word : tweetText)
    	{
    		if(word.contains("@"))
    		{
    			newTweetText += word + " ";
    			continue;
    		}
    		
    		for(int i = 0; i < word.length(); i++)
    		{
    			if(Character.isLetter(word.charAt(i)) || Character.isDigit(word.charAt(i)))
    			{
    				newTweetText += word.charAt(i);
    			}
    		}
    		newTweetText += " ";
    	}
    	
    	tweet.setTweetText(newTweetText);
    }
}
