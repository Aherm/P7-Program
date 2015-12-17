package businessLogicLayer;

import modelLayer.Cluster;
import modelLayer.ClusterStorage;
import modelLayer.TweetStorage;
import java.util.Map;

public class DataAnalysis {
	private TweetStorage tweets;
	private ClusterStorage clusters;

	public DataAnalysis(TweetStorage storage) {
		this.tweets = storage;
	}

	public DataAnalysis(TweetStorage storage, ClusterStorage clusters) {
		this.tweets = storage;
		this.clusters = clusters;
	}

	public long nrGeotagged() {
		return tweets.getGeotaggedTweets().size();
	}

	private double calcAvgClusterSize(ClusterStorage clusters) {
		int totalSize = 0;
		int numClusters = clusters.size();

		for (Cluster cluster : clusters){
			totalSize += cluster.size();
		}

		//make sure double return works
		return (double) totalSize / numClusters;
	}

	private int calcMinClusterSize(ClusterStorage clusters) {
		int minSize = Integer.MAX_VALUE;

		for (Cluster cluster : clusters) {
			if (minSize > cluster.size()) {
				minSize = cluster.size();
			}
		}

		return minSize;
	}

	private int calcMaxClusterSize(ClusterStorage clusters) {
		int maxSize = Integer.MIN_VALUE;

		for (Cluster cluster : clusters) {
			if (maxSize < cluster.size()) {
				maxSize = cluster.size();
			}
		}

		return maxSize;
	}

	public String printStatistics() {
		String tweetsAnalysis = "Total tweets: " + tweets.size() + "\r\n" +
								"Geotagged Tweets: " + nrGeotagged() + "\r\n" +
								"Percent Geotagged Tweets: " +
								(((double) nrGeotagged() / (double) tweets.size()) * 100) + " %" + "\r\n";

		String clusterAnalysis = "";
		if (clusters != null && clusters.size() > 0) {
			clusterAnalysis = "Average cluster size: " + calcAvgClusterSize(clusters) + "\r\n" +
							  "Min cluster size: " + calcMinClusterSize(clusters) + "\r\n" +
							  "Max cluster size: " + calcMaxClusterSize(clusters) + "\r\n";
		}

		return tweetsAnalysis + clusterAnalysis;
	}
/*
	public String printKeywordAnalysis() {
		String result = "";
		Map<String, Integer> keywordMatches = Filter.countMatches(tweets);
		for (Map.Entry<String, Integer> entry : keywordMatches.entrySet()){
			result += "Keyword: " + entry.getKey() + " Count: " + entry.getValue() + "\r\n";
		}
		return result;
	}
	*/

	public void clusterAnalysis(ClusterStorage clusters, int numbers){
		int[] sizes = new int[numbers];

		for(int i = 0; i < sizes.length; i++) {
			sizes[i] = 0;
		}

		for(Cluster c : clusters) {
			if(c.getTweets().size() > sizes.length - 1) {
				sizes[sizes.length-1]++;
			}
			else
				sizes[c.getTweets().size()]++;
		}

		System.out.println("Total amount of clusters: " + clusters.size());
		for(int i = 0; i < sizes.length; i++) {
			if(sizes[i] > 0 )
				System.out.println("nr " + i + " Size: " + sizes[i]);
		}
	}
}
