package modelLayer;

public class Grid {
	private TweetStorage[][] grid;
	private Box gridBox;
	
	private double cellWidth;
	private double cellHeight;
	
	public Grid(double x1, double y1, double x2, double y2, int cellRowAmount, int cellColumnAmount) {
		this.gridBox = new Box(x1, y1, x2, y2);
		
		this.cellWidth = gridBox.getWidth() / cellRowAmount;
		this.cellHeight = gridBox.getHeight() / cellColumnAmount;
		
		this.grid = new TweetStorage[cellRowAmount][cellColumnAmount];
		for (int i = 0; i < cellRowAmount; i++) {
			for (int j = 0; j < cellColumnAmount; j++) {
				grid[i][j] = new TweetStorage();
			}
		}
	}
	
	public TweetStorage rangeQuery(Box queryBox) {
		TweetStorage result = new TweetStorage();
		int mini = geti(queryBox.getX1());
		int maxi = geti(queryBox.getX2());
		int minj = getj(queryBox.getY1());
		int maxj = getj(queryBox.getY2());
		
		for (int i = mini; i < maxi; i++) {
			for (int j = minj; j < maxj; j++) {
				TweetStorage cell = grid[i][j];
				Box cellBox = new Box(i * cellWidth, j * cellWidth, (i + 1) * cellWidth, (j + 1) * cellWidth);
				
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
	
	// TODO: Error handling when tweet is out of grid range
	public void addTweet(Tweet t) {
		int i = geti(t);
		int j = getj(t);		
		grid[i][j].add(t);
	}
	
	public void removeTweet(Tweet t) {
		int i = geti(t);
		int j = getj(t);
		grid[i][j].remove(t);
	}
	
	private int geti(Tweet t) {
		return geti(t.getLon());
	}
	
	private int getj(Tweet t) {
		return getj(t.getLat());
	}
	
	private int geti(double x) {
		double hdist = Math.abs(x - gridBox.getX1());
		return (int) (hdist / cellWidth);
	}
	
	private int getj(double y) {
		double vdist = Math.abs(y - gridBox.getY1());
		return (int) (vdist / cellHeight);
	}
}
