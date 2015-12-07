package evaluationLayer;

import java.util.Comparator;

public class ScoreCompare implements Comparator<Rank>{
	
	public int compare(Rank rank1, Rank rank2){
		if(rank2.getScore() < rank1.getScore()) return -1; 
		if(rank2.getScore() == rank1.getScore()) return 0; 
		return 1; 
	}
	
}
