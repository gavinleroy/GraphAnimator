package DynamicGraph;

import java.util.*;

public class Edge{
	public boolean isWeighted;
	public boolean isDirected;
	public int Weight;
	public int Color;
	public Point Begin;
	public Point End;
	public Edge(boolean _w, boolean _d, int w, int c, Point p1, Point p2){
		this.isWeighted = _w;
		this.isDirected = _d;
		this.Weight = w;
		this.Color = c;
		this.Begin = p1;
		this.End = p2;
	}
}
