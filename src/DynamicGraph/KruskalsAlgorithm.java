package DynamicGraph;

import java.util.*;

public class KruskalsAlgorithm implements Algorithm{

	private Graph graph;
	private boolean done;
	private boolean used[];
	private int ind; // Index of the sorted edges array

	public KruskalsAlgorithm(Graph g){
		used = new boolean[g.E];
		graph = g;
		ind = 0;
		// Sort edges
	}

	private boolean focus(int u, int v){
		//TODO: implement
		// Should focus both U and V along with its edge
		return false;
	}

	private int findSmallest(){
		int minI = -1, min = Integer.MAX_VALUE;
		for(int i = 0; i < graph.Edges.length; i++){
			if(graph.Edges[i].Weight < min && !used[i]){
				minI = i;
				min = graph.Edges[i].Weight;
			}
		}
		return minI;
	}

	public void step(){
		// 1) Pick next smallest edge
		int edgeI = findSmallest();
		graph.Edges[i].Color = Colors.EHIGHLIGHTED;
		// 2) If it doesn't form a cycle then highlight it
		if(noCycle(i)){
			// Focus Edge
			graph.Edges[i].Color = Colors.EDONE;
			// Join the verticies 
		}else{
			used[i] = true; // Say that it is used
			graph.Edges[i].Color = Colors.EFOCUSED;
		}
	}

	public boolean isDone(){
		return done;
	}
}
