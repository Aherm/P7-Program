package businessLogicLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import modelLayer.Keyword;
import modelLayer.Tweet;

public class Filter {

    //public static List<String> containsKeywords(String tweetText) {
    public static void containsKeywords(Tweet tweet) {
        String tweetText = tweet.getTweetText().toLowerCase();
        List<Keyword> keywords = new ArrayList<Keyword>();

        keywords.add(new Keyword("food poison", 10));
        keywords.add(new Keyword("sick", 1));
        keywords.add(new Keyword("stomach pain", 2));
        keywords.add(new Keyword("diarrhea", 5));
        keywords.add(new Keyword("dehydration", 5));
        keywords.add(new Keyword("salmonella", 5));
        keywords.add(new Keyword("nausea", 4));
        keywords.add(new Keyword("vomit", 5));
        keywords.add(new Keyword("cramps", 2));
        keywords.add(new Keyword("pain", 1));
        keywords.add(new Keyword("fever", 1));
        keywords.add(new Keyword("ill", 1));
        keywords.add(new Keyword("infection", 1));
        keywords.add(new Keyword("disease", 2));
        keywords.add(new Keyword("headache", 1));
        keywords.add(new Keyword("stomach flu", 5));
        //pepto bismal
        //throwing up
        //throw up
        //bad stomach
        //on the toilet
        //toilet
        //upset stomach
        
        
        int score = 0;

        for (Keyword keyword : keywords) {
            if (tweetText.contains(keyword.getName()))
                score += keyword.getWeight();
        }

        tweet.setScore(score);
    }

    public static boolean filterTweet(Tweet tweet)
	{
    	List<String> regs = getRegularExpressions();
    	String reg1 = ".*";				//Any character 0-many times
    	String reg2 = "\\s@?";			//space followed by a @ zero or one time
    	String reg3 = "";				//Any of the regular expressions in regs(list of regular expressions)
    	String reg4 = "\\w?\\s";		//Any letter or digit zero or one time followed by a space
    	for(String reg : regs)
    	{
    		reg3 = reg;
    		//Full Regex Example: ".*\\s@?fo(od|d|ood|ods|odd)\\w?\\s"
    		Pattern p = Pattern.compile(reg1 + reg2 + reg3 + reg4, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    		Matcher m = p.matcher(tweet.getTweetText());
    		if(m.find())
    		{
    			return true;
    		}
    	}
    
    	return false;
	}
    
    static private List<String> getRegularExpressions()
    {
    	List<String> regs = new ArrayList<String>();
    	regs.add("fo(od|d|ood|ods|odd)");
    	regs.add("poi(son|sons|sen|sens|sn)");
    	regs.add("fo(od|d|ood|ods|odd)\\s?poi(son|sons|sen|sens|sn)");
    	regs.add("si(ck|k)");
    	regs.add("stoma(ch|k)\\s?(pain|flu)");
    	regs.add("dia(rrhea|rria|rhea|ria|hrrhea|hrhea)");
    	regs.add("de(hy|hi)dra(tion|sion)"); 
    	regs.add("salmonel(la|a)");
    	regs.add("nausea");
    	
    	return regs;
    }
}
