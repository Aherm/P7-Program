package businessLogicLayer;

import modelLayer.Tweet;

public class Preprocessor {
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
