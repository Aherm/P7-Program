package modelLayer;

import java.util.LinkedList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class TweetStorage implements Iterable<Tweet> {
	private LinkedList<Tweet> tweets = new LinkedList<Tweet>();
	
	public TweetStorage() {}
	
	public void add(Tweet t) {
		tweets.addLast(t);
	}
	
	public void add(TweetStorage ts) {
		tweets.addAll(ts.tweets);
	}
	
	public void remove(Tweet t) {
		tweets.remove(t);
	}
	
	public void removeOldTweets(int days) {
		Date past = tweets.getFirst().getCreatedAt();
		Date today = new Date();
		
		while(Days.daysBetween(new DateTime(past), new DateTime(today)).getDays() >= days) {
			remove(tweets.getFirst());
			past = tweets.getFirst().getCreatedAt();
		}
	}
	
	public TweetStorage clone(){
		
		TweetStorage clone = new TweetStorage();
		for(Tweet t : this){
			clone.add(t);
		}
		return clone;
	}
	
	public TweetStorage getRandomizedCopy(){
		TweetStorage copy = this.clone();
		copy.randomize();
		
		return copy;
	}
	
	public TweetStorage getReverseCopy(){
		TweetStorage copy = this.clone();
		copy.reverse();
		
		return copy;
	}
	
	private void reverse(){
		Collections.reverse(tweets);
	}
	
	
	//should only be used in getRandomizedCopy
	private void randomize(){
		Collections.shuffle(tweets);
	}
	
	
	public Iterator<Tweet> iterator(){
		return tweets.iterator();
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

	public void displayTweets(){
		for (Tweet tweet : tweets){
			System.out.println("time: " + tweet.getCreatedAt());
		}
	}
	
	public TweetStorage getDifference(TweetStorage ts) {
		TweetStorage res = new TweetStorage();
		res = this.clone();
		res.tweets.removeAll(ts.tweets);
		
		return res;
	}
}
