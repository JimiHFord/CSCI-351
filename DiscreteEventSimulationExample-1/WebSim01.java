import edu.rit.sim.Simulation;

/**
 * Class WebSim01 is the web server simulation main program, version 1.
 *
 * @author  Alan Kaminsky
 * @version 18-Apr-2014
 */
public class WebSim01
{
	private static double tproc;
	private static Simulation sim;
	private static Server01 server;

	/**
	 * Main program.
	 */
	public static void main
	(String[] args)
	{
		// Parse command line arguments.
		if (args.length != 1) usage();
		tproc = Double.parseDouble (args[0]);

		// Set up simulation.
		sim = new Simulation();

		// Set up server.
		server = new Server01 (sim, tproc);

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
		System.err.println ("Usage: java WebSim01 <tproc>");
		System.err.println ("<tproc> = Request processing time");
		System.exit (1);
	}
}