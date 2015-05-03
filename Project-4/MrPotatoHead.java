import edu.rit.numeric.ListXYSeries;
import edu.rit.numeric.Series;
import edu.rit.numeric.plot.Plot;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Class WebSim06 is the web server simulation main program, version 6.
 *
 * @author  Alan Kaminsky
 * @author Jimi Ford (jhf3617)
 * @version 22-Apr-2015
 */
public class MrPotatoHead
{
	private static double rlb;
	private static double rub;
	private static double rdelta;
	private static int nreq;
	private static long seed;

	private static Random prng;
	private static Simulation sim;
	private static String prefix;
	private static Generator generator;

	/**
	 * Main program.
	 */
	public static void main(String[] args)
	{
		// Parse command line arguments.
		if (args.length != 5 && args.length != 6) usage();
		rlb = Double.parseDouble (args[0]);
		rub = Double.parseDouble (args[1]);
		rdelta = Double.parseDouble (args[2]);
		if(rlb <= 0) rlb = rdelta;
		nreq = Integer.parseInt (args[3]);
		seed = Long.parseLong (args[4]);
		prefix = args.length == 6 ? args[5] : "potato-";
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
			Host h1, h2;
			Router a, b, c, d;
			
			h1 = new Host(sim);
			h2 = new Host(sim);
			d = new Router(prng, sim);
			d.setPrimary(new Link(d, h2));
			a = new Router(prng, sim);
			b = new Router(prng, sim);
			c = new Router(prng, sim);
			Link 
				ab = new Link(a, b), 
				ac = new Link(a, c),
				ad = new Link(a, d),
				bc = new Link(b, c),
				bd = new Link(b, d),
				cd = new Link(c, d);
			// preferred link
			a.setPrimary(ad);
			b.setPrimary(bd);
			c.setPrimary(cd);
			
			// secondary links
			a.addSecondary(ab);
			a.addSecondary(ac);
			b.addSecondary(ab);
			b.addSecondary(bc);
			c.addSecondary(ac);
			c.addSecondary(bc);
			d.addSecondary(ad);
			d.addSecondary(bd);
			d.addSecondary(cd);
			// Set up one server.
//			server = new Host (sim);

			// Set up request generator and generate first request.
			generator = new Generator (sim, 1.0/rate, nreq, prng, h1, 
					new Link(h1, a));

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
		Plot responseTime = new Plot()
		.plotTitle ("Response Time")
		.xAxisTitle ("Mean arrival rate (req/sec)")
		.yAxisTitle ("Mean response time (sec)")
		.yAxisTickFormat (new DecimalFormat ("0.0"))
		.seriesDots (null)
		.xySeries (respTimeSeries);
		Plot dropFraction = new Plot()
		.plotTitle ("Drop Fraction")
		.xAxisTitle ("Mean arrival rate (req/sec)")
		.yAxisTitle ("Drop fraction")
		.yAxisStart (0.0)
		.yAxisEnd (1.0)
		.yAxisTickFormat (new DecimalFormat ("0.0"))
		.seriesDots (null)
		.xySeries (dropFracSeries);
		try {
			Plot.write(responseTime, new File(prefix + "response-time.dwg"));
			Plot.write(dropFraction, new File(prefix + "drop-fraction.dwg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dropFraction
		.getFrame()
		.setVisible (true);
		responseTime.getFrame()
		.setVisible (true);
	}

	/**
	 * Print a usage message and exit.
	 */
	private static void usage()
	{
		System.err.println ("Usage: java MrPotatoHead <rlb> <rub> <rdelta> "
				+ "<nreq> <seed>");
		System.err.println ("<rlb> = Mean request rate lower bound");
		System.err.println ("<rub> = Mean request rate upper bound");
		System.err.println ("<rdelta> = Mean request rate delta");
		System.err.println ("<nreq> = Number of requests");
		System.err.println ("<seed> = Random seed");
		System.exit (1);
	}
}