package DynamicGraph;

import java.util.PriorityQueue;

class DijkstrasAlgorithm implements Algorithm {
	protected class Node implements Comparable<Node>{
		public Integer u;
		public Integer v;
		public Integer d;
		public Node(int u, int v, int d){
			this.u = u;
			this.v = v;
			this.d = d;
		}
		@Override
		public int compareTo(Node n){
			return this.d.compareTo(n.d);
		}
	}

	private Graph graph;
	private boolean done;
	private boolean itr;
	private PriorityQueue<Node> queue;

	private int graphI;
	private int adjI;
	private int dist;

	public DijkstrasAlgorithm(Graph g, int s){
		queue = new PriorityQueue<Node>();
		graph = g;
		done = itr = false;
		graphI = -1;
		graph.Verticies[s].Distance = 0;
		addV(s, s);
	}

	private void addV(int u, int v){
		if(!graph.visited[v]){
			int nDist = graph.Verticies[u].Distance + graph.Weights[u][v];
			if(graph.Verticies[v].Distance > nDist || graphI == -1){
				graph.Verticies[v].Distance = nDist;
				queue.add(new Node(u, v, nDist));
				graph.Verticies[v].Color = Colors.VFOCUSED;
			}
		}
	}

	public void step(){
		if(done) return;
		if(!itr){ // Get the next element from the queue
			if(queue.size() == 0) done = true;
			else{
				Node n;
				do{ // Pop off verticies until we get one that isn't final
					n = queue.poll();
					if(n == null){ // If all nodes are pulled then we are done
						done = true; 
						return; 
					}
					graphI = n.v; 
				} while(graph.visited[graphI]);
				graph.visited[graphI] = itr = true;
				// Update color when it gets pulled from the Pqueue
				graph.Verticies[graphI].Color = Colors.VDONE; // Update color
				// Mark edge as part of minimum path
				if(n.u != n.v) // First Case
					graph.Edges[graph.EdgeIndex[n.u][n.v]].Color = Colors.TREEEDGE;
				adjI = 0;
			}
		}else{
			if(adjI < graph.G[graphI].size()){ // Have more neighbors to add
				int neighbor = graph.G[graphI].get(adjI);
				addV( graphI, neighbor );
				if(graph.Edges[graph.EdgeIndex[graphI][neighbor]].Color != Colors.TREEEDGE)
					graph.Edges[graph.EdgeIndex[graphI][neighbor]].Color = Colors.BACKEDGE;
				adjI++;
			}else{ // Done adding neighbors
				itr = false;
			}
		}
	}

	public boolean isDone(){
		return done;
	}
}
