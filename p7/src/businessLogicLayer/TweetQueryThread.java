package businessLogicLayer;

import java.util.List;
import java.util.Scanner;

import modelLayer.Cluster;
import modelLayer.TweetStorage;

public class TweetQueryThread extends Thread {
	
	private TweetStorage tweets;
	private List<Cluster> clusters;
	
	public TweetQueryThread (TweetStorage tweets, List<Cluster> clusters) {
		this.tweets = tweets;
		this.clusters = clusters;
	}
	
	private int getSize() {
		return tweets.size();
	}
	
	public void run() {
		System.out.println("Ask for size");
		Scanner sc = new Scanner(System.in);
		
		while (true) {		
			int i = sc.nextInt();
			
			if (i == 1) {
				System.out.println("Size is " + getSize());
			}
			else break;
		}
		
		sc.close();
		
	}
}
