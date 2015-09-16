package main;

import streaming.OurStatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		OurStatusListener listener = new OurStatusListener(); 
		
		TwitterStream stream = new TwitterStreamFactory().getInstance(); 
		stream.addListener(listener);
		
		stream.sample();
		
	}

}
