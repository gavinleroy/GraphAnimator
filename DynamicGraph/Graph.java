package DynamicGraph;

import java.util.*;

public class Graph {

	public final int V;
	public final int E;

	private Random rand;
	private ArrayList<Integer>[] G;
	private Point[] Locations;
	private Edge[] Edges;
	private int[] Colors;
	private int[][] Weights;

	public static enum Property{
		CONNECTED,    // Default is a sparse graph
		DIRECTED, // Default is an undirected graph
		WEIGHTED, // Default is an unweighted graph
		NEGATIVE, // Default is strictly positive edges
		DAG
	}

	public Graph(int v, Property ...props){
		rand = new Random();
		V = v;
		E = v * v;

		// Do something with the list of properties that is passed in
		
		init(v, false);
		generate_edges();
	}
	
	private void init(int n, boolean weighted){
		// Initialize Graph size
		G = new ArrayList[n];
		Locations = new Point[n];
		Colors = new int[n];
		Edges = new Edge[E];
		for(int i = 0; i < n; i++){
			G[i] = new ArrayList();
			Colors[i] = 0;
		}
		generate_locations();

		// Initialize Weight matrix
		Weights = new int[n][n];
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				if(weighted == false) Weights[i][j] = 1;
				else Weights[i][j] = rand.nextInt( n );
			}
		}
	}

	private void generate_locations(){
		double radius = 0.5;
		double originX = 0.5;
		double originY = 0.5;
		double angleFactor = 2.0 * Math.PI / V;
		double angle;
		for(int i = 0; i < V; i++){
			angle = i * angleFactor;
			double x = originX + radius * Math.cos(angle);
			double y = originY + radius * Math.sin(angle);
			Locations[i] = new Point(x, y);	
		}
	}

	private void generate_edges(){
		// Will need to take into account all the special properties
		// however, currently just do an undirected unweighted sparse graph	
		boolean [][] ee = new boolean[V][V];
		int edgec = 0;
		while(edgec < E){
			int u = rand.nextInt(V);
			int v = rand.nextInt(V);
			if(u != v && !ee[u][v]){
				ee[u][v] = true;
				ee[v][u] = true; // Initially make it undirected
				G[u].add(v);
				G[v].add(u);
				Edges[edgec++] = new Edge(false, false, 1, Locations[u], Locations[v]);
			}
		}
	}

	public Vertex[] get_verticies(){
		Vertex[] ret = new Vertex[V];
		for(int i = 0; i < V; i++){
			ret[i] = new Vertex(Character.toString( ((char)(i + 'A')) ), Colors[i], Locations[i]);
		}
		return ret;
	}

	public Edge[] get_edges(){
		// It is easies to pre-generate the 
		// array of edges to return in the case that the 
		// graph is undirected
		return Edges;
	}
}
