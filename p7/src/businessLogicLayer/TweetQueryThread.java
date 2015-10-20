package businessLogicLayer;

import java.util.List;
import java.util.Scanner;

import modelLayer.Cluster;
import modelLayer.TweetStorage;
import visual.GpxCreator;

public class TweetQueryThread extends Thread {
	
	private TweetStorage tweets;
	private List<Cluster> clusters;
	private int facilityCost = 5000;
	
	public TweetQueryThread (TweetStorage tweets, List<Cluster> clusters) {
		this.tweets = tweets;
		this.clusters = clusters;
	}
	
	private int getSize() {
		return tweets.size();
	}
	
	public void run() {
		System.out.println("Press 1 to get size of tweets, 2 to cluster tweets, 3 to get cluster size, and 4 to create cluster gpx files");
		Scanner sc = new Scanner(System.in);
		boolean running = true;
		
		while (running) {		
			int i = sc.nextInt();
			
			switch (i) {
				case 1:
					System.out.println("Tweet size is " + getSize());
					break;
				case 2:
					if (clusters.size() == 0) {
						clusters = Clustering.tweetClustering(tweets, facilityCost);
					}
					else {
						Clustering.updateClusters(clusters, Cluster.getUnclusteredTweets(tweets), tweets, facilityCost);
					}
					break;
				case 3:
					System.out.println("Cluster size: " + clusters.size());
					break;
				case 4:
					GpxCreator.createClusterGpsFiles(clusters);
					break;
				default:
					running = false;
			}
		}
		
		sc.close();
	}
}
