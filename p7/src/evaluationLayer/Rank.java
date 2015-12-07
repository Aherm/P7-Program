package evaluationLayer;

import modelLayer.Restaurant;

public class Rank {

	private Restaurant res; 
	private int rank; 
	private double score; 
	
	public void setRank(int rank){
		this.rank = rank; 
	}
	
	public int getRank(){
		return this.rank; 
	}
	
	public void setScore(int score){
		this.score = score; 
	}
	
	public double getScore(){
		return this.score; 
	}
	
	public Restaurant getRestaurant(){
		return this.res; 
	}
	
	public Rank(Restaurant r, double score){
		this.res = r; 
		this.score = score; 
	}
}
