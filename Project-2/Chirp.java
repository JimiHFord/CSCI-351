
public class Chirp {

	
	public static void main(String[] args) {
		
		UndirectedGraph g = UndirectedGraph.cycleGraph(3);
		printGraph(g);
	}
	
	private static void printGraph(UndirectedGraph g) {
		for(int i = 0; i < 3; i++) {
			Vertex v = g.vertices.get(i);
			int edgeCount = v.edgeCount();
			System.out.print(i+", ");
			for(int j = 0; j < edgeCount; j++) {
				System.out.print(v.getEdges().get(j).other(v).n+", ");
			}
			System.out.println();
		}
	}
}
