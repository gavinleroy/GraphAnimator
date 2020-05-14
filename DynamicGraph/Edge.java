package DynamicGraph;

import java.util.*;

public class Edge{
	public boolean Weighted;
	public boolean Directed;
	public int W;
	public Point Begin;
	public Point End;
	public Edge(boolean _w, boolean _d, int w, Point p1, Point p2){
		this.Weighted = _w;
		this.Directed = _d;
		this.W = w;
		this.Begin = p1;
		this.End = p2;
	}
}
