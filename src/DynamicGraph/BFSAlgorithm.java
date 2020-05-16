package DynamicGraph;

import java.util.Queue;
import java.util.LinkedList;

class BFSAlgorithm implements Algorithm {
	private final int FOCUSED = 1;
	private final int ADDED = 2;
	private final int DONE = 3;

	private Graph graph;
	private boolean done;
	private boolean itr;
	private Queue<Integer> queue;
	private boolean[] visited;

	private int graphI;
	private int adjI;

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
			graph.Verticies[i].Color = ADDED;
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
				graph.Verticies[graphI].Color = FOCUSED;
			}
		}else{
			if(adjI < graph.G[graphI].size()){ // Have more neighbors to add
				addV( graph.G[graphI].get(adjI) );
				adjI++;
			}else{ // Done adding neighbors
				graph.Verticies[graphI].Color = DONE; // Update color
				itr = false;
			}
		}
	}

	public boolean isDone(){
		return done;
	}
}
