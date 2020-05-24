package DynamicGraph;

import java.util.*;

public class Edge implements Comparable<Edge>{
	public boolean isWeighted;
	public boolean isDirected;
	public Integer Weight;
	public Integer Color;
	public Integer Begin;
	public Integer End;
	public Edge(boolean _w, boolean _d, int w, int c, int p1, int p2){
		this.isWeighted = _w;
		this.isDirected = _d;
		this.Weight = w;
		this.Color = c;
		this.Begin = p1;
		this.End = p2;
	}
	@Override 
	public int compareTo(Edge e){
		return this.Weight.compareTo(e.Weight);
	}
}
