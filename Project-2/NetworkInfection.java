
import edu.rit.image.ByteImageQueue;
import edu.rit.image.IndexPngWriter;
import edu.rit.numeric.plot.Plot;
import edu.rit.util.Instance;
import edu.rit.util.Random;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

/**
 * Class NetworkInfection is a main program that simulates a network infection
 * process.
 * <P>
 * Usage: <TT>java NetworkInfection "<I>ctor</I>" <I>seed</I> <I>infprob</I>
 * <I>inftime</I> <I>rectime</I> <I>dieprob</I> <I>steps</I> <I>imagefile</I>
 * <I>plotfile</I></TT>
 * <BR><TT><I>ctor</I></TT> = Graph factory constructor expression
 * <BR><TT><I>seed</I></TT> = Random seed
 * <BR><TT><I>infprob</I></TT> = Infection probability, 0.0 &le; <I>infprob</I>
 * &le; 1.0
 * <BR><TT><I>inftime</I></TT> = Infection time (steps), <I>inftime</I> &ge; 1
 * <BR><TT><I>rectime</I></TT> = Recovery time (steps), <I>rectime</I> &ge; 1
 * <BR><TT><I>dieprob</I></TT> = Death probability, 0.0 &le; <I>dieprob</I>
 * &le; 1.0
 * <BR><TT><I>steps</I></TT> = Number of time steps, <I>steps</I> &ge; 1
 * <BR><TT><I>imagefile</I></TT> = Output image file name
 * <BR><TT><I>plotfile</I></TT> = Output plot file name
 * <P>
 * The program uses the given graph factory to create a graph. The program
 * simulates a network infection on the graph with the given parameters. The
 * output image consists of <I>steps</I> + 1 rows and <I>V</I> columns of
 * pixels, where <I>V</I> is the number of vertices. Each pixel shows the state
 * of a particular vertex at a particular time step: susceptible = yellow,
 * infected = red, recovered = green, dead = black. The output image is stored
 * in a PNG file. The output plot plots infection rate (fraction of infected
 * vertices) versus time step. The output plot is stored in a PJ2 drawing file.
 *
 * @author  Alan Kaminsky
 * @version 28-Feb-2015
 */
public class NetworkInfection
{
	// Main program.
	public static void main
	(String[] args)
			throws Exception
	{
		// Parse command line arguments.
		if (args.length != 9) usage();
		String ctor = args[0];
		long seed = Long.parseLong (args[1]);
		double infprob = Double.parseDouble (args[2]);
		if (0 > infprob || infprob > 1.0) usage();
		int inftime = Integer.parseInt (args[3]);
		if (inftime < 1) usage();
		int rectime = Integer.parseInt (args[4]);
		if (rectime < 1) usage();
		double dieprob = Double.parseDouble (args[5]);
		if (0 > dieprob || dieprob > 1.0) usage();
		int steps = Integer.parseInt (args[6]);
		if (steps < 1) usage();
		File imagefile = new File (args[7]);
		File plotfile = new File (args[8]);
		System.out.printf ("java NetworkInfection \"%s\"", args[0]);
		for (int i = 1; i < args.length; ++ i)
			System.out.printf (" %s", args[i]);
		System.out.println();
		System.out.flush();

		// Create graph.
		GraphFactory factory = (GraphFactory) Instance.newInstance (ctor);
		Random prng = new Random (seed);
		factory.setPrng (prng);
		Graph g = factory.manufacture (null);

		// Set up image writer and image queue.
		OutputStream imageout =
				new BufferedOutputStream (new FileOutputStream (imagefile));
		IndexPngWriter imageWriter = new IndexPngWriter
				(steps + 1, g.vcount(), imageout, InfectionState.palette());
		ByteImageQueue imageQueue = imageWriter.getImageQueue();

		// Set up vertex states.
		InfectionState state = new InfectionState
				(g, infprob, inftime, rectime, dieprob, prng);

		// Set up infection rate plot data.
		double[] stepdata = new double [steps + 1];
		for (int s = 0; s <= steps; ++ s)
			stepdata[s] = s;
		double[] irdata = new double [steps + 1];

		// Initially vertex 0 is infected, others are susceptible.
		state.setState (0, InfectionState.INFECTED);
		irdata[0] = state.infectionRate();
		state.putStateImage (imageQueue);

		// Do time steps.
		for (int s = 1; s <= steps; ++ s)
		{
			irdata[s] = state.step();
			state.putStateImage (imageQueue);
		}

		// Write image.
		imageWriter.write();
		imageout.close();

		// Print infection rate data.
		System.out.printf ("Step\tInf Rate%n");
		for (int s = 0; s <= steps; ++ s)
			System.out.printf ("%d\t%.5f%n", s, irdata[s]);

		// Write plot.
		Plot.write
		(new Plot()
		.plotTitle (String.format ("Net Inf %s", ctor))
		.xAxisTitle ("Step")
		.xAxisLength (288)
		.yAxisTitle ("Infection Rate")
		.yAxisLength (178)
		.yAxisStart (0.0)
		.yAxisEnd (1.0)
		.yAxisTickFormat (new DecimalFormat ("0.0"))
		.seriesDots (null)
		.xySeries (stepdata, irdata),
		plotfile);
	}

	// Print a usage message and exit.
	private static void usage()
	{
		System.err.println ("Usage: java NetworkInfection \"<ctor>\" <seed> <infprob> <inftime> <rectime> <dieprob> <steps> <imagefile> <plotfile>");
		System.err.println ("<ctor> = Graph factory constructor expression");
		System.err.println ("<seed> = Random seed");
		System.err.println ("<infprob> = Infection probability, 0.0 <= infprob <= 1.0");
		System.err.println ("<inftime> = Infection time (steps), inftime >= 1");
		System.err.println ("<rectime> = Recovery time (steps), rectime >= 1");
		System.err.println ("<dieprob> = Death probability, 0.0 <= dieprob <= 1.0");
		System.err.println ("<steps> = Number of time steps, steps >= 1");
		System.err.println ("<imagefile> = Output image file name");
		System.err.println ("<plotfile> = Output plot file name");
		System.exit (1);
	}
}