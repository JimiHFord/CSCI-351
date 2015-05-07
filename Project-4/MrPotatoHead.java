//******************************************************************************
//
// File:    MrPotatoHead.java
// Package: ---
// Unit:    Class MrPotatoHead
//
//******************************************************************************

import edu.rit.numeric.ListXYSeries;
import edu.rit.numeric.Series;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class MrPotatoHead is the hot potato simulation main program.
 *
 * @author  Alan Kaminsky
 * @author Jimi Ford (jhf3617)
 * @version 5-3-2015
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
	 * Main program to simulate hot-potato routing
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
		prefix = args.length == 6 ? args[5] : "potato";
		// Set up pseudorandom number generator.
		prng = new Random (seed);

		// Set up plot data series.
		ListXYSeries respTimeSeries = new ListXYSeries();
		ListXYSeries respTimeLargeSeries = new ListXYSeries();
		ListXYSeries respTimeSmallSeries = new ListXYSeries();
		ListXYSeries dropFracSeries = new ListXYSeries();
		ListXYSeries dropFracLargeSeries = new ListXYSeries();
		ListXYSeries dropFracSmallSeries = new ListXYSeries();
		
		// Sweep mean request rate.
		System.out.printf ("Mean\tResp\tResp%n");
		System.out.printf ("Pkt\tTime\tTime\tPkts\tPkts\tDrop%n");
		System.out.printf ("Rate\tMean\tStddev\tProc'd\tDrop'd\tFrac%n");
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("Mean\tResp\tResp%n"));
		builder.append(String.format("Pkt\tTime\tTime\tPkts\tPkts\tDrop%n"));
		builder.append(
				String.format("Rate\tMean\tStddev\tProc'd\tDrop'd\tFrac%n"));
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
			a = new Router(prng, sim);
			b = new Router(prng, sim);
			c = new Router(prng, sim);
			Link 
				ab = new Link(a, b), 
				ac = new Link(a, c),
				ad = new Link(a, d),
				ba = new Link(b, a),
				bc = new Link(b, c),
				bd = new Link(b, d),
				ca = new Link(c, a),
				cb = new Link(c, b),
				cd = new Link(c, d),
				da = new Link(d, a),
				db = new Link(d, b),
				dc = new Link(d, c);
			// preferred link
			a.setPrimary(ad);
			b.setPrimary(bd);
			c.setPrimary(cd);
			d.setPrimary(new Link(d, h2));
			// secondary links
			a.addSecondary(ab);
			a.addSecondary(ac);
			b.addSecondary(ba);
			b.addSecondary(bc);
			c.addSecondary(ca);
			c.addSecondary(cb);
			d.addSecondary(da);
			d.addSecondary(db);
			d.addSecondary(dc);

			// Set up request generator and generate first request.
			generator = new Generator (sim, rate, nreq, prng, h1, 
					new Link(true, h1, a));

			// Run the simulation.
			sim.run();

			// Print results.
			Series.Stats totalStats = generator.responseTimeStats();
			Series.Stats largeStats = generator.responseTimeLarge().stats();
			Series.Stats smallStats = generator.responseTimeSmall().stats();
			int nproc = generator.responseTimeSeries().length();
			int ndrop = nreq - nproc;
			double dropfrac = generator.totalDropFraction();
			System.out.printf ("%.3f\t%.3f\t%.3f\t%d\t%d\t%.3f%n",
					rate, totalStats.mean, totalStats.stddev, nproc, ndrop, 
					dropfrac);
			builder.append(String.format(
					"%.3f\t%.3f\t%.3f\t%d\t%d\t%.3f%n",
					rate, totalStats.mean, totalStats.stddev, nproc, ndrop, 
					dropfrac));
			// Record results for plot.
			respTimeSeries.add (rate, totalStats.mean);
			respTimeLargeSeries.add(rate, largeStats.mean);
			respTimeSmallSeries.add(rate, smallStats.mean);
			dropFracSeries.add (rate, dropfrac);
			dropFracLargeSeries.add(rate, generator.largePacketDropFraction());
			dropFracSmallSeries.add(rate, generator.smallPacketDropFraction());
		}

		try {
			new PlotHandler(prefix, dropFracSeries, respTimeSeries, 
					dropFracLargeSeries, respTimeLargeSeries,
					dropFracSmallSeries, respTimeSmallSeries).write();
			PrintWriter tableWriter = new PrintWriter(prefix + "-table.tsv");
			tableWriter.print(builder.toString());
			tableWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Print a usage message and exit.
	 */
	private static void usage()
	{
		System.err.println ("Usage: java MrPotatoHead <rlb> <rub> <rdelta> "
				+ "<npkt> <seed> [<file-prefix> (optional)]");
		System.err.println ("<rlb> = Mean packet rate lower bound");
		System.err.println ("<rub> = Mean packet rate upper bound");
		System.err.println ("<rdelta> = Mean packet rate delta");
		System.err.println ("<npkt> = Number of packets");
		System.err.println ("<seed> = Random seed");
		System.err.println ("<file-prefix> = optional file prefix, "
				+ "default = \"potato\"");
		System.exit (1);
	}
}