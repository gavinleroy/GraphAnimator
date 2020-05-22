package DynamicGraph;

public class Point{
	public double x;
	public double y;
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode(){
		int hash = 7;
		hash = 71 * hash + (int)(100*this.x);
		hash = 71 * hash + (int)(100*this.y);
		return hash;
	}
}
