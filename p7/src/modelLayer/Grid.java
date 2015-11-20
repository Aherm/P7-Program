package modelLayer;

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
	
	public TweetStorage rangeQuery(Box queryBox) {
		TweetStorage result = new TweetStorage();
		int mini = geti(queryBox.getBottom());
		int maxi = geti(queryBox.getTop());
		int minj = getj(queryBox.getLeft());
		int maxj = getj(queryBox.getRight());
		
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
		int i = geti(t);
		int j = getj(t);		
		grid[i][j].add(t);
	}
	
	public void removeTweet(Tweet t) {
		int i = geti(t);
		int j = getj(t);
		grid[i][j].remove(t);
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
