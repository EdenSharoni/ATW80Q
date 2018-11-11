package playground.examples;

public class Location {
	private double x;
	private double y;

	public Location() {
	}

	public Location(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	

	
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double length()
	{
		return Math.abs(Math.sqrt(x*x+y*y));
	}
	@Override
	public String toString() {
		return "Location [x=" + x + ", y=" + y + "]";
	}

}
