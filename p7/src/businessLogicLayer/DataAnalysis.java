package businessLogicLayer;

import modelLayer.Cluster;
import modelLayer.ClusterStorage;
import modelLayer.Tweet;
import modelLayer.TweetStorage;
import java.util.Map;

public class DataAnalysis {
	private TweetStorage storage;
	private ClusterStorage clusters;

	public DataAnalysis(TweetStorage storage){
		this.storage = storage;
	}

	public DataAnalysis(TweetStorage storage, ClusterStorage clusters) {
		this.storage = storage;
		this.clusters = clusters;
	}

	public long nrGeotagged(){
		long amount = 0; 
		
		for(Tweet t : storage){
			if(t.isGeotagged())
				amount++; 
		}
		
		return amount; 
	}
	
	public TweetStorage geotagged(){
		TweetStorage ts = new TweetStorage(); 
		for(Tweet t : storage){
			if(t.isGeotagged()){
				ts.add(t);
			}
		}
		
		return ts; 
	}

	private double calcAvgClusterSize(ClusterStorage clusters){
		int totalSize = 0;
		int numClusters = clusters.size();

		for (Cluster cluster : clusters){
			totalSize += cluster.size();
		}

		//make sure double return works
		return totalSize / numClusters;
	}


	private int calcMinClusterSize(ClusterStorage clusters){
		int minSize = 0;

		for (Cluster cluster : clusters){
			if (minSize == 0)
				minSize = cluster.size();
			else if (minSize > cluster.size())
				minSize = cluster.size();
		}
		return minSize;
	}

	private int calcMaxClusterSize(ClusterStorage clusters){
		int maxSize = 0;

		for (Cluster cluster : clusters){
			if (maxSize == 0)
				maxSize = cluster.size();
			else if (maxSize < cluster.size())
				maxSize = cluster.size();
		}
		return maxSize;
	}

	public String printStatistics(){
		String tweetsAnalysis = "Total tweets: " + storage.size() + "\r\n" +
				"Geotagged Tweets: " + nrGeotagged() + "\r\n" +
				"Procent Geotagged Tweets: " + (
				((double) nrGeotagged() / (double) storage.size()) * 100) + " %" + "\r\n";

		String clusterAnalysis = null;
		if (clusters != null){
			if(clusters.size() > 0){
				clusterAnalysis = "Average cluster size: " + calcAvgClusterSize(clusters) + "\r\n" +
						"Min cluster size: " + calcMinClusterSize(clusters) + "\r\n" +
						"Max cluster size: " + calcMaxClusterSize(clusters) + "\r\n";
			}
		}

		if (clusterAnalysis != null)
			return tweetsAnalysis + clusterAnalysis;
		else
			return tweetsAnalysis;
	}

	public String printKeywordAnalysis(){
		String result = "";
		Map<String, Integer> keywordMatches = Filter.countMatches(storage);
		for (Map.Entry<String, Integer> entry : keywordMatches.entrySet()){
			result += "Keyword: " + entry.getKey() + " Count: " + entry.getValue() + "\r\n";
		}
		return result;
	}



	public void clusterAnalysis(ClusterStorage clusters, int numbers){
		int[] sizes = new int[numbers];
		
		for(int i = 0; i < sizes.length; i++){
			sizes[i] = 0;
		}
		
		for(Cluster c : clusters){
			if(c.getTweets().size() > sizes.length -1){
				sizes[sizes.length-1]++;
			}
			else
				sizes[c.getTweets().size()]++;
		}
		
		System.out.println("Total amount of clusters: " + clusters.size());
		for(int i = 0; i < sizes.length; i++){
			if(sizes[i] > 0 )
				System.out.println("nr " + i + " Size: " + sizes[i]);
		}
		
	}
	
}
