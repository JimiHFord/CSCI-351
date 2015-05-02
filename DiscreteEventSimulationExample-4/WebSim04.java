import edu.rit.numeric.Series;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

/**
 * Class WebSim04 is the web server simulation main program, version 4.
 *
 * @author  Alan Kaminsky
 * @version 18-Apr-2014
 */
public class WebSim04
{
	private static double tproc;
	private static double treq;
	private static int nreq;
	private static long seed;

	private static Random prng;
	private static Simulation sim;
	private static Server03 server;
	private static Generator generator;

	/**
	 * Main program.
	 */
	public static void main
	(String[] args)
	{
		// Parse command line arguments.
		if (args.length != 4) usage();
		tproc = Double.parseDouble (args[0]);
		treq = Double.parseDouble (args[1]);
		nreq = Integer.parseInt (args[2]);
		seed = Long.parseLong (args[3]);

		// Set up pseudorandom number generator.
		prng = new Random (seed);

		// Set up simulation.
		sim = new Simulation();

		// Set up one server.
		server = new Server03 (sim, tproc, prng);

		// Set up request generator and generate first request.
		generator = new Generator (sim, treq, nreq, prng, server);

		// Run the simulation.
		sim.run();

		// Print the response time mean and standard deviation.
		Series.Stats stats = generator.responseTimeStats();
		System.out.printf ("Response time mean   = %.3f%n", stats.mean);
		System.out.printf ("Response time stddev = %.3f%n", stats.stddev);
	}

	/**
	 * Print a usage message and exit.
	 */
	private static void usage()
	{
		System.err.println ("Usage: java WebSim04 <tproc> <treq> <nreq> <seed>");
		System.err.println ("<tproc> = Mean request processing time");
		System.err.println ("<treq> = Mean request interarrival time");
		System.err.println ("<nreq> = Number of requests");
		System.err.println ("<seed> = Random seed");
		System.exit (1);
	}
}