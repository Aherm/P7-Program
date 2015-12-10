package streaming;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

import org.joda.time.DateTime;
import org.joda.time.Days;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public final class TwitterRest {
	private static TwitterRest instance = null; 
	private int totalcalls = 0; // holds nr of times we use the twitter api
	private long startTime;
	private Twitter twitter;
	public boolean limitReached = false; 

	private TwitterRest() {
		ConfigurationBuilder cb = Oauth.createConfigBuilder();
		TwitterFactory factory = new TwitterFactory(cb.build());
		twitter = factory.getInstance();
	}
	
	public static TwitterRest getInstance(){
		if(instance == null){
			instance = new TwitterRest(); 
		}
		
		return instance; 
	}

	//currently assumes that the list is sorted from newest to oldest 
	public TweetStorage getUserTimeline3days(long userId, Date _startdate, Tweet tweet) throws TwitterException {	
		TweetStorage tweets = new TweetStorage();		
		int pagenr = 1;
		
		Paging page = new Paging(pagenr, 500);
		List<Status> userTimeline = new ArrayList<Status>();
		DateTime oldestTweetDate = new DateTime();
		DateTime startDate = new DateTime(_startdate);
		do {
			rateLimiter();
			if(limitReached){
				return tweets;
			}
			userTimeline = twitter.getUserTimeline(userId, page);
			
			// adds all tweets that are no more than 3 days old 
			for (int i = 0; i < userTimeline.size(); i++) {
				DateTime tweetDate = new DateTime(userTimeline.get(i).getCreatedAt());
				if (Days.daysBetween(tweetDate,startDate).getDays() <= 3) {
					if(tweet.getTweetID() != userTimeline.get(i).getId() && userTimeline.get(i).getGeoLocation() != null){
						tweets.add(Tweet.createSickTweet(userTimeline.get(i)));	
					}								
				}
				else break;
			}
			pagenr++;
			page.setPage(pagenr);
			if(userTimeline.isEmpty()){
				return tweets;
			}
			oldestTweetDate = new DateTime(userTimeline.get(userTimeline.size() - 1).getCreatedAt()); //get the oldest tweet from the usertimeline
		} while (Days.daysBetween(oldestTweetDate, startDate).getDays() <= 3);
		
		return tweets; 
	}
	
	private void rateLimiter() {		
		if(totalcalls == 0) {
			startTime = System.nanoTime();
		}
		
		if((System.nanoTime() - startTime) > 900000000000L){// 15 minutes
			totalcalls = 0; 
			startTime = System.nanoTime();
			limitReached = false; 
		}
		//TODO: MADS: maybe thrown an exception 
		if(totalcalls == 175){
			System.out.println("REST API limit reached: need to wait");
			limitReached = true; 
		}
		else
			totalcalls++;		
	}

	public void printUserName(long id) throws TwitterException {		
		rateLimiter();
		System.out.println(twitter.getUserTimeline(id).get(0).getUser().getScreenName());
	}
}
