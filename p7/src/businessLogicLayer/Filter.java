package businessLogicLayer;

import java.util.ArrayList;
import java.util.List;

public class Filter {

    public static List<String> containsKeywords(String tweetText) {
        tweetText = tweetText.toLowerCase();
        List<String> matchedKeys = new ArrayList<String>();
        List<String> keywords = new ArrayList<String>();
        keywords.add("food");
        keywords.add("poison");
        keywords.add("restaurant");
        keywords.add("sick");
        keywords.add("soup");
        keywords.add("drink");
        keywords.add("bed");
        keywords.add("hungry");
        keywords.add("soda");
        keywords.add("chinese food");
        keywords.add("chipotle");
        keywords.add("mcdonald");
        keywords.add("mc donald");
        keywords.add("burgerking");
        keywords.add("burger king");
        keywords.add("stomach pain");
        keywords.add("diarrhea");
        keywords.add("the shits");

        for (String keyword : keywords) {
            if (tweetText.contains(keyword))
                matchedKeys.add(keyword);
        }
        return matchedKeys;
    }
}
