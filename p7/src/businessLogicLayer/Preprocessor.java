package businessLogicLayer;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class Preprocessor {
	public static void processTweets(TweetStorage tweets) {
		for(Tweet tweet : tweets) {
			processTweet(tweet);
		}
	}
	
	public static void processTweet(Tweet tweet) {
		//processTweetMentions(tweet);
		processTweetLinks(tweet);
		processTweetSymbols(tweet);
		// remove the last empty space character
		if(tweet.getTweetText().length() == 0)
			tweet.setTweetText("");
		else
			tweet.setTweetText(tweet.getTweetText().substring(0, tweet.getTweetText().length() - 1));
	}

	private static void processTweetMentions(Tweet tweet) {
		String[] tweetText = tweet.getTweetText().split(" ");
		String newTweetText = "";
		for (String word : tweetText) {
			if (word.contains("@") && !(word.contains("@LINK"))) {
				newTweetText += "@MENTION ";
				continue;
			}

			newTweetText += word + " ";
		}

		tweet.setTweetText(newTweetText);
	}

	private static void processTweetLinks(Tweet tweet) {
		String[] tweetText = tweet.getTweetText().split(" ");
		String newTweetText = "";
		for (String word : tweetText) {
			if (word.contains("https://t.co/")) {
				newTweetText += "@LINK ";
				continue;
			}

			newTweetText += word + " ";
		}

		tweet.setTweetText(newTweetText);
	}

	private static void processTweetSymbols(Tweet tweet) {
		String[] tweetText = tweet.getTweetText().split(" ");
		String tweetWord = "";
		String newTweetText = "";
		for (String word : tweetText) {
			if (word.contains("@") || word.contains("#")) {
				newTweetText += word + " ";
				continue;
			}

			for (int i = 0; i < word.length(); i++) {
				if (Character.isLetter(word.charAt(i)) || Character.isDigit(word.charAt(i))) {
					tweetWord += word.charAt(i);
				}
			}
			
			if(tweetWord.equals("@MENTION") || tweetWord.equals("@LINK"))
			{
				newTweetText += tweetWord + " ";
			}
			else
			{
				newTweetText += tweetWord + " ";
			}
			
			tweetWord = "";
		}

		tweet.setTweetText(newTweetText.toLowerCase());
	}
}
