package modelLayer;

import utility.Distance;

public class Grid {
	private TweetStorage[][] grid;
	private Box gridBox;
	
	private double cellWidth;
	private double cellHeight;
	
	public Grid(double leftX, double rightX, double bottomY, double topY, int rowAmount, int columnAmount) {
		this.gridBox = new Box(leftX, rightX, bottomY, topY);
		
		this.cellWidth = gridBox.getWidth() / rowAmount;
		this.cellHeight = gridBox.getHeight() / columnAmount;
		
		this.grid = new TweetStorage[rowAmount][columnAmount];
		for (int i = 0; i < rowAmount; i++) {
			for (int j = 0; j < columnAmount; j++) {
				grid[i][j] = new TweetStorage();
			}
		}
	}
	
	public TweetStorage rangeQuery(OurLocation loc, double dist) {
		Box queryBox = Distance.getBoundingBox(loc, dist);
		TweetStorage result = new TweetStorage();
		
		for (Tweet t : rangeQuery(queryBox)) {
			if (Distance.getDist(t, loc) < dist) {
				result.add(t);
			}
		}
		
		return result;
	}
	
	public TweetStorage rangeQuery(Box queryBox) {
		TweetStorage result = new TweetStorage();		

		int mini = Math.max(geti(queryBox.getBottom()), 0);
		int maxi = Math.min(geti(queryBox.getTop()), grid.length - 1);
		int minj = Math.max(getj(queryBox.getLeft()), 0);
		int maxj = Math.min(getj(queryBox.getRight()), grid[0].length - 1);
		
		for (int i = mini; i <= maxi; i++) {
			for (int j = minj; j <= maxj; j++) {
				TweetStorage cell = grid[i][j];
				double cellLeft = j * cellWidth + gridBox.getLeft();
				double cellRight = cellLeft + cellWidth;
				double cellBottom = i * cellHeight + gridBox.getBottom();
				double cellTop = cellBottom + cellHeight;
				Box cellBox = new Box(cellLeft, cellRight, cellBottom, cellTop);
				
				if (queryBox.contains(cellBox)) {
					result.addAll(cell);
				}
				else if (queryBox.intersects(cellBox)) {
					for (Tweet t : cell) {
						if (queryBox.contains(t)) {
							result.add(t);
						}
					}
				}
			}
		}
		
		return result;
	}
	
	public TweetStorage getCell(int i, int j) {
		return grid[i][j];
	}
	
	public void addTweet(Tweet t) {
		if (t.isGeotagged() && gridBox.contains(t)) {
			int i = geti(t);
			int j = getj(t);
			grid[i][j].add(t);
		}
	}
	
	public void addTweets(TweetStorage ts) {
		for (Tweet t : ts) {
			this.addTweet(t);
		}
	}
	
	public void removeTweet(Tweet t) {
		if (t.isGeotagged() && gridBox.contains(t)) {
			int i = geti(t);
			int j = getj(t);
			grid[i][j].remove(t);
		}
	}
	
	public int geti(Tweet t) {
		return geti(t.getLat());
	}
	
	public int getj(Tweet t) {
		return getj(t.getLon());
	}
	
	private int geti(double y) {
		double vdist = y - gridBox.getBottom();
		return (int) (vdist / cellWidth);
	}
	
	private int getj(double x) {
		double hdist = x - gridBox.getLeft();
		return (int) (hdist / cellHeight);
	}
}
