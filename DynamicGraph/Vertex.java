package DynamicGraph;

import java.util.*;

public class Vertex{
	public String Name;
	public Point Location;
	public int Color;
	public Vertex(String n, int c, Point p){
		this.Name = n;
		this.Color = c;
		this.Location = p;
	}
}
