
public class Simulator {

	private long seed;
	private int v, numSimulations;
	private double p;
	
	public Simulator(long seed, int numVertices, double edgeProbability, int numSimulations) {
		this. seed = seed;
		this.v = numVertices;
		this.p = edgeProbability;
		this.numSimulations = numSimulations;
	}
	
	public SimulationResult simulate() {
		this.init();
		return new SimulationResult(0);
	}
	
	private void init() {
		new UndirectedGraph(v);
	}
}
