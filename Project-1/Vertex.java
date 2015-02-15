import java.util.ArrayList;


public class Vertex {

	private int n;
	private ArrayList<UndirectedEdge> edges = new ArrayList<UndirectedEdge>();
	
	public Vertex(int n) {
		this.n = n;
	}
	
	public int getN() {
		return this.n;
	}
	
	public int edgeCount() {
		return edges.size();
	}
	
	public ArrayList<UndirectedEdge> getEdges() {
		return this.edges;
	}
	
	public void addEdge(UndirectedEdge e) {
		this.edges.add(e);
	}
	
	@Override
	public boolean equals(Object o) {
		if( !(o instanceof Vertex)) {
			return false;
		}
		if(o == this) {
			return true;
		}
		Vertex casted = (Vertex) o;
		
		return casted.n == this.n;
	}
}
