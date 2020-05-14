package DynamicGraph;

import java.util.*;

public class Graph {

	public final int V;
	public final int E;

	private Random rand;
	private ArrayList<Integer>[] G;
	private Point[] Locations;
	private Vertex[] Verticies;
	private Edge[] Edges;
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
		E = v * 2;

		// Do something with the list of properties that is passed in
		
		init(false, false); //TODO: allow for dynamic weighted/directed choosing
		generate_verticies();
		generate_edges();
	}
	
	private void init(boolean weighted, boolean directed){
		// Initialize Graph size
		G = new ArrayList[V];
		Locations = new Point[V];
		Edges = new Edge[E];
		Verticies = new Vertex[V];
		for(int i = 0; i < V; i++){
			G[i] = new ArrayList();
		}
		generate_locations();

		// Initialize Weight matrix
		Weights = new int[V][V];
		for(int i = 0; i < V; i++){
			for(int j = 0; j < V; j++){
				if(weighted == false) Weights[i][j] = 1;
				else Weights[i][j] = rand.nextInt(V);
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

	private void generate_verticies(){
		for(int i = 0; i < V; i++){
			Verticies[i] = new Vertex(Character.toString( ((char)(i + 'A')) ), 1, Locations[i]);
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
		return Verticies;
	}

	public Edge[] get_edges(){
		return Edges;
	}
}
