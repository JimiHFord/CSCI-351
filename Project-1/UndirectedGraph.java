import java.util.ArrayList;
import java.util.LinkedList;

import edu.rit.util.Random;


public class UndirectedGraph {

	private ArrayList<UndirectedEdge> edges;
	private ArrayList<Vertex> vertices;
	private int v;
	private Random prng;
	
	// Prevent construction
	private UndirectedGraph() {
		
	}
	
	private UndirectedGraph(int v) {
		this.v = v;
		vertices = new ArrayList<Vertex>(v);
		edges = new ArrayList<UndirectedEdge>();
		for(int i = 0; i < v; i++) {
			vertices.add(new Vertex(i));
		}
	}
	
	private int BFS(int start, int goal) {
		return BFS(vertices.get(start), vertices.get(goal));
	}
	
	private int BFS(Vertex start, Vertex goal) {
		int distance = 0;
//		procedure BFS(G,v) is
//		2      create a queue Q
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
//		3      create a set V
		boolean[] visited = new boolean[v];
//		4      add v to V
		visited[start.getN()] = true;
		Vertex current, t2;
//		5      enqueue v onto Q
		queue.addLast(start);
//		6      while Q is not empty loop
		while(!queue.isEmpty()) {
//		7         t <- Q.dequeue()
			current = queue.removeFirst();
//		8         if t is what we are looking for then
			if(current.equals(goal)) {
//		9            return t
				return distance;
//		10        end if
			}
//		11        for all edges e in G.adjacentEdges(t) loop
			for(int i = 0; i < current.edgeCount(); i++) {
//		12           u <- G.adjacentVertex(t,e)
				t2 = current.getEdges().get(i).other(current);
//		13           if u is not in V then
				if(!visited[t2.getN()]) {
//		14               add u to V
					visited[t2.getN()] = true;
//		15          enqueue u onto Q
					queue.add(t2);
//		16           end if
				}
//		17        end loop
			}
//		18     end loop
			distance++;
		}
		return 0;
	}
	
	public double averageDistance() {
		double sum = 0;
		long count = 0;
		for(int i = 0; i < v; i++) {
			for(int j = i + 1; j < v; j++) {
				sum += BFS(i, j);
				count++;
			}
		}
		return  count == 0 ? 0 : sum / count;
	}
		
	public static UndirectedGraph randomGraph(Random prng, int v, double p) {
		UndirectedGraph g = new UndirectedGraph(v);
		g.prng = prng;
		// loop 
		UndirectedEdge edge;
		Vertex a, b;
		int edgeCount = 0;
		for (int i = 0; i < v; i++) {
			for (int j = i + 1; j < v; j++) {
				// connect edges
				// always order it `i` then `j`
				if(prng.nextDouble() <= p) {
					a = g.vertices.get(i);
					b = g.vertices.get(j);
					edge = new UndirectedEdge(edgeCount++, a, b);
					g.edges.add(edge);
				}
			}
		}
		return g;
	}
}
