package businessLogicLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fileCreation.GenericPrint;
import modelLayer.Grid;
import modelLayer.InvertedIndex;
import modelLayer.Restaurant;
import modelLayer.Tweet;
import modelLayer.TweetStorage;
import utility.Constants;
import utility.Distance;

public class Scoring {
	public  Map<Restaurant,Double> geoScore = new HashMap<Restaurant,Double>();
	public  Map<Restaurant,Double> nameScore = new HashMap<Restaurant,Double>();
	public  Map<Restaurant,Double> combinedScore = new HashMap<Restaurant,Double>();
	public  Map<Restaurant,Double> conservative = new HashMap<Restaurant,Double>();
	public  Map<Restaurant,Double> noMcombinedScore = new HashMap<Restaurant,Double>();
	private  Map<String,Integer> RestaurantNameCounter = new HashMap<String,Integer>(); 
	private  Map<String,List<Restaurant>> restaurantsWithSameName = new HashMap<String,List<Restaurant>>();
	private  int   locTotalVisits = 0; 
	private  int   locTotalSickVisits = 0; 
	private  int   nameTotalVisits = 0; 
	private  int   nameTotalSickVisits = 0; 
	private int uniqueLoc = 0; 
	private int uniqueMen = 0; 
	private int equal = 0; 
	private int diff = 0;
	private int conflictLoc = 0; 
	public int conflictName = 0; 
	private int geotaggedMentions = 0; 
	private StringBuilder conLoc = new StringBuilder(); 
	private StringBuilder conName = new StringBuilder();
	private int locTotalTweets = 0;
	private int locTotalSickTweets = 0;
	private int nameTotalTweets = 0;
	private int nameTotalSickTweets = 0;
	
 
	private void init(List<Restaurant> restaurants){
		
		for(Restaurant r : restaurants){
			if(RestaurantNameCounter.containsKey(r.getName())){
				  Integer value = RestaurantNameCounter.get(r.getName()); 
				  value = new Integer(value.intValue() + new Integer(1).intValue()); 
				  RestaurantNameCounter.put(r.getName(), value); 
			}
			else{
				RestaurantNameCounter.put(r.getName(), new Integer(1)); 
			}
		}
		
		for(Restaurant r : restaurants){
			if(restaurantsWithSameName.containsKey(r.getName())){
				restaurantsWithSameName.get(r.getName()).add(r);
			}
			else{
				List<Restaurant> res = new ArrayList<Restaurant>(); 
				res.add(r); 
				restaurantsWithSameName.put(r.getName(), res);
			}
		}
		initScores(restaurants, geoScore);
		initScores(restaurants, nameScore);
		initScores(restaurants, combinedScore);
		initScores(restaurants, noMcombinedScore);
		initScores(restaurants, conservative); 
		
	}
	
	private void initScores(List<Restaurant> restaurants, Map<Restaurant, Double> map){
		for(Restaurant r : restaurants){
			map.put(r, new Double(0)); 
		}
	}
	
	
	private void setLocRes(Restaurant r, Grid grid) {
		TweetStorage tweets = grid.rangeQuery(r, 25);
		
		for(Tweet t: tweets){
			t.setLocRes(r);
		}
	}
	
	private void calcGeotagged(TweetStorage ts, List<Restaurant> Allres){
		Map<Restaurant,TweetStorage> mapper = new HashMap<Restaurant, TweetStorage>();
		
		for(Restaurant r : Allres){
			mapper.put(r, new TweetStorage()); 
		}
		
		for(Tweet t : ts ){
			if(t.getLocRes() != null){
				mapper.get(t.getLocRes()).add(t); 
			}
		}	
		for(Restaurant r : mapper.keySet()){
			TweetStorage t = mapper.get(r); 
			if(t.isEmpty())
				continue; 
			
			double visit = t.countVisits(); 
			double sickVisist = t.getSickTweets().countVisits(); 
			
			locTotalSickVisits += sickVisist; 
			locTotalVisits += visit; 
			locTotalTweets += t.size();
			locTotalSickTweets += t.getSickTweets().size();
			
			double results = calcScore(sickVisist, visit); 
			geoScore.put(r, results);
		}
	}
	
	private double nameScore(Restaurant r, InvertedIndex ii) {
		
		double result = 0;
		TweetStorage tweets = ii.nameQuery(r);
		TweetStorage sickTweets = tweets.getSickTweets();
		
		if(tweets.isEmpty()){
			return 0; 
		}
		boolean flag = false; 
		if(tweets.get(0).getNameRes() == null){
			flag  = true; 
			geotaggedMentions += tweets.getGeotaggedTweets().size();
		}
		for(Tweet t: tweets){
			t.setNameRes(r);
		}
		
		if(flag){
			nameTotalSickVisits += sickTweets.countVisits(); 
			nameTotalVisits += tweets.countVisits(); 
			nameTotalTweets += tweets.size();
			nameTotalSickTweets += tweets.getSickTweets().size();
		}
		
		double visit = tweets.countVisits(); 
		double sickVisit = sickTweets.countVisits(); 
		result = calcScore(sickVisit, visit) * (1/RestaurantNameCounter.get(r.getName()).doubleValue()); 
	
		nameScore.put(r, result);
		return result;
	}

	public void ScoreSystem(Grid grid, InvertedIndex ii,TweetStorage allTweets, List<Restaurant> allRestaurants){
		System.out.println("Starting init");
		init(allRestaurants); 
		System.out.println("starting geotagging score");
		for(Restaurant r : allRestaurants ){
			setLocRes(r, grid); 
			nameScore(r, ii); 
		}
		calcGeotagged(allTweets, allRestaurants);
	 

		
		for(Tweet t: allTweets){
			if(t.hasVisited()){
				if(t.uniqueLocation())
					uniqueLoc++;
				else if(t.uniqueMention())
					uniqueMen++;
				else if(t.conflict())
					diff++;
				else 
					equal++; 
			}
		}
	
		statistics(allTweets,allRestaurants);
		
		combinedScore(allTweets,allRestaurants); 
		
		printCounts(allTweets);
		System.out.println("i am done now");
	}
	
	public void printCounts(TweetStorage ts){
		StringBuilder builder = new StringBuilder(); 
		
		builder.append("VISIT STATS" + "\n");
		builder.append("Total Locations visists: " + locTotalVisits +"\n"); 
		builder.append("Total Mentions visists: " + nameTotalVisits +"\n"); 
		builder.append("Total Sick L:" + locTotalSickVisits + "\n");
		builder.append("Total Sick M:" + nameTotalSickVisits + "\n"); 
		
		builder.append("TWEET STATS" + "\n");
		builder.append("Total mentions tweets: " + nameTotalTweets + "\n");
		builder.append("Total sick name tweets: " + nameTotalSickTweets + "\n");
		builder.append("Total loc tweets: " + locTotalTweets + "\n");
		builder.append("Total mentions loc tweets: " + locTotalSickTweets + "\n");
		builder.append("Unique L:" + uniqueLoc +"\n");
		builder.append("Unique M:" + uniqueMen + "\n"); 
		builder.append("Diff: " + diff + "\n"); 
		builder.append("Equal: " + equal + "\n");
		builder.append("Conflict solved with loc: " + conflictLoc + "\n");
		builder.append("Conflict solved with name; " + conflictName + "\n");
		builder.append("Sick Tweets: " + ts.getSickTweets().size() + "\n");
		builder.append("GeoTagged: " + ts.getGeotaggedTweets().size() + "\n");
		builder.append("Geotagged mentions: " + geotaggedMentions + "\n");
		GenericPrint.PRINTER("counters.txt",builder.toString());
		GenericPrint.PRINTER("ConflicsSolvedByLoc.txt", conLoc.toString());
		GenericPrint.PRINTER("ConflicsSolvedByname.txt",conName.toString());

		GenericPrint.PRINTER("counters.txt", builder.toString());

		System.out.println(builder.toString());
		
		
	}

	public  void statistics(TweetStorage ts, List<Restaurant> restaurants){
		
		Map<Restaurant,TweetStorage> withLoc = new HashMap<Restaurant,TweetStorage>();
		Map<String, TweetStorage> withName = new HashMap<String,TweetStorage>();
		TweetStorage conflicts  = new TweetStorage(); 
		TweetStorage same = new TweetStorage(); 
		for(Restaurant r : restaurants){
			withLoc.put(r, new TweetStorage());
			withName.put(r.getName(), new TweetStorage());
		}
		
		
		for(Tweet t : ts){
			if(t.hasVisited()){
				if(t.uniqueLocation()){
					withLoc.get(t.getLocRes()).add(t);
				}
				else if(t.uniqueMention()){
					withName.get(t.getNameRes().getName()).add(t);
				}
				else if(t.conflict()){
					conflicts.add(t);
					withLoc.get(t.getLocRes()).add(t);
					withName.get(t.getNameRes().getName()).add(t);
				}
				else if(t.resSame()){
					same.add(t);
					withLoc.get(t.getLocRes()).add(t);
					withName.get(t.getNameRes().getName()).add(t);
					}
			}
		}
		
		StringBuilder loc = new  StringBuilder(); 
		StringBuilder name = new StringBuilder(); 
		StringBuilder con = new StringBuilder(); 
		StringBuilder sam = new StringBuilder(); 
		
		StringBuilder sickLoc = new  StringBuilder(); 
		StringBuilder sickName = new StringBuilder(); 
		
		for(Restaurant r : withLoc.keySet()){
			if(!withLoc.get(r).isEmpty()){
				loc.append(r.getName() + "----------------------------------------------\n"); 
				loc.append("count: " + withLoc.get(r).size() + "\n" );
				
				for(Tweet t : withLoc.get(r)){
					loc.append(t.toString() + "\n");
				}
			}
			if(!withLoc.get(r).getSickTweets().isEmpty()){
				sickLoc.append(r.getName() + "----------------------------------------------\n"); 
				sickLoc.append("count: " + withLoc.get(r).size() + "\n" );
				sickLoc.append("sickCount: " + withLoc.get(r).getSickTweets().size() + "\n");
				for(Tweet t : withLoc.get(r).getSickTweets()){
					sickLoc.append(t.toString() + "\n");
				}
			}
		}
		
		for(String s : withName.keySet()){
			if(!withName.get(s).isEmpty()){
				name.append(s + "----------------------------------------------\n"); 
				name.append("count: " + withName.get(s).size() + "\n");
				
				for(Tweet t : withName.get(s)){
					name.append(t.toString() + "\n"); 
				}
			}
			if(!withName.get(s).getSickTweets().isEmpty()){
				sickName.append(s + "----------------------------------------------\n");
				sickName.append("count: " + withName.get(s).size() + "\n");
				sickName.append("sickCount: " + withName.get(s).getSickTweets().size() + "\n");
				for(Tweet t : withName.get(s).getSickTweets()){
					sickName.append(t.toString() + "\n"); 
				}
			}
		}
		
		for(Tweet t : conflicts){
			con.append("Loc: " + t.getLocRes().getName() + "Name: " + t.getNameRes().getName() + t.toString() + "\n");
		}
		
		for(Tweet t: same){
			sam.append("Loc: " + t.getLocRes().getName() + "Name: " + t.getNameRes().getName() + t.toString() + "\n");
		}	
		
		GenericPrint.PRINTER("SickLoc.txt", sickLoc.toString());
		GenericPrint.PRINTER("SickName.txt", sickName.toString());
		GenericPrint.PRINTER("location.txt", loc.toString() );
		GenericPrint.PRINTER("name.txt", name.toString());
		GenericPrint.PRINTER("same.txt",sam.toString());
		GenericPrint.PRINTER("conflicts.txt", con.toString());
	}
	
	
	private void combinedScore(TweetStorage ts, List<Restaurant> restaurants){
		Map<Restaurant,TweetStorage> map = new HashMap<Restaurant,TweetStorage>();
		Map<String,TweetStorage> nameCounter = new HashMap<String,TweetStorage>();
		//INIT MAP
		for(Restaurant r : restaurants){
			nameCounter.put(r.getName(), new TweetStorage());
		}
		for(Restaurant r : restaurants){
			map.put(r, new TweetStorage()); 
		}
		//Insert into map and handle conflicts
		for(Tweet t : ts){
			if(t.hasVisited()){
				if(t.getNameRes() == null)
					map.get(t.getLocRes()).add(t);
				else if(t.getLocRes() == null && !t.isGeotagged())
					nameCounter.get(t.getNameRes().getName()).add(t);
				else if(t.getLocRes() == null && t.isGeotagged()){
					continue;
				}
				else if(t.conflict()){
					map.get(handleConflict(t)).add(t);
				}
				else
					map.get(t.getLocRes()).add(t);
			}		
		}

		for(Restaurant r : map.keySet()){
			TweetStorage tweets  = map.get(r);
			TweetStorage sickTweets = tweets.getSickTweets();
			if(tweets.isEmpty() && nameCounter.get(r.getName()).isEmpty())
				continue; 
			
			double nameResults = 0; 
			double mSick = 0; 
			double mTotal = 0; 
			if(!nameCounter.get(r.getName()).isEmpty()){
				mSick = nameCounter.get(r.getName()).getSickTweets().countVisits() / RestaurantNameCounter.get(r.getName()).doubleValue(); 
				mTotal = nameCounter.get(r.getName()).countVisits() / RestaurantNameCounter.get(r.getName()).doubleValue();		
			}
			
			double results = 0; 
			
			if(tweets.isEmpty()){ //only from mentions
				combinedScore.put(r,nameScore.get(r));
				noMcombinedScore.put(r, new Double(0));
			}
			else{
				results = calcScore((double)sickTweets.countVisits() + mSick,(double)tweets.countVisits() + mTotal);
				double results2 = calcScore((double)sickTweets.countVisits() , (double)tweets.countVisits()); 
				combinedScore.put(r, results);
				noMcombinedScore.put(r,results);
				conservative.put(r, results2);	
			}
		}
		
	}
	

	private double calcScore(double sick, double normal){
		return  sick / normal;
	}
	
	
	private Restaurant handleConflict(Tweet t){
		
		if(t.getLocRes().getName().equals(t.getNameRes().getName())){
			return t.getLocRes(); 
		}	
		//second case
		for(Restaurant r : restaurantsWithSameName.get(t.getNameRes().getName())){
			if(Distance.getDist(r,t) < Constants.restaurantDistance){
				conflictName++;
				conName.append("name: " + t.getNameRes().getName() + " loc:" + t.getLocRes().getName() + ", tweet: "+ t.toString() + "\n");
				return r; 
			}
		}
		conflictLoc++; 
		conLoc.append("name: " + t.getNameRes().getName() + " loc:" + t.getLocRes().getName() + ", tweet: "+ t.toString() + "\n");
		return t.getLocRes();	
	}
}