import edu.rit.sim.Simulation;
import edu.rit.util.Random;

/**
 * Class WebSim02 is the web server simulation main program, version 2.
 *
 * @author  Alan Kaminsky
 * @version 18-Apr-2014
 */
public class WebSim02
{
	private static double tproc;
	private static long seed;
	private static Random prng;
	private static Simulation sim;
	private static Server02 server;

	/**
	 * Main program.
	 */
	public static void main
	(String[] args)
	{
		// Parse command line arguments.
		if (args.length != 2) usage();
		tproc = Double.parseDouble (args[0]);
		seed = Long.parseLong (args[1]);

		// Set up pseudorandom number generator.
		prng = new Random (seed);

		// Set up simulation.
		sim = new Simulation();

		// Set up one server.
		server = new Server02 (sim, tproc, prng);

		// Create one request and give it to the server.
		Request request = new Request (sim);
		server.add (request);

		// Run the simulation.
		sim.run();

		// Print the request's response time.
		System.out.printf ("Response time = %.3f%n", request.responseTime());
	}

	/**
	 * Print a usage message and exit.
	 */
	private static void usage()
	{
		System.err.println ("Usage: java WebSim02 <tproc> <seed>");
		System.err.println ("<tproc> = Mean request processing time");
		System.err.println ("<seed> = Random seed");
		System.exit (1);
	}
}