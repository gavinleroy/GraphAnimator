package DynamicGraph;

import java.util.*;

public class KruskalsAlgorithm implements Algorithm{

	private Graph graph;
	private boolean done;
	private boolean used[];
	private int ind; // Index of the sorted edges array
	private Map<Point, Point> parent;

	public KruskalsAlgorithm(Graph g){
		parent = new HashMap<Point, Point>();
		used = new boolean[g.E];
		graph = g;
		ind = 0;
		// Sort edges
	}

	Point find(Point p){
		Point pp = parent.get(p);
		if(pp == null) return p;
		else{
			pp = find(pp);
			parent.put(p, pp);
			return pp;
		}
	}

	private boolean join(Point p1, Point p2){
		Point pp1 = find(p1);
		Point pp2 = find(p2);
		if(pp1.equals(pp2)) return false;
		parent.put(p1, pp2);
		return true;
	}

	private boolean focus(int u, int v){
		//TODO: implement
		// Should focus both U and V along with its edge
		return false;
	}

	private int findSmallest(){
		int minI = -1, min = Integer.MAX_VALUE;
		for(int i = 0; i < graph.Edges.length; i++){
			if(graph.Edges[i].Weight < min && !used[i]){
				minI = i;
				min = graph.Edges[i].Weight;
			}
		}
		return minI;
	}

	public void step(){
		// 1) Pick next smallest edge
		int i = findSmallest();
		if(i == -1){ 
			done = true;
			return;
		}

		Edge e = graph.Edges[i];
		e.Color = Colors.EHIGHLIGHTED;
		// 2) If it doesn't form a cycle then highlight it
		if(join(e.Begin, e.End)){
			// Focus Edge
			e.Color = Colors.EDONE;
			// Join the verticies 
		}else{
			e.Color = Colors.EFOCUSED;
		}
		used[i] = true; // Say that it is used
	}

	public boolean isDone(){
		return done;
	}
}
