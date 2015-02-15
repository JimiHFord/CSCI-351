import edu.rit.util.Random;


public class Simulation {

	private int v, n;
	private double p;
	private Random prng;
	

	public Simulation(Random prng, int v, double p, int n) {
		this.v = v;
		this.p = p;
		this.n = n;
		this.prng = prng;
	}
	
	
	
	public SimulationResult simulate() {
		double average = 0;
		for(int i = 0; i < n; i++) {
//			average += prng.nextInt(Integer.MAX_VALUE);
			if(prng.nextDouble() <= p) {
				average += prng.nextInt(v);
			}
		}
		average /= n;
		return new SimulationResult(v, p, average);
	}
}
