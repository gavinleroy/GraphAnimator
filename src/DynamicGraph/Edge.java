package DynamicGraph;

import java.util.*;

public class Edge{
	public boolean isWeighted;
	public boolean isDirected;
	public int Weight;
	public int Color;
	public int Begin;
	public int End;
	public Edge(boolean _w, boolean _d, int w, int c, int p1, int p2){
		this.isWeighted = _w;
		this.isDirected = _d;
		this.Weight = w;
		this.Color = c;
		this.Begin = p1;
		this.End = p2;
	}
}
