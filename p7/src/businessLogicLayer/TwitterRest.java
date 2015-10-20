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

//Created by Mads on 07-10-2015

public class TwitterRest {

	private int totalcalls = 0; // holds nr of times we use the twitter api
	private long startTime;
	private long endTime;
	private Twitter twitter;

	public TwitterRest() {

		ConfigurationBuilder cb = new Oauth().createConfigBuilder();
		TwitterFactory factory = new TwitterFactory(cb.build());
		twitter = factory.getInstance();
	}

	//currently assumes that the list is sorted from newest to oldest 
	public TweetStorage getUserTimeline3days(long userId, Date startDate) {
	
		TweetStorage tweets = new TweetStorage();		
		
		try {
			int pagenr = 1;

			Paging page = new Paging(pagenr, 500);
			List<Status> userTimeline = new ArrayList<Status>();
			do {	
				rateLimiter();
				if(totalcalls == 280) // makes sure we do not reach our limit 
					break;
				
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

				pagenr++;
				page.setPage(pagenr);
			} while (Days.daysBetween(new DateTime(userTimeline.get(userTimeline.size() -1).getCreatedAt()), new DateTime(startDate)).getDays() <= 3);

		}
		catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tweets; 
	}
	
	private void rateLimiter(){
		
		if(totalcalls == 0){
			startTime = System.nanoTime();
			totalcalls++;
		}

		if(System.nanoTime() - startTime > 900000000000L){// 15 minutes
			totalcalls = 1; 
			startTime = System.nanoTime();
		}
	}

	public void printUserName(long id){
		try {
			System.out.println(twitter.getUserTimeline(id).get(0).getUser().getScreenName());
		}
		catch(TwitterException e){
			e.printStackTrace();
		}
	}
}
