package modelLayer;

import java.util.ArrayList;

public class ClusterStorage extends ArrayList<Cluster> {
	// This field has to be there. We don't use it.
	private static final long serialVersionUID = -7427521225340998074L;

	public void remove(Cluster c) {
		for (Tweet t : c.getTweets()) {
			t.setCluster(null);
		}
		super.remove(c);
	}
	
	public void removeAll(ClusterStorage cs) {
		for (Cluster c : cs) {
			for (Tweet t : c.getTweets()) {
				t.setCluster(null);
			}
			super.remove(c);
		}
	}
}