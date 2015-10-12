package businessLogicLayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class Filter {

	public static TweetStorage filterTweet(TweetStorage tweets, Date date) {
		TweetStorage newTweetStorage = new TweetStorage();
		List<String> regs = getRegularExpressions();
		String reg1 = ".*"; 		// Any character 0-many times
		String reg2 = "\\s@?"; 		// space followed by a @ zero or one time
		String reg3 = ""; 			// Any of the regular expressions in regs(list of regular expressions)					
		String reg4 = "\\w?\\s?"; 	// Any letter or digit zero or one time followed by a space
		for (int i = tweets.size() - 1; i >= 0; i--) {
			Tweet tweet = tweets.get(i);
			if (tweet.getCreatedAt().before(date)) {
				break;
			}
			for (String reg : regs) {
				reg3 = reg;
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

	static private List<String> getRegularExpressions() {
		List<String> regs = new ArrayList<String>();
		regs.add("fo(od|d|ood|ods|odd)\\s?poi(son|sons|sen|sens|sn)");
		regs.add("si(ck|k)");
		regs.add("(ill|il|fever|pain)");
		regs.add("(bad?\\s?|upset?\\s?)?stoma(ch|k)\\s?(pain?|flu?|flue?|)");
		regs.add("dia(rrhea|rria|rhea|ria|hrrhea|hrhea)");
		regs.add("de(hy|hi)dra(tion|sion)");
		regs.add("salmonel(la|a)");
		regs.add("nausea");
		regs.add("vom(it|mit)");
		regs.add("throw(ing|)\\s?(u|o)p");
		regs.add("pe(p|b)to\\s?bi(s|ss)mal");
		regs.add("(on\\s?the\\s?)?toilet");
		regs.add("(c|k)ram(p|b)s");
		regs.add("infe(c|k)(t|s)ion");
		regs.add("d(i|e)sease");
		regs.add("head(a|e)(che|k)");

		return regs;
	}
}
