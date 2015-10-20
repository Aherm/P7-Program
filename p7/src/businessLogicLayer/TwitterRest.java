package businessLogicLayer;

import java.io.Console;
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

//Created by Mads on 07-10-2015

public class TwitterRest {

	private int totalcalls = 0; // holds nr of times we use the twitter api
	private long startTime = System.nanoTime();
	private long endTime;
	private Twitter twitter;
	private int stuff = 1; 
	public boolean limitReached = false; 

	public TwitterRest() {

		ConfigurationBuilder cb = new Oauth().createConfigBuilder();
		TwitterFactory factory = new TwitterFactory(cb.build());
		twitter = factory.getInstance();
	}

	//currently assumes that the list is sorted from newest to oldest 
	public TweetStorage getUserTimeline3days(long userId, Date startDate) throws TwitterException{
	
		TweetStorage tweets = new TweetStorage();		
		

			int pagenr = 1;

			Paging page = new Paging(pagenr, 500);
			List<Status> userTimeline = new ArrayList<Status>();
			do {
				rateLimiter();
				if(limitReached)
					break; 
				System.out.println("iteration: " + stuff);
				stuff++;
				userTimeline = twitter.getUserTimeline(userId, page);
				Date lastDate = userTimeline.get(userTimeline.size() - 1).getCreatedAt();
				// adds all tweets that are no more than 3 days old 
				for (int i = 0; i < userTimeline.size(); i++) {
					Date today = userTimeline.get(i).getCreatedAt();
					if (Days.daysBetween(new DateTime(today),new DateTime(startDate)).getDays() <= 3) {
						tweets.add(Tweet.createTweet(userTimeline.get(i)));
					}
					else break;
				}
				System.out.println();
				pagenr++;
				page.setPage(pagenr);
			} while (Days.daysBetween(new DateTime(userTimeline.get(userTimeline.size() -1).getCreatedAt()), new DateTime(startDate)).getDays() <= 3);

	

		return tweets; 
	}
	
	private void rateLimiter(){
		
		if(System.nanoTime() - startTime > 900000000000L){// 15 minutes
			totalcalls = 0; 
			startTime = System.nanoTime();
			limitReached = false; 
		}
		 
		if(totalcalls == 170){
			limitReached = true; 
		}
		else
			totalcalls++;
		
		
		
	}

	public void printUserName(long id){
		try {
			System.out.println(twitter.getUserTimeline(id).get(0).getUser().getScreenName());
			rateLimiter();
		}
		catch(TwitterException e){
			e.printStackTrace();
		}
	}
}
