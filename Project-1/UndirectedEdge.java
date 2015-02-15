
public class UndirectedEdge {

	private Vertex a, b;
	private int id;
	
	public UndirectedEdge(int id, Vertex a, Vertex b) {
		this.id = id;
		// enforce that a.n is always less than b.n
		if(a.getN() < b.getN()) {
			this.a = a;
			this.b = b;
		} else if(b.getN() < a.getN()) {
			this.a = b;
			this.b = a;
		} else {
			throw new IllegalArgumentException("Cannot have self loop");
		}
		this.a.addEdge(this);
		this.b.addEdge(this);
	}
	
	public Vertex other(Vertex other) {
		return other == a ? b : a;
	}
}
