package businessLogicLayer;

import modelLayer.InvertedIndex;
import modelLayer.Keyword;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Filter {
	static String reg1 = ".*?";     // Any character 0-many times
	static String reg2 = "\\s?@?";  // space followed by a @ zero or one time
	static String reg4 = "\\w?";    // Any letter or digit zero or one time followed by a space

	public static TweetStorage getFilteredTweets(TweetStorage tweets, Date date) {
		TweetStorage newTweetStorage = new TweetStorage();
		List<Keyword> keywordsToMatch = getKeywordsToMatch();
		String reg3 = "";           // Any of the regular expressions in regs(list of regular expressions)

		for (Keyword keyword : keywordsToMatch) {
			reg3 = keyword.getRegex();
			Pattern p = Pattern.compile(reg1 + reg2 + reg3 + reg4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

			for (Tweet tweet : tweets) {
				Matcher m = p.matcher(tweet.getTweetText());
				if (m.find()) {
					if (!newTweetStorage.contains(tweet)) {
						newTweetStorage.add(tweet);
					}
					
					tweet.addKeyword(keyword);
				}
			}
		}
		
		return newTweetStorage;
	}

	public static boolean passesFilter(Tweet tweet) {
		List<Keyword> keywordsToMatch = getKeywordsToMatch();
		String reg3 = ""; // Any of the regular expressions in regularExpressions
		boolean passed = false;
		
		for (Keyword keyword : keywordsToMatch) {
			reg3 = keyword.getRegex();
			Pattern p = Pattern.compile(reg1 + reg2 + reg3 + reg4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(tweet.getTweetText());

			if (m.find()) {
				tweet.addKeyword(keyword);

				passed = true;
			}
		}

		return passed;
	}

	public static Map<String, Integer> countMatches(TweetStorage tweets) {
		List<Keyword> regs = getKeywordsToMatch();
		Map<String, Integer> counters = new HashMap<String, Integer>();
		String reg3 = "";            // Any of the regular expressions in regs(list of regular expressions)
		int counter = 0;

		for (int i = 0; i < regs.size(); i++) {
			reg3 = regs.get(i).getRegex();
			Pattern p = Pattern.compile(reg1 + reg2 + reg3 + reg4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

			for (int n = 0; n < tweets.size(); n++) {
				Tweet tweet = tweets.get(n);
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

	static private List<Keyword> getKeywordsToMatch() {
		List<Keyword> regs = new ArrayList<Keyword>();

		String feelingReg = "fe(e|el|l|)(lin(g|))?";
		String sickReg = "si(ck|k)";
		String stomacheReg = "stoma(ch|k)e";
		String excludeOfReg = "(?!\\sof)";
		regs.add(new Keyword("food poison", "fo(od|d|ood|ods|odd)\\s?pois(on|ons|en|ens|n|oning)", 10));
		regs.add(new Keyword("feeling sick", feelingReg + "\\s?" + sickReg + excludeOfReg, 10));
		regs.add(new Keyword("being sick", "be(ing|)\\s?" + sickReg, 10));
		regs.add(new Keyword("sick of", "i('m|m)\\s?" + sickReg + excludeOfReg, 10));
		regs.add(new Keyword("ill", feelingReg + "\\s?(ill|il)", 10));
		regs.add(new Keyword("stomach flue", stomacheReg + "\\s?(is\\s)?(pain|flu|flue|hurt|hurts|hurting|)", 10));
		regs.add(new Keyword("bad stomach", "(bad|upset)\\s?" + stomacheReg, 10));
		regs.add(new Keyword("diarrhea", "dia(rrhea|rria|rhea|ria|hrrhea|hrhea)", 10));
		regs.add(new Keyword("dehydration", "de(hy|hi)dra(tion|sion)", 10));
		regs.add(new Keyword("salmonella", "salmonel(la|a)", 10));
		regs.add(new Keyword("nausea", feelingReg + "?\\s?nause(a|ous)", 10));
		regs.add(new Keyword("vomit", "vom(it|mit|iting|itin|miting|mitin)", 10));
		regs.add(new Keyword("throwing up", "throw(ing|)\\s?(u|o)p", 10));
		regs.add(new Keyword("pepto bismal", "pe(p|b)to\\s?bi(s|ss)mal", 10));
		regs.add(new Keyword("on the toilet", "on\\s?the\\s?toilet", 10));
		regs.add(new Keyword("cramps", "(c|k)ram(p|b)s", 10));
		regs.add(new Keyword("infection", "infe(c|k)(t|s)ion", 10));
		regs.add(new Keyword("disease", "d(i|e)sease", 10));
		regs.add(new Keyword("headache", "head(a|e)(che|k)", 10));
		return regs;
	}
	//String notToReg = "(^(.(?!to))$)*";
	
	public static boolean matchResturantByName(String restaurant, InvertedIndex invertedIndex)
    {
        List<Set<Tweet>> tweetSet = new ArrayList<Set<Tweet>>();
        String[] restaurantWords = restaurant.split(" ");
        for (String restaurantWord : restaurantWords) {
            String resWord = restaurantWord.toLowerCase();
            for (String word : invertedIndex.keySet()) {
                String w = word.toLowerCase();
                if (resWord.equals(w)) {
                    tweetSet.add(invertedIndex.get(word));
                    break;
                }
            }
        }

        for(int i = 1; i < tweetSet.size(); i++)
        {
            tweetSet.get(0).retainAll(tweetSet.get(i));
        }

        for(Tweet tweet : tweetSet.get(0))
        {
            if(tweet.getTweetText().contains(restaurant))
                return true;
        }

        return false;
    }
}
