package modelLayer;

import twitter4j.Status;
import utility.Distance;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Date;

public class Tweet implements OurLocation {
	private long tweetID, userID, responseID, retweetID;
	private String tweetText;
	private Date createdAt;
	private double lat, lon;
	private List<Keyword> matchedKeywords = new ArrayList<Keyword>();
	private boolean sick = false; 
	private double score = -1;
	private Cluster assignedCluster = null;
	private String expectedClassLabel = ""; 
	private String assignedClassLabel = ""; //Result of classification
	private Restaurant nameRestaurant = null; 
	private Restaurant locRestaurant = null; 
	
	private BigDecimal probabilityTrue;
	//private boolean addedToStorage = false;

	public Tweet() {}

	public Tweet(long tweetID, long userID, long responseID, long retweetID, String tweetText, Date createdAt) {
		this(tweetID, userID, responseID, retweetID, tweetText, createdAt, 0.0, 0.0);
	}

	public Tweet(long tweetID, long userID, long responseID, long retweetID, String tweetText, Date createdAt, double lat, double lon) {
		this.tweetID = tweetID;
		this.userID = userID;
		this.responseID = responseID;
		this.retweetID = retweetID;
		this.tweetText = tweetText;
		this.createdAt = createdAt;
		this.lat = lat;
		this.lon = lon;
	}

	//btc 28112015, constructor to set expectedClassLabel for training data
	public Tweet(long tweetID, long userID, long responseID, long retweetID, String tweetText, Date createdAt, double lat, double lon, String expectedClassLabel) {
		this(tweetID, userID, responseID, retweetID, tweetText, createdAt, lat, lon);
		this.setExpectedClassLabel(expectedClassLabel);
	}

	public static Tweet createTweet(Status status) {
		if (status.getGeoLocation() == null) {
			return new Tweet(status.getId(), status.getUser().getId(), status.getInReplyToUserId(), status.getCurrentUserRetweetId(),
					status.getText(), status.getCreatedAt());
		}
		else return new Tweet(status.getId(), status.getUser().getId(), status.getInReplyToUserId(), status.getCurrentUserRetweetId(),
				status.getText(), status.getCreatedAt(), status.getGeoLocation().getLatitude(), status.getGeoLocation().getLongitude());
	}
	
	public static Tweet createSickTweet(Status status){
		Tweet t = createTweet(status);
		t.setSick(true);
		return t; 
	}

	public long getTweetID() {
		return tweetID;
	}

	public void setTweetID(long tweetID) {
		this.tweetID = tweetID;
	}

	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	public long getResponseID() {
		return responseID;
	}

	public void setResponseID(long responseID) {
		this.responseID = responseID;
	}

	public long getRetweetID() {
		return retweetID;
	}

	public void setRetweetID(long retweetID) {
		this.retweetID = retweetID;
	}

	public String getTweetText() {
		return tweetText;
	}

	public void setTweetText(String tweetText) {
		this.tweetText = tweetText;
	}

	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public void addKeyword(Keyword keyword) {
		matchedKeywords.add(keyword);
	}

	public double getScore() {
		score = 0;
		for(Keyword keyword : matchedKeywords) {
			this.score += keyword.getWeight();
		}

		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Cluster getCluster() {
		return this.assignedCluster;
	}

	public void setCluster (Cluster c) {
		this.assignedCluster = c;
	}

	public String getExpectedClassLabel() {
		return expectedClassLabel;
	}

	public void setExpectedClassLabel(String expectedClassLabel) {
		this.expectedClassLabel = expectedClassLabel;
	}

	public String getAssignedClassLabel() {
		return assignedClassLabel;
	}

	public void setAssignedClassLabel(String assignedClassLabel) {
		this.assignedClassLabel = assignedClassLabel;
	}

	public BigDecimal getProbabilityTrue() {
		return probabilityTrue;
	}

	public void setProbabilityTrue(BigDecimal probabilityTrue) {
		this.probabilityTrue = probabilityTrue;
	}

	public void setSick(boolean s){
		this.sick = s; 
	}
	
	public boolean isSick(){
		return this.sick; 
	}

	/* TODO: Remove if the new filter methods work
	public boolean isAddedToStorage() {
		return addedToStorage;
	}

	public void setAddedToStorage(boolean addedToStorage) {
		this.addedToStorage = addedToStorage;
	}*/

	public boolean isGeotagged() {
		if(lat == 0 && lon == 0)
			return false; 
		else 
			return true; 
	}

	public Tweet clone() {
		return new Tweet(tweetID, userID, responseID, retweetID, tweetText, createdAt, lat, lon);
	}
	
	@Override
	public boolean equals(Object o){
		if(o == this) 
			return true; 
		
		if(!(o instanceof Tweet))
			return false; 
		
		Tweet t = (Tweet)o;
		
		return new EqualsBuilder().append(tweetID, t.tweetID).isEquals();  
		
	}
	
	@Override
	public int hashCode(){
		return new HashCodeBuilder(41,83).append(tweetID).toHashCode();
	}
	
	//Stuff for scoring pls ignore )=
	
	public void setNameRes(Restaurant r){
		this.nameRestaurant = r; 
	}
	
	public void setLocRes(Restaurant r){
		if(locRestaurant == null)
			this.locRestaurant = r; 
		else{
			double dist1 = Distance.getDist(this.locRestaurant, this); 
			double dist2 = Distance.getDist(r, this); 
			if(dist2 < dist1)
				this.locRestaurant = r; 
			}
	}
	
	
	public Restaurant getLocRes(){
		return this.locRestaurant; 
	}
	
	public Restaurant getNameRes(){
		return this.getNameRes(); 
	}
	
	public boolean conflict(){
		return !locRestaurant.equals(nameRestaurant); 
	}
	
	//used in scoring
	public boolean hasVisited(){
		return this.locRestaurant != null || this.nameRestaurant != null; 
	}
	
	public boolean nameResWithin(){
		return (Distance.getDist(this,nameRestaurant) < 25); 
	}
}