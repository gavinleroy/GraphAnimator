package DynamicGraph;

import java.util.Stack;

class DFSAlgorithm implements Algorithm {
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
	private boolean[] visited;

	public DFSAlgorithm(Graph g, int s){
		graph = g;
		done = false;
		stack = new Stack<Node>();
		visited = new boolean[g.V];
		graph.Verticies[s].Distance = 0;
		addV(s); // Initialize with Source Vertex
	}

	private boolean addV(int i){
		if(!visited[i]){
			stack.push(new Node(i, 0));
			visited[i] = true;
			graph.Verticies[i].Color = Colors.VFOCUSED;
			return true;
		}
		return false;
	}

	public void step(){
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
