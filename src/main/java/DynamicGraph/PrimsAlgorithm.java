package DynamicGraph;

import java.util.*;

public class PrimsAlgorithm implements Algorithm{

	private Graph graph;
	private Set<Integer> mstSet;
	private int[] parent;
	private boolean[] finished;
	private boolean done;
	private boolean itr;
	private int graphI;
	private int adjI;

	public PrimsAlgorithm(Graph g, int s){
		finished = new boolean[g.V];
		mstSet = new HashSet<Integer>();
		parent = new int[g.V];
		graph = g;
		done = itr = false;
		graph.Verticies[s].Distance = 0;
		focus(s, s);
	}

	/*
	 * Focus a specific node, this relaxes the distance from INF -> real 
	 */
	private boolean focus(int u, int v){
		int ei = (u != v) ? graph.EdgeIndex[u][v] : -1;
		int nd = graph.Weights[u][v]; // New dist

		if(!finished[v] && graph.Verticies[v].Distance > nd || u == v){ 
			parent[v] = u;
			graph.Verticies[v].Distance = nd; 		
			graph.Verticies[v].Color = Colors.VFOCUSED; // Set color as focused
			if(ei >= 0) graph.Edges[ei].Color = Colors.EFOCUSED; // Set edge as focused
			return true;
		}
		return false;
	}

	private int findClosest(){
		int v = -1; 
		int d = Integer.MAX_VALUE;
		for(int i = 0; i < graph.Verticies.length; i++){
			if(!mstSet.contains(i) && graph.Verticies[i].Distance < d){
				v = i;
				d = graph.Verticies[i].Distance;
			}	
		}
		return v;
	}

	public void step(){
		if(!itr){
			int c = findClosest();
			if(c < 0){
				for(int i = 0; i < graph.Edges.length; i++){
					if(graph.Edges[i].Color != Colors.EDONE)
						graph.Edges[i].Color = Colors.EFOCUSED;
				}	
				done = true;
				return;
			}
			mstSet.add(c);
			finished[c] = true;
			graph.Verticies[c].Color = Colors.VDONE;
			// Add Edge into graph with color
			if(parent[c] != c) graph.Edges[graph.EdgeIndex[parent[c]][c]].Color = Colors.EDONE;
			itr = true;
			graphI = c;
			adjI = 0;
		}else{
			
			while(adjI < graph.G[graphI].size() && !focus(graphI, graph.G[graphI].get(adjI))) 
				adjI++;
			adjI++;
			if(adjI >= graph.G[graphI].size()) itr = false;
		}
	}

	public boolean isDone(){
		return done;
	}
}
