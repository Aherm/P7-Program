package businessLogicLayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

import org.joda.time.DateTime;
import org.joda.time.Days;

import streaming.Oauth;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterRest {
	private int totalcalls = 0; // holds nr of times we use the twitter api
	private long startTime;
	private Twitter twitter;
	public boolean limitReached = false; 

	public TwitterRest() {
		ConfigurationBuilder cb = Oauth.createConfigBuilder();
		TwitterFactory factory = new TwitterFactory(cb.build());
		twitter = factory.getInstance();
	}

	//currently assumes that the list is sorted from newest to oldest 
	// TODO: Make sure that the assumption is actually true
	// TODO: Why do we need to send a tweet as parameter?
	public TweetStorage getUserTimeline3days(long userId, Date _startdate, Tweet tweet) throws TwitterException {	
		TweetStorage tweets = new TweetStorage();		
		int pagenr = 1;
		
		Paging page = new Paging(pagenr, 500);
		List<Status> userTimeline = new ArrayList<Status>();
		DateTime oldestTweetDate = new DateTime();
		DateTime startDate = new DateTime(_startdate);
		do {
			rateLimiter();
			if(limitReached)
				break; 
			userTimeline = twitter.getUserTimeline(userId, page);
			
			// adds all tweets that are no more than 3 days old 
			for (int i = 0; i < userTimeline.size(); i++) {
				DateTime tweetDate = new DateTime(userTimeline.get(i).getCreatedAt());
				if (Days.daysBetween(tweetDate,startDate).getDays() <= 3) {
					if(tweet.getTweetID() != userTimeline.get(i).getId()){
						tweets.add(Tweet.createTweet(userTimeline.get(i)));	
					}								
				}
				else break;
			}
			pagenr++;
			page.setPage(pagenr);
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
		//MADS: maybe thrown an exception 
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
