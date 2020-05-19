package DynamicGraph;

import java.util.*;

public class PrimsAlgorithm implements Algorithm{

	private Graph graph;
	private Set<int> mstSet;
	private boolean done;
	private boolean itr;
	private int graphI;
	private int adjI;

	public PrimsAlgorithm(Graph g, int s){
		mstSet = new HashSet<int>();
		graph = g;
		done = itr = false;
		graph.Verticies[s].Distance = 0;
		focus(s, s);
	}

	/*
	 * Focus a specific node, this relaxes the distance from INF -> real 
	 */
	private void focus(int u, int v){
		int ei = graph.EdgeIndex[u][v];
		graph.Verticies[v].Distance = graph.Verticies[u].Distance + graph.Edges[ei].Weight; // New dist
		graph.Verticies[v].Color = Colors.VFOCUSED; // Set color as focused
		graph.Edges[ei].Color = Colors.EFOCUSED; // Set edge as focused
	}

	private int findClosest(){
		int v = -1; 
		int d = Integer.MAX_VALUE;
		for(int i = 0; i < graph.Verticies.length; i++){
			if(graph.Verticies[i].Distance < d){
				v = i;
				d = graph.Verticies[i].Distance;
			}	
		}
		return v;
	}

	public void step(){

	}

	public boolean isDone(){
		return done;
	}
}
