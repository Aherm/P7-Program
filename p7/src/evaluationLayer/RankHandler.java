package evaluationLayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankHandler {

	
	public List<Rank> ourRanks; 
	public List<Rank> dohmRanks; 
	
	public RankHandler(List<Rank> y, List<Rank> y2){
		this.ourRanks = y; 
		this.dohmRanks = y2; 
	}
	
	public void printRanks(){
		Collections.sort(dohmRanks,new ScoreCompare());
		Collections.sort(ourRanks, new ScoreCompare());
		setRanks(dohmRanks);
		setRanks(ourRanks);
		
		for(Rank r : dohmRanks){
			for(Rank ourR : ourRanks){
				if(r.getRestaurant().equals(ourR.getRestaurant())){
					System.out.println(r.getRestaurant().getName() + " dohmh: " + r.getRank() + " our: " + r.getRank()); 
				}
			}
		}		
	}
	
	
	private void setRanks(List<Rank> ranks){		
		ranks.get(0).setRank(1);
		double lastScore = ranks.get(0).getScore(); 
		int currentRank = 1;
		
		for(Rank r : ranks){
			
			if(r.getScore() != lastScore){
				currentRank++; 
				lastScore = r.getScore(); 
			}
			
			r.setRank(currentRank);
		}
	}
	
	
	
}
