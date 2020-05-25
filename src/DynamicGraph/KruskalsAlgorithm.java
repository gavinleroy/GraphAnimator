package DynamicGraph;

import java.util.*;

public class KruskalsAlgorithm implements Algorithm{

	private Graph graph;
	private boolean done;
	private int ind; // Index of the sorted edges array
	private int added;
	private int parent[];
	private Edge sorted_edges[];

	public KruskalsAlgorithm(Graph g){
		parent = new int[g.V];
		sorted_edges = g.Edges.clone();
		Arrays.sort(sorted_edges); // Sort edges
		for(int i = 0; i < g.V; i++) parent[i]=-1;
		graph = g;
		ind = added = 0;
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

	public void step(){
		if(done) return;
		int i = ind++;
		if(i >= graph.E){ 
			done = true;
			return;
		}
		Edge e = sorted_edges[i];
		// 2) If it doesn't form a cycle then highlight it
		if(join(e.Begin, e.End)){
			// Focus Edge
			e.Color = Colors.EDONE;
			graph.Verticies[e.Begin].Color = Colors.VDONE;
			graph.Verticies[e.End].Color = Colors.VDONE;
			added++;
			// Join the verticies 
		}else{
			e.Color = Colors.EFOCUSED;
			if(added == graph.V-1){
				done = true;
				while(ind<graph.E) sorted_edges[ind++].Color=Colors.EFOCUSED;
			}
		}
	}

	public boolean isDone(){
		return done;
	}
}
