package DynamicGraph;

import java.util.*;

public class Graph {
	public static enum Property{
		CONNECTED, // Default is a sparse graph
		DIRECTED,  // Default is an undirected graph
		WEIGHTED,  // Default is an unweighted graph
		NEGATIVE,  // Default is strictly positive edges
			   // For a graph to be negative it also needs
			   // to be weighted
		ACYCLIC
	}
	private final HashSet<Property> properties;
	public final int V;
	public final int E;

	protected ArrayList<Integer>[] G;
	protected Vertex[] Verticies;
	protected int[][] Weights;
	protected int[][] EdgeIndex;
	protected Edge[] Edges;
	protected boolean[] visited;
	private Random rand;
	private Point[] Locations;

	/*
	 * Constructor ** 
	 * @param int v: the number of desired verticies in the final graph
	 * @param HashSet<> args: A set of all the desired properties of the graph
	 * 			  which will determine in how the graph is generated.
	 * */
	public Graph(int v, HashSet<Property> args){
		rand = new Random();
		V = v;
		properties = args;
		init(); // Initialize data structures
		generate_verticies(); // Generate Verticies
		E = generate_edges(); // Generae Edges which returns the number
				      // of edges actually generated
	}
	
	/*
	 * Initialize data structes for graph,
	 * call functions to generate Locations, 
	 * Weights, Edges, and Verticies
	 * */
	private void init(){
		// Initialize Graph
		visited = new boolean[V];
		G = new ArrayList[V];
		for(int i = 0; i < V; i++){
			G[i] = new ArrayList<Integer>();
		}
		// Initialize Location
		Locations = new Point[V];
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
				if(i == j) Weights[i][j] = 0; // Make sure self edges have no weight
				else if(properties.contains(Property.WEIGHTED)){
					Weights[i][j] = rand.nextInt(V*2);
					// If negative is allowed each edge has 1/3 chance of being negative
					if(properties.contains(Property.NEGATIVE) && rand.nextInt() % 3 == 0) 
						Weights[i][j] *= -1;
				}else Weights[i][j] = 1;
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
			Verticies[i] = new Vertex(i, Colors.INIT, Locations[i]);
		}
	}

	/*
	 * Generate Edges method: generate the edges randomly of a given graph.
	 * Edges are generated using something similar to the Erdős-Rényi model,
	 * we start with a number in the range [0.0,1.0] and for each edge a random
	 * real number is generated. If it lies below the starting number then we add 
	 * the edge to the graph.
	 * This method also takes into account generating Directed, Acyclic, and Connected
	 * graphs. A connected graph merely starts with a propbability of 1.0, therefore, 
	 * every edge will be added. NOTE: for a graph to be acyclic it also needs to be
	 * directed this creating a DAG. The process for a dag is to generate a random 
	 * topological ordering for the nodes and only allow edges that go from a lower
	 * rank to a higher one. 
	 *
	 * @return edgec: the number of edges that are added to the graph which is then 
	 * 		  set to be our this.E for the object.
	 * */
	private int generate_edges(){
		EdgeIndex = new int[V][V];
		ArrayList<Edge> edges = new ArrayList<Edge>();
		boolean [][] ee = new boolean[V][V];
		int [] rank = new int[V]; // Generate random ranks for the verticies
		for(int i = 0; i < V; i++) rank[i] = rand.nextInt((int)(~(1 << 31)));

		int edgec = 0;
		double prob; // Starting probability to which edges will be tested
		boolean edge;  // bool to check whether the edge passed probability test
		boolean dir = properties.contains(Property.DIRECTED);
		boolean dag = dir && properties.contains(Property.ACYCLIC);
		if(properties.contains(Property.CONNECTED)) prob = 1.0; // Every edge included
		else if(dag) prob = 0.55;
		else prob = 0.25; 
		for(int u = 0; u < V; u++){
			for(int v = 0; v < V; v++){
				edge = false;
				if(!ee[u][v] && u != v){ // If edge doesn't already exist
					double p = rand.nextDouble();
					// If the graph is a DAG
					if(dag && rank[u] < rank[v] && p <= prob)
						edge = ee[u][v] = true;
					else if(!dag && dir && p <= prob) // If the graph is just Directed
						edge = ee[u][v] = true;
					else if(!dir && p <= prob){ // If undirected
						edge = ee[u][v] = ee[v][u] = true;
						G[v].add(u);
						EdgeIndex[v][u] = edgec;
					}
				}	
				if(edge){ // add edge and increase edge count
					G[u].add(v); // Add the edge to our adjacenty list
					EdgeIndex[u][v] = edgec;
					edges.add(new Edge(properties.contains(Property.WEIGHTED), dir,
							Weights[u][v], Colors.INIT, Locations[u], Locations[v]));
					edgec++;
				}
			}
		} // Initialize the Edge array now that we know how many edges exist
		Edges = new Edge[edgec]; // Convert ArrayList<Edge> -> Edge[]
		for(int i = 0; i < edges.size(); i++) Edges[i] = edges.get(i);
		return edgec;
	}

	/*
	 * This method is called by the view in order to get an algorithm object
	 * on which it can invoke the step() method.
	 * */
	public Algorithm createAlgorithm(String algo, int s){
		Algorithm ret;
		switch(algo){ // Switch on the label passed in to get right object
			case Algorithm.BFS:
				ret = new BFSAlgorithm(this, s);
				break;
			case Algorithm.DFS:
				ret = new DFSAlgorithm(this, s);
				break;
			case Algorithm.DIJKSTRAS:
				ret = new DijkstrasAlgorithm(this, s);
				break;
			case Algorithm.KRUSKALS:
			case Algorithm.PRIMS:
				ret = new PrimsAlgorithm(this, s);
				break;
			default: 
				ret = new BFSAlgorithm(this, s);
				break;
		}
		return ret;
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
