
public class SimulationResult {

	public final double percentConnected;
	public final double averagePower;
	public final int v;
	public final int trials;
	
	public SimulationResult(int v, int trials, int connectedCount, 
			double averagePower) {
		this.v = v;
		this.trials = trials;
		this.percentConnected = connectedCount / (double) trials;
		this.averagePower = averagePower;
	}
}
