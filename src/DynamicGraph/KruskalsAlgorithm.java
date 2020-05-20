package DynamicGraph;

import java.util.*;

public class KruskalsAlgorithm implements Algorithm{

	private Graph graph;
	private boolean done;
	private int ind; // Index of the sorted edges array

	public KruskalsAlgorithm(Graph g){
		graph = g;
		ind = 0;
		// Sort edges
	}

	private boolean focus(int u, int v){
		//TODO: implement
		// Should focus both U and V along with its edge
		return false;
	}

	public void step(){
		//TODO: implement
		// 1) Pick next smallest edge
		// 2) If it doesn't form a cycle then highlight it
		// otherwise skip
	}

	public boolean isDone(){
		return done;
	}
}
