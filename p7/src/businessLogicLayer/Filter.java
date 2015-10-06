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
        
        keywords.add(new Keyword("food", 1));
        keywords.add(new Keyword("poison", 1));
        keywords.add(new Keyword("food poison", 10));
        keywords.add(new Keyword("restaurant", 1));
        keywords.add(new Keyword("sick", 1));
        keywords.add(new Keyword("soup", 1));
        keywords.add(new Keyword("drink", 1));
        keywords.add(new Keyword("bed", 1));
        keywords.add(new Keyword("hungry", 1));
        keywords.add(new Keyword("soda", 1));
        keywords.add(new Keyword("chinese food", 1));
        keywords.add(new Keyword("chipotle", 1));
        keywords.add(new Keyword("mcdonald", 1));
        keywords.add(new Keyword("mc donald", 1));
        keywords.add(new Keyword("burgerking", 1));
        keywords.add(new Keyword("burger king", 1));
        keywords.add(new Keyword("stomach pain", 2));
        keywords.add(new Keyword("diarrhea", 5));
        keywords.add(new Keyword("the shits", 2));
        keywords.add(new Keyword("dehydration", 5));
        keywords.add(new Keyword("salmonella", 5));
        keywords.add(new Keyword("nausea", 4));
        keywords.add(new Keyword("vomit", 5));
        keywords.add(new Keyword("cramps", 2));
        keywords.add(new Keyword("pain", 1));
        keywords.add(new Keyword("fever", 1));
        keywords.add(new Keyword("ill", 1));
        keywords.add(new Keyword("sick", 1));
        keywords.add(new Keyword("infection", 1));
        keywords.add(new Keyword("hygiene", 2));
        keywords.add(new Keyword("disease", 2));
        keywords.add(new Keyword("headache", 1));
        keywords.add(new Keyword("confusion", 1));
        keywords.add(new Keyword("stomach flu", 5));
        
        int score = 0;
        
        for (Keyword keyword : keywords) {
            if (tweetText.contains(keyword.getName()))
                score += keyword.getWeight();
        }
        
        tweet.setScore(score);
    }
	
	public static void filterTweets(Tweet tweet)
	{
		System.out.println(tweet.getTweetText().toLowerCase());
		//Pattern p = Pattern.compile("\\w*mads\\w*");
		//Matcher m = p.matcher(tweet.getTweetText().toLowerCase());
		if(Pattern.matches("[a-z]*", tweet.getTweetText()))
		{
			System.out.println("madsen mor");
		}
		else
		{
			System.out.print("ikke nogen madser mor :((");
		}
	}
}
