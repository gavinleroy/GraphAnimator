package DynamicGraph;

public class Point{
	public double x;
	public double y;
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}

	public String toString(){
		return Double.toString(x) + " " + Double.toString(y) + "\n";
	}

	@Override
	public boolean equals(Object o){
		if(o==this) return true;
		if(!(o instanceof Point)) return false;
		Point p = (Point)o;
		return Double.compare(x, p.x) == 0 && Double.compare(y, p.y) == 0;
	}

	@Override
	public int hashCode(){
		int hash = 7;
		hash = 71 * hash + (int)(100*this.x);
		hash = 71 * hash + (int)(100*this.y);
		return hash;
	}
}
