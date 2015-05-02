import edu.rit.numeric.ExponentialPrng;
import edu.rit.sim.Event;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

/**
 * Class Server02 provides the server in the web simulation. The server's
 * request processing time is exponentially distributed with a given mean. All
 * requests are added at the beginning of the simulation.
 *
 * @author  Alan Kaminsky
 * @version 22-Apr-2015
 */
public class Server02
extends Server
{
	private ExponentialPrng tprocPrng;

	/**
	 * Construct a new server. The server's request processing time is
	 * exponentially distributed with the given mean.
	 *
	 * @param  sim    Simulation.
	 * @param  tproc  Mean request processing time.
	 * @param  prng   Pseudorandom number generator.
	 */
	public Server02
	(Simulation sim,
			double tproc,
			Random prng)
	{
		super (sim);
		this.tprocPrng = new ExponentialPrng (prng, 1.0/tproc);
	}

	/**
	 * Add the given request to this server.
	 *
	 * @param  request  Request.
	 */
	public void add
	(final Request request)
	{
		System.out.printf ("%.3f %s added%n", sim.time(), request);
		System.out.printf ("%.3f %s starts processing%n", sim.time(), request);
		sim.doAfter (tprocPrng.next(), new Event()
		{
			public void perform()
			{
				System.out.printf ("%.3f %s finishes processing%n",
						sim.time(), request);
				request.finish();
			}
		});
	}
}