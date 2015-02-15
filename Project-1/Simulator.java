import edu.rit.pj2.Loop;
import edu.rit.pj2.vbl.DoubleVbl;
import edu.rit.util.Random;

public class Simulator extends Loop {

	private long seed;
	private int numVertices, numSimulations;
	private double p;
	private Random prng;
	private DoubleVbl average;
	private DoubleVbl localAverage;
	
	
	public Simulator(long seed, int vertices, double p, int numSimulations, DoubleVbl average) {
		this.seed = seed;
		this.p = p;
		this.numVertices = vertices;
		this.numSimulations = numSimulations;
		this.average = average;
	}
	
	@Override
	public void start() {
		seed += rank();
		prng = new Random(seed);
		localAverage = threadLocal(average);
	}
	
	@Override
	public void run(int i) throws Exception {
		// TODO Auto-generated method stub
		double r = prng.nextDouble();
//		System.out.print(seed + p + i + r + "\t");
//		System.out.println(seed + ", " + p + ", " + i + ", " + r);
		localAverage.item += r;
//		System.out.println(r);
		
	}
	
	@Override
	public void finish() {
		System.out.println(average.item++ + " " + localAverage.item++);
	}
}
