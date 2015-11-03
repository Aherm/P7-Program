package businessLogicLayer;

import fileCreation.GpxCreator;
import fileCreation.StatisticsWriter;
import modelLayer.ClusterStorage;
import modelLayer.TweetStorage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class TweetQueryThread extends Thread {
	private TweetStorage tweets;
	private ClusterStorage clusters;
	private int facilityCost = 5000;

	public TweetQueryThread(TweetStorage tweets, ClusterStorage clusters) {
		this.tweets = tweets;
		this.clusters = clusters;
	}

	public void run() {
		System.out.println("Press 1 to get size of tweets, 2 to cluster tweets, 3 to get cluster size, 4 to create cluster gpx files and 5 to get data statistics");
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
					Clustering.updateClusters(clusters, tweets, facilityCost);
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
				 GpxCreator.createGpxFile(tweets, dateString + "_all" , "./gpxFiles");
				 break;
			default:
				running = false;
			}
		}

		sc.close();
	}
}
