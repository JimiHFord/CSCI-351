
public class Chirp {

	private static final int GRAPH_TYPE_INDEX = 0;
	private static final int NUM_VERTICES_INDEX = 1;
	
	public static void main(String[] args) {
		int numVertices = 0;
		try {
			numVertices = Integer.parseInt(args[NUM_VERTICES_INDEX]);
		} catch (NumberFormatException e) {
			
		}
//		int numVertices 
		UndirectedGraph g = UndirectedGraph.cycleGraph(3);
		printGraph(g);
	}
	
	private static void printGraph(UndirectedGraph g) {
		for(int i = 0; i < g.vertices.size(); i++) {
			Cricket v = g.vertices.get(i);
			int edgeCount = v.edgeCount();
			System.out.print(i+", ");
			for(int j = 0; j < edgeCount; j++) {
				System.out.print(v.getEdges().get(j).other(v).n+", ");
			}
			System.out.println();
		}
	}
}
