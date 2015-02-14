import java.util.ArrayList;


public class UndirectedGraph {

	private ArrayList<UndirectedEdge> edges;
	private ArrayList<Vertex> vertices;
	
	public UndirectedGraph(int n) {
		vertices = new ArrayList<Vertex>(n);
		for(int i = 0; i < n; i++) {
			vertices.add(new Vertex(i));
		}
	}
}
