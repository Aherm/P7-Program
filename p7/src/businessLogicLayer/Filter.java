package businessLogicLayer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class Filter {

	public static TweetStorage filterTweets(TweetStorage tweets, Date date) {
		TweetStorage newTweetStorage = new TweetStorage();
		Map<String,String> regs = getRegularExpressions();
		String reg1 = ".*"; 		// Any character 0-many times
		String reg2 = "\\s@?"; 		// space followed by a @ zero or one time
		String reg3 = ""; 			// Any of the regular expressions in regs(list of regular expressions)					
		String reg4 = "\\w?\\s?"; 	// Any letter or digit zero or one time followed by a space

		for (int i = tweets.size() - 1; i >= 0; i--) {
			Tweet tweet = tweets.get(i);
			if (tweet.getCreatedAt().before(date)) {
				break;
			}

			for (Map.Entry<String, String> entry : regs.entrySet())
			{
				reg3 = entry.getValue();
				// Full Regex Example:
				// ".*\\s@?(bad?\\s?|upset?\\s?)?stoma(ch|k)\\s?(pain?|flu?|flue?|)\\w?\\s"
				Pattern p = Pattern.compile(reg1 + reg2 + reg3 + reg4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
				Matcher m = p.matcher(tweet.getTweetText());
				if (m.find()) {
					newTweetStorage.add(tweet);
				}
			}
		}
		return newTweetStorage;
	}

	public static Map<String, Integer> countOccurances(TweetStorage tweets){
		Map<String, Integer> counters = new HashMap<String, Integer>();
		Map<String, String> regularExpressions = getRegularExpressions();

		String reg1 = ".*"; 		// Any character 0-many times
		String reg2 = "\\s@?"; 		// space followed by a @ zero or one time
		String reg4 = "\\w?\\s?"; 	// Any letter or digit zero or one time followed by a space
		for (int i = tweets.size() - 1; i >= 0; i--) {
			Tweet tweet = tweets.get(i);

			for (Map.Entry<String, String> entry : regularExpressions.entrySet())
			{
				String regEx = entry.getValue();
				// Full Regex Example:
				// ".*\\s@?(bad?\\s?|upset?\\s?)?stoma(ch|k)\\s?(pain?|flu?|flue?|)\\w?\\s"
				Pattern p = Pattern.compile(reg1 + reg2 + regEx + reg4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
				Matcher m = p.matcher(tweet.getTweetText());
				if (m.find()) {
					// do counter work here
				}
			}
		}

		return counters;
	}


	static private Map<String, String> getRegularExpressions() {
		Map<String,String> regs = new HashMap<String, String>();
		regs.put("food", "fo(od|d|ood|ods|odd)\\s?poi(son|sons|sen|sens|sn)");
		regs.put("sick", "si(ck|k)");
		regs.put("ill", "(ill|il|fever|pain)");
		regs.put("stomach flue","(bad?\\s?|upset?\\s?)?stoma(ch|k)\\s?(pain?|flu?|flue?|)");
		regs.put("diarrhea","dia(rrhea|rria|rhea|ria|hrrhea|hrhea)");
		regs.put("dehydration", "de(hy|hi)dra(tion|sion)");
		regs.put("salmonella", "salmonel(la|a)");
		regs.put("nausea", "nausea");
		regs.put("vomit", "vom(it|mit)");
		regs.put("throwing up", "throw(ing|)\\s?(u|o)p");
		regs.put("pepto bismal", "pe(p|b)to\\s?bi(s|ss)mal");
		regs.put("on the toilet","(on\\s?the\\s?)?toilet");
		regs.put("cramps","(c|k)ram(p|b)s");
		regs.put("infection","infe(c|k)(t|s)ion");
		regs.put("decrease","d(i|e)sease");
		regs.put("headache","head(a|e)(che|k)");

		return regs;
	}

	// ----------------------------TALK TO MATHIAS-------------------------
	public static boolean filterTweet(Tweet tweet) {
		TweetStorage newTweetStorage = new TweetStorage();
		Map<String,String> regs = getRegularExpressions();
		String reg1 = ".*"; // Any character 0-many times
		String reg2 = "\\s@?"; // space followed by a @ zero or one time
		String reg3 = ""; // Any of the regular expressions in regs(list of
							// regular expressions)
		String reg4 = "\\w?\\s?"; // Any letter or digit zero or one time
									// followed by a space
		
		for (Map.Entry<String, String> entry : regs.entrySet())
		{
			reg3 = entry.getValue();
			// Full Regex Example:
			// ".*\\s@?(bad?\\s?|upset?\\s?)?stoma(ch|k)\\s?(pain?|flu?|flue?|)\\w?\\s"
			Pattern p = Pattern.compile(reg1 + reg2 + reg3 + reg4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(tweet.getTweetText());
			if (m.find()) {
				return true;
			}
		}
			
		return false;
	}
}
