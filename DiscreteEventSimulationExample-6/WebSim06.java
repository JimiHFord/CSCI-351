import edu.rit.numeric.ListXYSeries;
import edu.rit.numeric.Series;
import edu.rit.numeric.plot.Plot;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;
import java.text.DecimalFormat;

/**
 * Class WebSim06 is the web server simulation main program, version 6.
 *
 * @author  Alan Kaminsky
 * @version 22-Apr-2015
 */
public class WebSim06
{
	private static double tproc;
	private static double rlb;
	private static double rub;
	private static double rdelta;
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
		if (args.length != 7) usage();
		tproc = Double.parseDouble (args[0]);
		rlb = Double.parseDouble (args[1]);
		rub = Double.parseDouble (args[2]);
		rdelta = Double.parseDouble (args[3]);
		qmax = Integer.parseInt (args[4]);
		nreq = Integer.parseInt (args[5]);
		seed = Long.parseLong (args[6]);

		// Set up pseudorandom number generator.
		prng = new Random (seed);

		// Set up plot data series.
		ListXYSeries respTimeSeries = new ListXYSeries();
		ListXYSeries dropFracSeries = new ListXYSeries();

		// Sweep mean request rate.
		System.out.printf ("Mean\tResp\tResp%n");
		System.out.printf ("Req\tTime\tTime\tReqs\tReqs\tDrop%n");
		System.out.printf ("Rate\tMean\tStddev\tProc'd\tDrop'd\tFrac%n");
		double rate;
		for (int i = 0; (rate = rlb + i*rdelta) <= rub; ++ i)
		{
			// Set up simulation.
			sim = new Simulation();

			// Set up one server.
			server = new Server04 (sim, tproc, qmax, prng);
			server.transcript = false;

			// Set up request generator and generate first request.
			generator = new Generator (sim, 1.0/rate, nreq, prng, server);

			// Run the simulation.
			sim.run();

			// Print results.
			Series.Stats stats = generator.responseTimeStats();
			int nproc = generator.responseTimeSeries().length();
			int ndrop = nreq - nproc;
			double dropfrac = (double)ndrop/(double)nreq;
			System.out.printf ("%.3f\t%.3f\t%.3f\t%d\t%d\t%.3f%n",
					rate, stats.mean, stats.stddev, nproc, ndrop, dropfrac);

			// Record results for plot.
			respTimeSeries.add (rate, stats.mean);
			dropFracSeries.add (rate, dropfrac);
		}

		// Display plots.
		new Plot()
		.plotTitle ("Response Time")
		.xAxisTitle ("Mean arrival rate (req/sec)")
		.yAxisTitle ("Mean response time (sec)")
		.seriesDots (null)
		.xySeries (respTimeSeries)
		.getFrame()
		.setVisible (true);
		new Plot()
		.plotTitle ("Drop Fraction")
		.xAxisTitle ("Mean arrival rate (req/sec)")
		.yAxisTitle ("Drop fraction")
		.yAxisStart (0.0)
		.yAxisEnd (1.0)
		.yAxisTickFormat (new DecimalFormat ("0.0"))
		.seriesDots (null)
		.xySeries (dropFracSeries)
		.getFrame()
		.setVisible (true);
	}

	/**
	 * Print a usage message and exit.
	 */
	private static void usage()
	{
		System.err.println ("Usage: java WebSim06 <tproc> <rlb> <rub> <rdelta> <qmax> <nreq> <seed>");
		System.err.println ("<tproc> = Mean request processing time");
		System.err.println ("<rlb> = Mean request rate lower bound");
		System.err.println ("<rub> = Mean request rate upper bound");
		System.err.println ("<rdelta> = Mean request rate delta");
		System.err.println ("<qmax> = Maximum queue size");
		System.err.println ("<nreq> = Number of requests");
		System.err.println ("<seed> = Random seed");
		System.exit (1);
	}
}