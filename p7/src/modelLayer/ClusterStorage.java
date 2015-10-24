package modelLayer;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ClusterStorage implements Iterable<Cluster> {
	private List<Cluster> clusters = new ArrayList<Cluster>();

	public ClusterStorage() {}

	public Cluster get(int n) {
		return clusters.get(n);
	}

	public void add(Cluster c) {
		clusters.add(c);
	}

	public void remove(Cluster c) {
		for (Tweet t : c.getTweets()) {
			t.setCluster(null);
		}
		clusters.remove(c);
	}
	
	public void removeAll(ClusterStorage cs) {
		Iterator<Cluster> it = cs.iterator();
		while (it.hasNext()) {
			Cluster c = it.next();
			for (Tweet t : c.getTweets()) {
				t.setCluster(null);
			}
			it.remove();
		}
	}

	public int size() {
		return clusters.size();
	}

	public Iterator<Cluster> iterator() {
		return clusters.iterator();
	}
}