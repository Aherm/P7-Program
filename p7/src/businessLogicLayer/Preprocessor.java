package businessLogicLayer;

import java.util.Date;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

// TODO: Why we we have two versions of the same processing functions?
public class Preprocessor {
	// This preprocessing can easily be optimized to n instead of 3n(although exactly the same asymptotically)
	public static void processTweets(TweetStorage tweets, Date date) {
		for (int i = tweets.size() - 1; i >= 0; i--) {
			Tweet tweet = tweets.get(i);
			// TODO: I don't think we have the assumption that tweets are stored in chronological order anymore.
			if (tweet.getCreatedAt().before(date)) {
				break;
			}
			processTweetsMentions(tweet);
			processTweetsLinks(tweet);
			processTweetsSymbols(tweet);
			// remove the last empty space character
			tweet.setTweetText(tweet.getTweetText().substring(0, tweet.getTweetText().length() - 1));
		}
	}

	private static void processTweetsMentions(Tweet tweet) {
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

	private static void processTweetsLinks(Tweet tweet) {
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

	private static void processTweetsSymbols(Tweet tweet) {
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

	// ------------------------------TALK TO
	// MATHIAS-----------------------------
	public static void processTweet(Tweet tweet) {
		processTweetMentions(tweet);
		processTweetLinks(tweet);
		processTweetSymbols(tweet);
		// remove the last empty space character
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
