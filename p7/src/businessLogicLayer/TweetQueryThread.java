package businessLogicLayer;

import fileCreation.GpxCreator;
import fileCreation.StatisticsWriter;
import modelLayer.ClusterStorage;
import modelLayer.InvertedIndex;
import modelLayer.TweetStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class TweetQueryThread extends Thread {
	private TweetStorage tweets;
	private ClusterStorage clusters;
	private List<String> restaurants;
	private InvertedIndex invertedIndex;
	private int facilityCost = 5000;

	public TweetQueryThread(TweetStorage tweets, ClusterStorage clusters) {
		this.tweets = tweets;
		this.clusters = clusters;
	}
	
	public TweetQueryThread(TweetStorage tweets, ClusterStorage clusters, List<String> restaurants, InvertedIndex invertedIndex) {
		this.tweets = tweets;
		this.clusters = clusters;
		this.restaurants = restaurants;
		this.invertedIndex = invertedIndex;
	}

	public void run() {
		System.out.println("Press 1 to get size of tweets, 2 to cluster tweets, 3 to get cluster size, 4 to create cluster gpx files, 5 to get data statistics, 6 to write location tweets in storage to gpx");
		Scanner sc = new Scanner(System.in);
		boolean running = true;

		while (running) {
			int i = sc.nextInt();
			switch (i) {
			case 1:
				System.out.println("Tweet size is " + tweets.size());
				break;
			case 2:
				if (clusters.size() == 0) {
					clusters = Clustering.clusterTweets(tweets, facilityCost);
				}
				else {
					Clustering.updateClusters(clusters, tweets.clone(), facilityCost);
				}
				break;
			case 3:
				System.out.println("Cluster size: " + clusters.size());
				break;
			case 4:
				GpxCreator.createClusterGpsFiles(clusters);
				break;
			case 5:
				DataAnalysis analysis = new DataAnalysis(tweets, clusters);
				String output = "";
				//output += analysis.printKeywordAnalysis();
				output += analysis.printStatistics();
				try {
					StatisticsWriter.writeFile(output);
				}
				catch (Exception ex) {
					System.out.print(ex);
				}
				System.out.println("DataAnalysis done, check the statistics folder");
				break;
			case 6:
				 Date date = new Date();
			     String dateString = new SimpleDateFormat("yyyy-MM-dd HH-mm").format(date);
				 GpxCreator.createGpxFile(tweets, dateString + "_all" , "./gpxFiles/all");
				 break;
			case 7:
				List<String> mentionedRestaurants = new ArrayList<String>();
				for(String restaurant : restaurants)
				{
					if(Filter.matchRestaurantByName(restaurant, invertedIndex))
						mentionedRestaurants.add(restaurant);
				}
				
				System.out.println("Number of restaurants mentioned: " + mentionedRestaurants.size());
				break;
			default:
				running = false;
			}
		}

		sc.close();
	}
}
