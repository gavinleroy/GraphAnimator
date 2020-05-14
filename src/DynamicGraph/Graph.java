package DynamicGraph;

import java.util.*;

public class Graph {

	public static enum Property{
		CONNECTED,    // Default is a sparse graph
		DIRECTED, // Default is an undirected graph
		WEIGHTED, // Default is an unweighted graph
		NEGATIVE, // Default is strictly positive edges
		DAG
	}
	private final HashSet<Property> properties;
	public final int V;
	public final int E;

	private Random rand;
	private ArrayList<Integer>[] G;
	private Point[] Locations;
	private Vertex[] Verticies;
	private Edge[] Edges;
	private int[][] Weights;

	/*
	 * Constructor ** 
	 * @param int v: the number of desired verticies in the final graph
	 * @param HashSet<> args: A set of all the desired properties of the graph
	 * 	which will determine in how the graph is generated.
	 * */
	public Graph(int v, HashSet<Property> args){
		rand = new Random();
		V = v;
		E = v * 2;
		properties = args;
		init(); // Initialize data structures
		generate_verticies(); // Generate Verticies
		generate_edges(); // Generae Edges
	}
	
	/*
	 * Initialize data structes for graph,
	 * call functions to generate Locations, 
	 * Weights, Edges, and Verticies
	 * */
	private void init(){
		// Initialize Graph
		G = new ArrayList[V];
		for(int i = 0; i < V; i++){
			G[i] = new ArrayList();
		}
		// Initialize Location
		Locations = new Point[V];
		// Initialize Edges
		Edges = new Edge[E];
		// Initialize Verticies
		Verticies = new Vertex[V];
		// Initialize Weight matrix
		Weights = new int[V][V];
		generate_locations();
		generate_weights();
	}

	/*
	 * Generate the Weights for each possible edge.
	 * The weights are generated before the edges for 
	 * increased randomness, each edge weight is generated 
	 * regardless of whether or not the edge will actually exist 
	 * in the final graph
	 * */
	private void generate_weights(){
		for(int i = 0; i < V; i++){
			// If undirected we only need to do the upper triangle of matrix
			int j = properties.contains(Property.DIRECTED) ? 0 : i;
			for(; j < V; j++){
				if(properties.contains(Property.WEIGHTED)) 
					Weights[i][j] = rand.nextInt(V*2);
				else 
					Weights[i][j] = 1;
				// If negative is allowed each edge has 1/3 chance of being negative
				if(properties.contains(Property.NEGATIVE) && rand.nextInt() % 3 == 0) 
					Weights[i][j] *= -1;
				// If undirected then make sure both sides are same weight
				if(!properties.contains(Property.DIRECTED)) 
						Weights[j][i] = Weights[i][j];
			}
		}
	}

	/*
	 * Generates the location of all V verticies:
	 * said verticies are placed on a circle equally spaced out
	 * the location x,y are kept in the range: [0.0, 1.0] 
	 * which are meant to be scaling factors for however the points will be 
	 * displayed
	 * */
	private void generate_locations(){
		double radius = 0.5; // radius of circle is 0.5 = (1.0 - 0.0) / 2
		double originX = 0.5; // Center of said cirlce is (0.5, 0.5)
		double originY = 0.5;
		double angleFactor = 2.0 * Math.PI / V; // Angle difference for V verticies
		for(int i = 0; i < V; i++){
			double angle = i * angleFactor; // Get angle for next vertex
			double x = originX + radius * Math.cos(angle); 
			double y = originY + radius * Math.sin(angle);
			Locations[i] = new Point(x, y);	
		}
	}

	/*
	 * Generate the list of Verticies that will be passed to the view
	 * the 'Name' of each vertex is its index [0,V)
	 * */
	private void generate_verticies(){
		for(int i = 0; i < V; i++){
			Verticies[i] = new Vertex(i, 1, Locations[i]);
		}
	}

	/* TODO: better implement generating edges based on desired density of graph */
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
				G[u].add(v);
				// If undirected count edge for both directions
				if(!properties.contains(Property.DIRECTED)){
					ee[v][u] = true; 
					G[v].add(u);
				}
				Edges[edgec++] = new Edge(properties.contains(Property.WEIGHTED), 
							properties.contains(Property.DIRECTED), 
							Weights[u][v], 0, Locations[u], Locations[v]);
			}
		}
	}

	// Returns the list of Verticies generated
	public Vertex[] get_verticies(){
		return Verticies;
	}

	// Returns the list of Edges generated
	public Edge[] get_edges(){
		return Edges;
	}
}
