import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;
import edu.rit.pj2.vbl.DoubleVbl;
import edu.rit.util.Random;


public class Simulation {

	private int v, n;
	private double p;
	private Random prng;
	private Task ref;

	// change prng to seed
	// add task reference to constructor
	public Simulation(/*Task ref, long seed,*/ Random prng, int v, double p, int n) {
		this.v = v;
		this.p = p;
		this.n = n;
		this.prng = prng;
		this.ref = ref;
	}
	
	
	
	public SimulationResult simulate() {
		double average = 0;
//		DoubleVbl.Sum average = new DoubleVbl.Sum(0);
		for(int i = 0; i < n; i++) {
			average += UndirectedGraph.randomGraph(prng, v, p).averageDistance();
		}
		
		
//		average /= n;
		// 1 have task object
		// have reference to task
//		this.ref.parallelFor(0, n - 1).exec(new Loop() {
//
//			@Override
//			public void run(int i) {
//				
//			}
//			
//		});
		// 2 parallel loop
		// 3 per thread random number generator
		// seed + rank() 
		// reduction variable
		// DoubleVbl.Sum 
		
		return new SimulationResult(v, p, average);
	}
}
