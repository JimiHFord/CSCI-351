import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;
import edu.rit.pj2.vbl.DoubleVbl;
import edu.rit.pj2.vbl.IntVbl;
import edu.rit.util.Random;


public class Simulator {

	private Task ref;
	private int v;
	private int trials;
	private long seed;
	private IntVbl.Sum countConnected;
	private DoubleVbl.Mean averagePower;
	
	public Simulator(Task ref, int v, int trials, long seed) {
		this.ref = ref;
		this.v = v;
		this.trials = trials;
		this.seed = seed;
		countConnected = new IntVbl.Sum();
		averagePower = new DoubleVbl.Mean();
	}
	
	public SimulationResult simulate() {
		ref.parallelFor(0, trials - 1).exec(new Loop() {
			
			Random prng;
			DoubleVbl.Mean thrAverage;
			IntVbl.Sum thrCount;
			
			public void start() {
				prng = new Random(seed + rank());
				thrAverage = threadLocal(averagePower);
				thrCount = threadLocal(countConnected);
			}
			
			@Override
			public void run(int i) throws Exception {
				SpaceNetwork sn = new SpaceNetwork(prng, v);
				if(sn.isConnected()) {
					thrCount.item++;
				}
				sn.accumulatePower(thrAverage);
			}
		});
		return new SimulationResult(
				v,
				trials,
				countConnected.intValue(),
				averagePower.doubleValue());
	}
}
