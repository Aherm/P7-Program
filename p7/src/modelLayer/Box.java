package modelLayer;

public class Box {
	private double x1, x2, y1, y2;

	public Box(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	
	public double getWidth() {
		return Math.abs(x2 - x1);
	}
	
	public double getHeight() {
		return Math.abs(y2 - y1);
	}
	
	public Boolean contains(Tweet t) {
		return (t.getLat() < this.y1) && (t.getLat() > this.y2) && (t.getLon() > this.x1) && (t.getLon() < this.x2);
	}
	
	public Boolean contains(Box b) {
		return (this.x1 < b.x1) && (this.x2 > b.x2) && (this.y1 > b.y1) && (this.y2 < b.y2);
	}
	
	public boolean intersects(Box b) {
		return !((this.x2 < b.x1) ||
				(b.x2 < this.x1) || 
				(y2 > b.y1) ||
				(b.y2 > this.y1));
	}
	
	public double getX1() {
		return x1;
	}

	public void setX1(double x1) {
		this.x1 = x1;
	}

	public double getX2() {
		return x2;
	}

	public void setX2(double x2) {
		this.x2 = x2;
	}

	public double getY1() {
		return y1;
	}

	public void setY1(double y1) {
		this.y1 = y1;
	}

	public double getY2() {
		return y2;
	}

	public void setY2(double y2) {
		this.y2 = y2;
	}
}