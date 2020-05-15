package DynamicGraph;

import java.util.Queue;
import java.util.LinkedList;

class BFSAlgorithm implements Algorithm {
	private final int FOCUSED = 2;
	private final int DONE = 3;

	private Graph graph;
	private boolean done;
	private boolean itr;
	private Queue<Integer> queue;

	private int graphI;
	private int adjI;

	public BFSAlgorithm(Graph g){
		queue = new LinkedList<Integer>();
		graph = g;
		done = itr = false;
		addV(0);
	}

	private void addV(int i){
		queue.add(i);
		graph.Verticies[i].Color = FOCUSED;
	}

	public void step(){
		if(!itr){ // Get the next element from the queue
			if(queue.size() == 0) done = true;
			else{
				itr = true;
				graphI = queue.peek();
				adjI = 0;
				queue.remove();
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
