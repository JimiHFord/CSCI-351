
public class SimulationResult {

	public final double averageDistance, p;
	public final int v;
	
	public SimulationResult(int v, double p, double averageDistance) {
		this.averageDistance = averageDistance;
		this.v = v;
		this.p = p;
	}
	
}
