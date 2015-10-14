package businessLogicLayer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import modelLayer.Keyword;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class Filter {
    static String reg1 = "[.*]?";        // Any character 0-many times
    static String reg2 = "[\\s@?]?";        // space followed by a @ zero or one time
    static String reg4 = "[\\w?\\s?]?";    // Any letter or digit zero or one time followed by a space

    public static TweetStorage filterTweets(TweetStorage tweets, Date date) {
        TweetStorage newTweetStorage = new TweetStorage();
        List<Keyword> regularExpressions = getRegularExpressions();
        String reg3 = "";            // Any of the regular expressions in regs(list of regular expressions)

        for (Keyword keyword : regularExpressions) {
            reg3 = keyword.getRegex();
            Pattern p = Pattern.compile(reg1 + reg2 + reg3 + reg4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

            for (int i = tweets.size() - 1; i >= 0; i--) {
                Tweet tweet = tweets.get(i);
                if (tweet.getCreatedAt().before(date)) {
                    continue;
                }
                Matcher m = p.matcher(tweet.getTweetText());
                if (m.find()) {
                    if (!tweet.isAddedToStorage()) {
                        newTweetStorage.add(tweet);
                        tweet.setAddedToStorage(true);
                    }

                    tweet.add(keyword);
                }
            }
        }
        return newTweetStorage.getReverseCopy();
    }

    public static boolean filterTweet(Tweet tweet) {
        List<Keyword> regularExpressions = getRegularExpressions();
        String reg3 = ""; // Any of the regular expressions in regularExpressions
        for (Keyword keyword : regularExpressions) {
            reg3 = keyword.getRegex();
            Pattern p = Pattern.compile(reg1 + reg2 + reg3 + reg4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(tweet.getTweetText());

            //Pattern.matches(reg1 + reg2 + reg3 + reg4, tweet.getTweetText())

            if (m.find()) {
                tweet.add(keyword);

                if (!tweet.isAddedToStorage())
                    tweet.setAddedToStorage(true);

            }
        }
        if (tweet.isAddedToStorage())
            return true;
        else
            return false;
    }

    public static Map<String, Integer> countMatches(TweetStorage tweets) {
        List<Keyword> regs = getRegularExpressions();
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


    static private List<Keyword> getRegularExpressions() {
        List<Keyword> regs = new ArrayList<Keyword>();
        regs.add(new Keyword("food poison", "fo(od|d|ood|ods|odd)\\s?poi(son|sons|sen|sens|sn)", 10));
        regs.add(new Keyword("sick", "si(ck|k)", 10));
        regs.add(new Keyword("ill", "(ill|il|fever|pain)", 10));
        regs.add(new Keyword("stomach flue", "stoma(ch|k)\\s?(pain|flu|flue|)", 10));
        regs.add(new Keyword("stomach flue", "(bad|upset)\\s?stoma(ch|k)", 10));
        regs.add(new Keyword("diarrhea", "dia(rrhea|rria|rhea|ria|hrrhea|hrhea)", 10));
        regs.add(new Keyword("dehydration", "de(hy|hi)dra(tion|sion)", 10));
        regs.add(new Keyword("salmonella", "salmonel(la|a)", 10));
        regs.add(new Keyword("nausea", "nausea", 10));
        regs.add(new Keyword("vomit", "vom(it|mit)", 10));
        regs.add(new Keyword("throwing up", "throw(ing|)\\s?(u|o)p", 10));
        regs.add(new Keyword("pepto bismal", "pe(p|b)to\\s?bi(s|ss)mal", 10));
        regs.add(new Keyword("on the toilet", "(on\\s?the\\s?)?toilet", 10));
        regs.add(new Keyword("cramps", "(c|k)ram(p|b)s", 10));
        regs.add(new Keyword("infection", "infe(c|k)(t|s)ion", 10));
        regs.add(new Keyword("disease", "d(i|e)sease", 10));
        regs.add(new Keyword("headache", "head(a|e)(che|k)", 10));
        return regs;
    }
}
