import edu.rit.numeric.ListSeries;
import edu.rit.numeric.Series;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

/**
 * Class WebSim03 is the web server simulation main program, version 3.
 *
 * @author  Alan Kaminsky
 * @version 18-Apr-2014
 */
public class WebSim03
	{
	private static double tproc;
	private static int nreq;
	private static long seed;
	private static Random prng;
	private static Simulation sim;
	private static Server03 server;
	private static ListSeries respTimeSeries;

	/**
	 * Main program.
	 */
	public static void main
		(String[] args)
		{
		// Parse command line arguments.
		if (args.length != 3) usage();
		tproc = Double.parseDouble (args[0]);
		nreq = Integer.parseInt (args[1]);
		seed = Long.parseLong (args[2]);

		// Set up pseudorandom number generator.
		prng = new Random (seed);

		// Set up simulation.
		sim = new Simulation();

		// Set up one server.
		server = new Server03 (sim, tproc, prng);

		// Set up series for recording response times.
		respTimeSeries = new ListSeries();

		// Create qmax requests and give them to the server.
		for (int i = 0; i < nreq; ++ i)
			{
			Request request = new Request (sim, respTimeSeries);
			server.add (request);
			}

		// Run the simulation.
		sim.run();

		// Print the response time mean and standard deviation.
		Series.Stats stats = respTimeSeries.stats();
		System.out.printf ("Response time mean   = %.3f%n", stats.mean);
		System.out.printf ("Response time stddev = %.3f%n", stats.stddev);
		}

	/**
	 * Print a usage message and exit.
	 */
	private static void usage()
		{
		System.err.println ("Usage: java WebSim03 <tproc> <nreq> <seed>");
		System.err.println ("<tproc> = Mean request processing time");
		System.err.println ("<nreq> = Number of requests");
		System.err.println ("<seed> = Random seed");
		System.exit (1);
		}
	}