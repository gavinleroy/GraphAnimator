package DynamicGraph;

import java.util.Queue;
import java.util.LinkedList;

class BFSAlgorithm implements Algorithm {

	private Graph graph;
	private boolean done;
	private boolean itr;
	private Queue<Integer> queue;
	private boolean[] visited;

	private int graphI;
	private int adjI;

	//TODO: Change the weights of the edges to be the distance from the source node

	public BFSAlgorithm(Graph g){
		queue = new LinkedList<Integer>();
		visited = new boolean[g.V];
		graph = g;
		done = itr = false;
		addV(0);
	}

	private void addV(int i){
		if(!visited[i]){
			queue.add(i);
			visited[i] = true;
			graph.Verticies[i].Color = Colors.VFOCUSED;
		}
	}

	public void step(){
		if(!itr){ // Get the next element from the queue
			if(queue.size() == 0) done = true;
			else{
				graphI = queue.peek();
				queue.remove();
				itr = true;
				adjI = 0;
				graph.Verticies[graphI].Color = Colors.VFOCUSED;
			}
		}else{
			if(adjI > 0) // Set the highlighted edge back to initial state
				graph.Edges[graph.EdgeIndex[graphI][graph.G[graphI].get(adjI-1)]].Color = Colors.INIT;
			if(adjI < graph.G[graphI].size()){ // Have more neighbors to add
				int neighbor = graph.G[graphI].get(adjI);
				addV( neighbor );
				// Set the edge to adjacent neighbor as focused
				graph.Edges[graph.EdgeIndex[graphI][neighbor]].Color = Colors.EFOCUSED;
				adjI++;
			}else{ // Done adding neighbors
				graph.Verticies[graphI].Color = Colors.VDONE; // Update color
				itr = false;
			}
		}
	}

	public boolean isDone(){
		return done;
	}
}
