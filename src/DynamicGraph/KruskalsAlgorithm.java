package DynamicGraph;

import java.util.*;

public class KruskalsAlgorithm implements Algorithm{

	private Graph graph;
	private boolean done;
	private boolean used[];
	private int ind; // Index of the sorted edges array
	private int parent[];

	public KruskalsAlgorithm(Graph g){
		parent = new int[g.V];
		for(int i = 0; i < g.V; i++) parent[i]=-1;
		used = new boolean[g.E];
		graph = g;
		ind = 0;
		// Sort edges
	}

	private int find(int p){
		if(parent[p] < 0) return p;
		else return (parent[p] = find(parent[p]));
	}

	private boolean join(int p1, int p2){
		int u = find(p1);
		int v = find(p2);
		if(u == v) return false;
		if(parent[u] > parent[v]) parent[u] = v;
		else{
			if(parent[u] == parent[v]) parent[u]--;
			parent[v] = u;
		}
		return true;
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
		int i = findSmallest();
		if(i == -1){ 
			done = true;
			return;
		}

		Edge e = graph.Edges[i];
		e.Color = Colors.EHIGHLIGHTED;
		// 2) If it doesn't form a cycle then highlight it
		if(join(e.Begin, e.End)){
			// Focus Edge
			e.Color = Colors.EDONE;
			// Join the verticies 
		}else{
			e.Color = Colors.EFOCUSED;
		}
		used[i] = true; // Say that it is used

		System.out.println(parent);
	}

	public boolean isDone(){
		return done;
	}
}
