package modelLayer;

import java.util.LinkedList;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

public class TweetStorage {
	private LinkedList<Tweet> tweets = new LinkedList<Tweet>();
	
	public TweetStorage() {}
	
	public void add(Tweet t) {
		tweets.addLast(t);
	}
	
	public void remove(Tweet t) {
		tweets.remove(t);
	}
	
	public void removeOld(int days) {
		Date past = tweets.getFirst().getCreatedAt();
		Date today = new Date();
		System.out.println(Days.daysBetween(new LocalDate(past), new LocalDate(today)).getDays());
		while(Math.abs(Days.daysBetween(new DateTime(past), new DateTime(today)).getDays()) >= days) {
			remove(tweets.getFirst());
			past = tweets.getFirst().getCreatedAt();
		}
	}
	
	public int size() {
		return tweets.size();
	}
	
	public Tweet getFirst() {
		return tweets.getFirst();
	}
	
	public Tweet getLast() {
		return tweets.getLast();
	}
	
	public Tweet get(int n) {
		return tweets.get(n);
	}
}
