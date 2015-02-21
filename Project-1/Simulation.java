import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;
import edu.rit.pj2.vbl.DoubleVbl;
import edu.rit.util.Random;


public class Simulation {

	private int v, n;
	private double p;
	private Task ref;
	private long seed;
	private DoubleVbl.Mean average = new DoubleVbl.Mean();

	public Simulation(Task ref, long seed, int v, double p, int n) {
		this.v = v;
		this.p = p;
		this.n = n;
		this.seed = seed;
		this.ref = ref;
	}
	
	
	
	public SimulationResult simulate() {
		// run "n" simulations
		this.ref.parallelFor(0, n - 1).exec(new Loop() {
			Random prng;
			DoubleVbl.Mean thrAverage;
			
			@Override
			public void start() {
				prng = new Random(seed + rank());
				thrAverage = threadLocal(average);
			}
			@Override
			public void run(int i) {
				UndirectedGraph.randomGraph(prng, v, p).accumulateDistances(thrAverage);
			}
			
		});
		
		return new SimulationResult(v, p, average.doubleValue());
	}
}
