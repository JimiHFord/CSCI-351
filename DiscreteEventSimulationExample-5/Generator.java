import edu.rit.numeric.ExponentialPrng;
import edu.rit.numeric.ListSeries;
import edu.rit.numeric.Series;
import edu.rit.sim.Event;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

/**
 * Class Generator generates requests for the web server simulations.
 *
 * @author  Alan Kaminsky
 * @version 18-Apr-2014
 */
public class Generator
{
	private Simulation sim;
	private ExponentialPrng treqPrng;
	private int nreq;
	private Server server;

	private ListSeries respTimeSeries;
	private int n;

	/**
	 * Create a new request generator.
	 *
	 * @param  sim     Simulation.
	 * @param  treq    Request mean interarrival time.
	 * @param  nreq    Number of requests.
	 * @param  prng    Pseudorandom number generator.
	 * @param  server  Server.
	 */
	public Generator
	(Simulation sim,
			double treq,
			int nreq,
			Random prng,
			Server server)
	{
		this.sim = sim;
		this.treqPrng = new ExponentialPrng (prng, 1.0/treq);
		this.nreq = nreq;
		this.server = server;

		respTimeSeries = new ListSeries();
		n = 0;

		generateRequest();
	}

	/**
	 * Generate the next request.
	 */
	private void generateRequest()
	{
		server.add (new Request (sim, respTimeSeries));
		++ n;
		if (n < nreq)
		{
			sim.doAfter (treqPrng.next(), new Event()
			{
				public void perform()
				{
					generateRequest();
				}
			});
		}
	}

	/**
	 * Returns a data series containing the response time statistics of the
	 * generated requests.
	 *
	 * @return  Response time series.
	 */
	public Series responseTimeSeries()
	{
		return respTimeSeries;
	}

	/**
	 * Returns the response time statistics of the generated requests.
	 *
	 * @return  Response time statistics (mean, standard deviation, variance).
	 */
	public Series.Stats responseTimeStats()
	{
		return respTimeSeries.stats();
	}

	/**
	 * Returns the drop fraction of the generated requests.
	 */
	public double dropFraction()
	{
		return (double)(nreq - respTimeSeries.length())/(double)nreq;
	}
}