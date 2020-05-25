package DynamicGraph;

import java.util.Queue;
import java.util.LinkedList;

class BFSAlgorithm implements Algorithm {

	private Graph graph;
	private boolean done;
	private boolean itr;
	private Queue<Integer> queue;

	private int graphI;
	private int adjI;
	private int dist;

	public BFSAlgorithm(Graph g, int s){
		queue = new LinkedList<Integer>();
		graph = g;
		done = itr = false;
		graph.Verticies[s].Distance = 0;
		addV(s);
	}

	private boolean addV(int i){
		if(!graph.visited[i]){
			queue.add(i);
			graph.visited[i] = true;
			graph.Verticies[i].Color = Colors.VFOCUSED;
			return true;
		}
		return false;
	}

	public void step(){
		if(done) return;

		if(!itr){ // Get the next element from the queue
			if(queue.size() == 0) done = true;
			else{
				graphI = queue.poll();
				itr = true;
				adjI = 0;
			}
		}else{
			if(adjI < graph.G[graphI].size()){ // Have more neighbors to add
				int neighbor = graph.G[graphI].get(adjI);
				if(addV( neighbor )){
					graph.Verticies[neighbor].Distance = 
				       		graph.Verticies[graphI].Distance + 
					        graph.Weights[graphI][neighbor];	
					graph.Edges[graph.EdgeIndex[graphI][neighbor]].Color = Colors.TREEEDGE;
				}else{
					if(graph.Edges[graph.EdgeIndex[graphI][neighbor]].Color != Colors.TREEEDGE)
						graph.Edges[graph.EdgeIndex[graphI][neighbor]].Color = Colors.BACKEDGE;
				}
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
