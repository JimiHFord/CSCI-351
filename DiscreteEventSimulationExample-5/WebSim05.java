import edu.rit.numeric.Series;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

/**
 * Class WebSim05 is the web server simulation main program, version 5.
 *
 * @author  Alan Kaminsky
 * @version 22-Apr-2015
 */
public class WebSim05
{
	private static double tproc;
	private static double treq;
	private static int qmax;
	private static int nreq;
	private static long seed;

	private static Random prng;
	private static Simulation sim;
	private static Server04 server;
	private static Generator generator;

	/**
	 * Main program.
	 */
	public static void main
	(String[] args)
	{
		// Parse command line arguments.
		if (args.length != 5) usage();
		tproc = Double.parseDouble (args[0]);
		treq = Double.parseDouble (args[1]);
		qmax = Integer.parseInt (args[2]);
		nreq = Integer.parseInt (args[3]);
		seed = Long.parseLong (args[4]);

		// Set up pseudorandom number generator.
		prng = new Random (seed);

		// Set up simulation.
		sim = new Simulation();

		// Set up one server.
		server = new Server04 (sim, tproc, qmax, prng);

		// Set up request generator and generate first request.
		generator = new Generator (sim, treq, nreq, prng, server);

		// Run the simulation.
		sim.run();

		// Print the response time mean and standard deviation.
		Series.Stats stats = generator.responseTimeStats();
		System.out.printf ("Response time mean   = %.3f%n", stats.mean);
		System.out.printf ("Response time stddev = %.3f%n", stats.stddev);

		// Print the number of packets processed and dropped.
		int nproc = generator.responseTimeSeries().length();
		int ndrop = nreq - nproc;
		System.out.printf ("Requests processed   = %d%n", nproc);
		System.out.printf ("Requests dropped     = %d%n", ndrop);
		System.out.printf ("Drop fraction        = %.3f%n",
				(double)ndrop/(double)nreq);
	}

	/**
	 * Print a usage message and exit.
	 */
	private static void usage()
	{
		System.err.println ("Usage: java WebSim05 <tproc> <treq> <qmax> <nreq> <seed>");
		System.err.println ("<tproc> = Mean request processing time");
		System.err.println ("<treq> = Mean request interarrival time");
		System.err.println ("<qmax> = Maximum queue size");
		System.err.println ("<nreq> = Number of requests");
		System.err.println ("<seed> = Random seed");
		System.exit (1);
	}
}