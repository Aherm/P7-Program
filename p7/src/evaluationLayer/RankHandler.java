package evaluationLayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fileCreation.GenericPrint;

public class RankHandler {

	
	public List<Rank> ourRanks; 
	public List<Rank> dohmRanks; 
	
	public RankHandler(List<Rank> dohmh, List<Rank> our){
		this.ourRanks = our; 
		this.dohmRanks = dohmh; 
	}
	
	public void printRanks(){
		removeNotIn();  
		Collections.sort(dohmRanks,new ScoreCompare());
		Collections.sort(ourRanks, new ScoreCompare());
		setRanks(dohmRanks);
		setRanks(ourRanks);
		
		StringBuilder builder = new StringBuilder(); 
		for(Rank r : dohmRanks){
			for(Rank ourR : ourRanks){
				if(r.getRestaurant().equals(ourR.getRestaurant())){
					builder.append(r.getRank() +";" + ourR.getRank() + "\n");  
				}
			}
		}
		
		GenericPrint.PRINTER("ranks", builder.toString());
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
	
	private void removeNotIn(){
		List<Rank> rankList = new ArrayList<Rank>();
		
		for(Rank r : dohmRanks){
			for(Rank our : ourRanks){
				if(r.getRestaurant().equals(our.getRestaurant())){
					rankList.add(r);
				}
			}
		}
		
		dohmRanks = rankList; 
	}
	
	
}
