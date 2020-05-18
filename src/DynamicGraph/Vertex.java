package DynamicGraph;

import java.util.*;

public class Vertex{
	public int ID;
	public Point Location;
	public int Color;
	public int Distance;
	public Vertex(int n, int c, Point p){
		this.ID = n;
		this.Color = c;
		this.Location = p;
		this.Distance = 0;
	}
}
