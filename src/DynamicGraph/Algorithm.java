package DynamicGraph;

public interface Algorithm{
	public static final String BFS = "BFS";
	public static final String DFS = "DFS";
	public static final String DIJKSTRAS = "Dijkstras";
	public static final String KRUSKALS = "Kruskals";
	public static final String PRIMS = "Prims";
	void step();	
	boolean isDone();
}
