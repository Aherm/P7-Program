package modelLayer;

public class Box {
	private double left, right, top, bottom;

	public Box(double leftX, double rightX, double bottomY, double topY) {
		this.left = leftX;
		this.right = rightX;
		this.top = topY;
		this.bottom = bottomY;
	}
	
	public double getWidth() {
		return Math.abs(right - left);
	}
	
	public double getHeight() {
		return Math.abs(top - bottom);
	}
	
	public Boolean contains(Tweet t) {
		return (t.getLat() < this.top) && (t.getLat() > this.bottom) && (t.getLon() > this.left) && (t.getLon() < this.right);
	}
	
	public Boolean contains(Box b) {
		return (this.left < b.left) && (this.right > b.right) && (this.top > b.top) && (this.bottom < b.bottom);
	}
	
	public boolean intersects(Box b) {
		return !((this.right < b.left) ||
				(b.right < this.left) || 
				(this.bottom > b.top) ||
				(b.bottom > this.top));
	}
	
	public double getLeft() {
		return left;
	}

	public double getRight() {
		return right;
	}

	public double getTop() {
		return top;
	}

	public double getBottom() {
		return bottom;
	}
}