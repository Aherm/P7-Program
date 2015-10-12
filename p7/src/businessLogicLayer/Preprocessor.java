package businessLogicLayer;

import java.util.Date;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class Preprocessor {

	// This preprocessing can easily be optimized to n instead of 3n(although
	// exactly the same asymptotically)
	public static void processTweet(TweetStorage tweets, Date date) {
		for (int i = tweets.size() - 1; i >= 0; i--) {
			Tweet tweet = tweets.get(i);
			if (tweet.getCreatedAt().before(date)) {
				break;
			}
			processTweetMentions(tweet);
			processTweetLinks(tweet);
			processTweetSymbols(tweet);
			// remove the last empty space character
			tweet.setTweetText(tweet.getTweetText().substring(0, tweet.getTweetText().length() - 1));
		}
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
			if (word.contains("http://t.co/")) {
				newTweetText += "@LINK ";
				continue;
			}

			newTweetText += word + " ";
		}

		tweet.setTweetText(newTweetText);
	}

	private static void processTweetSymbols(Tweet tweet) {
		String[] tweetText = tweet.getTweetText().split(" ");
		String newTweetText = "";
		for (String word : tweetText) {
			if (word.contains("@") || word.contains("#")) {
				newTweetText += word + " ";
				continue;
			}

			for (int i = 0; i < word.length(); i++) {
				if (Character.isLetter(word.charAt(i)) || Character.isDigit(word.charAt(i))) {
					newTweetText += word.charAt(i);
				}
			}
			newTweetText += " ";
		}

		tweet.setTweetText(newTweetText);
	}
}
