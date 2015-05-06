//******************************************************************************
//
// File:    Generator.java
// Package: ---
// Unit:    Class Generator
//
//******************************************************************************

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
 * @author Jimi Ford (jhf3617)
 * @version 5-2-2015
 */
public class Generator
{
	// private data members
	
	private Simulation sim;
	private ExponentialPrng tpktPrng;
	private Random prng;
	private int npkt;
	private Routable source;
	private Link link;
	private ListSeries respTimeSeries;
	private int n;

	/**
	 * Create a new request generator.
	 *
	 * @param  sim     Simulation.
	 * @param  tpkt    Request mean interarrival time.
	 * @param  npkt    Number of requests.
	 * @param  prng    Pseudorandom number generator.
	 * @param  source  First host in network sending the packets.
	 */
	public Generator (Simulation sim, double tpkt, int npkt, Random prng,
			Routable source, Link link) {
		this.sim = sim;
		this.tpktPrng = new ExponentialPrng (prng, 1.0/tpkt);
		this.npkt = npkt;
		this.source = source;
		this.prng = prng;
		respTimeSeries = new ListSeries();
		n = 0;
		this.link = link;
		generatePacket();
	}

	/**
	 * Generate the next packet.
	 */
	private void generatePacket() {
		if(link.ready()) {
			source.startSending (new Packet (prng, sim, respTimeSeries), link);
		}
		++ n;
		if (n < npkt) {
			sim.doAfter (tpktPrng.next(), new Event() {
				public void perform() {
					generatePacket();
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
	public Series responseTimeSeries() {
		return respTimeSeries;
	}

	/**
	 * Returns the response time statistics of the generated requests.
	 *
	 * @return  Response time statistics (mean, standard deviation, variance).
	 */
	public Series.Stats responseTimeStats() {
		return respTimeSeries.stats();
	}

	/**
	 * Returns the drop fraction of the generated requests.
	 */
	public double dropFraction() {
		return (double)(npkt - respTimeSeries.length())/(double)npkt;
	}
}