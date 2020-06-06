package DynamicGraph;

import java.util.Stack;

// TODO: modify the DFS algorithm to display a topological sort.
// this should be done by counting the post order and chaning the 
// location on the screen. To think about... should the starting graph be smaller?
// should the modified graph overlap the existing one? should we keep both
// to view at the same time? 
class TopoSortAlgorithm implements Algorithm {
	protected class Node{
		public int v;
		public int i;
		public Node(int x, int y){
			v = x;
			i = y;
		}
	}

	private Graph graph;
	private boolean done;
	private Stack<Node> stack;

	public TopoSortAlgorithm(Graph g, int s){
		graph = g;
		done = false;
		stack = new Stack<Node>();
		graph.Verticies[s].Distance = 0;
		addV(s); // Initialize with Source Vertex
	}

	private boolean addV(int i){
		if(!graph.visited[i]){
			stack.push(new Node(i, 0));
			graph.visited[i] = true;
			graph.Verticies[i].Color = Colors.VFOCUSED;
			return true;
		}
		return false;
	}

	public void step(){
		if(done) return;

		if(stack.empty()) done = true;
		else{
			int graphI = stack.peek().v;
			int adjI = stack.peek().i;
			if(adjI < graph.G[graphI].size()){ // Have more neighbors to add
				int next = graph.G[graphI].get(adjI);
				stack.pop(); // Update the adjacent index
				stack.push(new Node(graphI, adjI + 1));
				if(addV( next )){ // Successful add it a Tree Edge
					graph.Edges[graph.EdgeIndex[graphI][next]].Color = Colors.TREEEDGE;
				}else{ // If edge was highlighted as a Tree Edge then leave it
					if(graph.Edges[graph.EdgeIndex[graphI][next]].Color != Colors.TREEEDGE)
						graph.Edges[graph.EdgeIndex[graphI][next]].Color = Colors.BACKEDGE;
				}
			}else{ // Done adding neighbors
				graph.Verticies[graphI].Color = Colors.VDONE; // Update color
				stack.pop(); // Remove vertex from stack
			}
		}
	}

	public boolean isDone(){
		return done;
	}
}
