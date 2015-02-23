//******************************************************************************
//
// File:    UndirectedGraph.java
// Package: ---
// Unit:    Class UndirectedGraph
//
//******************************************************************************

import java.util.ArrayList;
import java.util.LinkedList;
import edu.rit.pj2.vbl.DoubleVbl;
import edu.rit.util.Random;

/**
 * 
 * 
 * @author Jimi Ford
 * @version 2-15-2015
 */
public class UndirectedGraph {

	// private data members
	private ArrayList<UndirectedEdge> edges;
	private ArrayList<Vertex> vertices;
	private int v;
		
	// Prevent construction
	private UndirectedGraph() {
		
	}
	
	/**
	 * Private constructor used internally by the static random graph
	 * method
	 * @param v the number of vertices in the graph
	 */
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
		int distance = 0, verticesToProcess = 1, uniqueNeighbors = 0;
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		boolean[] visited = new boolean[v];
		visited[start.getN()] = true;
		Vertex current, t2;
		queue.add(start);
		while(!queue.isEmpty()) {
			current = queue.removeFirst();
			if(current.equals(goal)) {
				return distance;
			}
			for(int i = 0; i < current.edgeCount(); i++) {
				t2 = current.getEdges().get(i).other(current);
				if(!visited[t2.getN()]) {
					visited[t2.getN()] = true;
					queue.add(t2);
					uniqueNeighbors++;
				}
			}
			verticesToProcess--;
			if(verticesToProcess <= 0) {
				verticesToProcess = uniqueNeighbors;
				uniqueNeighbors = 0;
				distance++;
			}
			
		}
		return 0;
	}
	
	public void accumulateDistances(DoubleVbl.Mean thrLocal) {
		for(int i = 0; i < v; i++) {
			for(int j = i + 1; j < v; j++) {
				thrLocal.accumulate(BFS(i, j));
			}
		}
	}
		
	public static UndirectedGraph randomGraph(Random prng, int v, double p) {
		UndirectedGraph g = new UndirectedGraph(v);
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
