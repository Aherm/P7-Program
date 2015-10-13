package businessLogicLayer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import modelLayer.Keyword;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class Filter {

	public static TweetStorage filterTweets(TweetStorage tweets, Date date) {
		TweetStorage newTweetStorage = new TweetStorage();
		Map<String,String> regs = getRegularExpressions();
		String reg1 = "[.*]?"; 		// Any character 0-many times
		String reg2 = "[\\s@?]?"; 		// space followed by a @ zero or one time
		String reg3 = ""; 			// Any of the regular expressions in regs(list of regular expressions)					
		String reg4 = "[\\w?\\s?]?"; 	// Any letter or digit zero or one time followed by a space

		for (int i = tweets.size() - 1; i >= 0; i--) {
			Tweet tweet = tweets.get(i);
			if (tweet.getCreatedAt().before(date)) {
				continue;
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
					break;
				}
			}
		}
		return newTweetStorage.getReverseCopy();
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
	public static Map<String, Integer> countMatches(TweetStorage tweets)
	{
		List<Keyword> regs = getRegs();
		Map<String, Integer> counters = new HashMap<String, Integer>();
		String reg1 = "[.*]?"; 		// Any character 0-many times
		String reg2 = "[\\s@?]?"; 		// space followed by a @ zero or one time
		String reg3 = ""; 			// Any of the regular expressions in regs(list of regular expressions)					
		String reg4 = "[\\w?\\s?]?"; 	// Any letter or digit zero or one time followed by a space
		int counter = 0;
		
		for(int i = 0; i < regs.size(); i++)
		{
			for(int n = 0; n < tweets.size(); n++)
			{
				Tweet tweet = tweets.get(n);
				reg3 = regs.get(i).getRegex();
				Pattern p = Pattern.compile(reg1 + reg2 + reg3 + reg4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
				Matcher m = p.matcher(tweet.getTweetText());
				if (m.find()) {
					counter++;
				}
			}
			counters.put(regs.get(i).getName(), counter);
			counter = 0;
		}
		return counters;
	}
	
	public static TweetStorage filterTweetsList(TweetStorage tweets, Date date) {
		List<Keyword> regs = getRegs();
		TweetStorage newTweetStorage = new TweetStorage();
		String reg1 = "[.*]?"; 		// Any character 0-many times
		String reg2 = "[\\s@?]?"; 		// space followed by a @ zero or one time
		String reg3 = ""; 			// Any of the regular expressions in regs(list of regular expressions)					
		String reg4 = "[\\w?\\s?]?"; 	// Any letter or digit zero or one time followed by a space

		for (int i = tweets.size() - 1; i >= 0; i--) {
			Tweet tweet = tweets.get(i);
			if (tweet.getCreatedAt().before(date)) {
				continue;
			}

			for (Keyword keyword : regs)
			{
				reg3 = keyword.getRegex();
				// Full Regex Example:
				// ".*\\s@?(bad?\\s?|upset?\\s?)?stoma(ch|k)\\s?(pain?|flu?|flue?|)\\w?\\s"
				Pattern p = Pattern.compile(reg1 + reg2 + reg3 + reg4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
				Matcher m = p.matcher(tweet.getTweetText());
				if (m.find()) {
					newTweetStorage.add(tweet);
					//do something here to add keyword matched to tweet
				}
			}
		}
		return newTweetStorage.getReverseCopy();
	}
	
	static private List<Keyword> getRegs()
	{
		List<Keyword> regs = new ArrayList<Keyword>();
		regs.add(new Keyword("food poison", "fo(od|d|ood|ods|odd)\\s?poi(son|sons|sen|sens|sn)", 10));
		regs.add(new Keyword("sick", "si(ck|k)", 10));
		regs.add(new Keyword("ill", "(ill|il|fever|pain)", 10));
		regs.add(new Keyword("stomach flue","(bad?\\s?|upset?\\s?)?stoma(ch|k)\\s?(pain?|flu?|flue?|)", 10));
		regs.add(new Keyword("diarrhea","dia(rrhea|rria|rhea|ria|hrrhea|hrhea)", 10));
		regs.add(new Keyword("dehydration", "de(hy|hi)dra(tion|sion)", 10));
		regs.add(new Keyword("salmonella", "salmonel(la|a)", 10));
		regs.add(new Keyword("nausea", "nausea", 10));
		regs.add(new Keyword("vomit", "vom(it|mit)", 10));
		regs.add(new Keyword("throwing up", "throw(ing|)\\s?(u|o)p", 10));
		regs.add(new Keyword("pepto bismal", "pe(p|b)to\\s?bi(s|ss)mal", 10));
		regs.add(new Keyword("on the toilet","(on\\s?the\\s?)?toilet", 10));
		regs.add(new Keyword("cramps","(c|k)ram(p|b)s", 10));
		regs.add(new Keyword("infection","infe(c|k)(t|s)ion", 10));
		regs.add(new Keyword("disease","d(i|e)sease", 10));
		regs.add(new Keyword("headache","head(a|e)(che|k)", 10));
		return regs;
	}
	
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
