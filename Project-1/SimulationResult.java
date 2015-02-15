
public class SimulationResult {

	private double averageDistance, p;
	private int v;
	
	public SimulationResult(int v, double p, double averageDistance) {
		this.averageDistance = averageDistance;
		this.v = v;
		this.p = p;
	}
	
	public double getAverageDistance() {
		return this.averageDistance;
	}
	
	public double getP() {
		return p;
	}
	
	public int getV() {
		return v;
	}
}
