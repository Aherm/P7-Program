package streaming;
import twitter4j.*;
public class OurStatusListener implements StatusListener{

	public void onStatus(Status status ){
		
		
		
	}
	
	 public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
     public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
     public void onException(Exception ex) {
         ex.printStackTrace();
     }
     public void onScrubGeo(long x, long y) {}
     public void onStallWarning(StallWarning warning) {}
     
}
