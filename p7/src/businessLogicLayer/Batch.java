package businessLogicLayer;

import dataAccessLayer.DBGetTweets;
import modelLayer.TweetStorage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Batch {

	public static TweetStorage filterTweets() {
		TweetStorage ts = new TweetStorage();
		TweetStorage interval = new TweetStorage();
		int size = 10000;
		int start = 0;
		int itnr = 1;
		do {
			System.out.println("iteration: " + itnr);
			interval = DBGetTweets.getInterval(start, size);
			ts.addAll(Filter.getFilteredTweets(interval, new Date()));
			System.out.println(ts.size());
			start = start + size; 
			itnr++;
		} while(interval.size() == size); 

		return ts;
	}

	public static Map<String, Integer> getTweets() {
		Map<String, Integer> matchedKeywords = new HashMap<String, Integer>();
		TweetStorage interval = new TweetStorage();
		int size = 10000;
		int start = 0;
		int itnr = 1;
		do {
			System.out.println("iteration: " + itnr);
			interval = DBGetTweets.getInterval(start, size);
			Map<String, Integer> tempMap = Filter.countMatches(interval);
			for (Map.Entry<String, Integer> entry : tempMap.entrySet()) {

				String key = (String) entry.getKey();
				int value = (Integer) entry.getValue();

				if (matchedKeywords.get(key) == null ){
					matchedKeywords.put(key, value);
				}
				else {
					matchedKeywords.put(key, matchedKeywords.get(key) + value);
				}
				
				System.out.println("------------------------------------------------------------------------------------");
				System.out.println("Local matched");
				System.out.println(matchedKeywords);
				System.out.println("------------------------------------------------------------------------------------");
			}

			System.out.println(interval.size());
			System.out.println("start value " + start);
			start = start + size;
			itnr++;
		} while(interval.size() == size);

		return matchedKeywords;
	}
}
