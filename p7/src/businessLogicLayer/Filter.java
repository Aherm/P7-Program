package businessLogicLayer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class Filter {
	static String reg1 = "[.*]?"; 		// Any character 0-many times
	static String reg2 = "[\\s@?]?"; 		// space followed by a @ zero or one time
	static String reg4 = "[\\w?\\s?]?"; 	// Any letter or digit zero or one time followed by a space

	public static TweetStorage filterTweets(TweetStorage tweets, Date date) {
		TweetStorage newTweetStorage = new TweetStorage();
		Map<String,String> regs = getRegularExpressions();

		String reg3 = ""; 			// Any of the regular expressions in regs(list of regular expressions)					


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

	public static Map<String, Integer> countOccurrences(TweetStorage tweets){
		Map<String, Integer> counters = new HashMap<String, Integer>();
		Map<String, String> regularExpressions = getRegularExpressions();

		int food = 0, sick = 0, ill = 0, stomachFlue = 0, diarrhea = 0, dehydration = 0, salmonella = 0, nausea = 0,
				vomit = 0, throwingUp = 0, peptoBismal = 0, onTheToilet = 0, cramps = 0, infection = 0, disease = 0,
				headache = 0;


		for (int i = tweets.size() - 1; i >= 0; i--) {
			Tweet tweet = tweets.get(i);

			for (Map.Entry<String, String> entry : regularExpressions.entrySet())
			{
				String key = entry.getKey();
				String regEx = entry.getValue();
				// Full Regex Example:
				// ".*\\s@?(bad?\\s?|upset?\\s?)?stoma(ch|k)\\s?(pain?|flu?|flue?|)\\w?\\s"
				Pattern p = Pattern.compile(reg1 + reg2 + regEx + reg4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
				Matcher m = p.matcher(tweet.getTweetText());

				//if a match is found
				if (m.find()) {
					if (key.equals("food")){
						food++;
						//counterEntry.setValue(counterEntry.getValue() + 1);
					}
					else if (key.equals("sick"))
						sick++;
					else if (key.equals("ill"))
						ill++;
					else if (key.equals("stomach flue"))
						stomachFlue++;
					else if (key.equals("diarrhea"))
						diarrhea++;
					else if (key.equals("dehydration"))
						dehydration++;
					else if (key.equals("salmonella"))
						salmonella++;
					else if (key.equals("nausea"))
						nausea++;
					else if (key.equals("vomit"))
						vomit++;
					else if (key.equals("throwing up"))
						throwingUp++;
					else if (key.equals("pepto bismal"))
						peptoBismal++;
					else if (key.equals("on the toilet"))
						onTheToilet++;
					else if (key.equals("cramps"))
						cramps++;
					else if (key.equals("infection"))
						infection++;
					else if (key.equals("decrease"))
						disease++;
					else if (key.equals("headache"))
						headache++;
				}
			}

			counters.put("food",food);
			counters.put("sick",sick);
			counters.put("ill",ill);
			counters.put("stomach flue",stomachFlue);
			counters.put("diarrhea",diarrhea);
			counters.put("dehydration",dehydration);
			counters.put("salmonella",salmonella);
			counters.put("nausea",nausea);
			counters.put("vomit",vomit);
			counters.put("throwing up",throwingUp);
			counters.put("pepto bismal",peptoBismal);
			counters.put("on the toilet",onTheToilet);
			counters.put("cramps",cramps);
			counters.put("infection",infection);
			counters.put("disease",disease);
			counters.put("headache",headache);
		}

		return counters;
	}

	private static Map<String, String> getRegularExpressions() {
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
		regs.put("disease","d(i|e)sease");
		regs.put("headache","head(a|e)(che|k)");

		return regs;
	}

	// ----------------------------TALK TO MATHIAS-------------------------
	public static boolean filterTweet(Tweet tweet) {
		TweetStorage newTweetStorage = new TweetStorage();
		Map<String,String> regs = getRegularExpressions();
		String reg3 = ""; // Any of the regular expressions in regs(list of // regular expressions)
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
