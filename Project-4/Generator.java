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
	private final int npkt;
	private Routable source;
	private Link link;
	private ListSeries respTimeSeries;
	private ListSeries respTimeLargePackets;
	private ListSeries respTimeSmallPackets;
	private int largePackets;
	private int smallPackets;

	/**
	 * Create a new request generator.
	 *
	 * @param  sim     Simulation.
	 * @param  rpkt    Packet generation mean rate.
	 * @param  npkt    Number of packets.
	 * @param  prng    Pseudorandom number generator.
	 * @param  source  First host in network sending the packets.
	 */
	public Generator (Simulation sim, double rpkt, int npkt, Random prng,
			Routable source, Link link) {
		this.sim = sim;
		this.tpktPrng = new ExponentialPrng (prng, rpkt);
		this.npkt = npkt;
		this.source = source;
		this.prng = prng;
		respTimeSeries = new ListSeries();
		respTimeLargePackets = new ListSeries();
		respTimeSmallPackets = new ListSeries();
		largePackets = 0;
		smallPackets = 0;
		this.link = link;
		generatePacket();
	}

	/**
	 * Generate the next packet.
	 */
	private void generatePacket() {
		Packet p = new Packet (prng, sim, respTimeSeries, respTimeLargePackets,
				respTimeSmallPackets);
		if(link.ready()) {
			source.startSending (p, link);
		}
		if(p.isLarge) ++largePackets;
		else ++smallPackets;
		if (totalPackets() < npkt) {
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
	 * Returns a data series containing the response time statistics of the
	 * larger packets
	 *
	 * @return  Response time series.
	 */
	public Series responseTimeLarge() {
		return respTimeLargePackets;
	}
	
	/**
	 * Returns a data series containing the response time statistics of the
	 * smaller packets.
	 *
	 * @return  Response time series.
	 */
	public Series responseTimeSmall() {
		return respTimeSmallPackets;
	}
	
	/**
	 * Returns the total number of packets currently generated
	 */
	public int totalPackets() {
		return largePackets + smallPackets;
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
	 * Returns the drop fraction of the generated packets.
	 */
	public double totalDropFraction() {
		return (double)(totalPackets() - respTimeSeries.length())
				/(double)totalPackets();
	}
	
	/**
	 * Returns the drop fraction of the large packets generated
	 */
	public double largePacketDropFraction() {
		return (double)(largePackets - respTimeLargePackets.length())
				/(double)largePackets;
	}
	
	/**
	 * Returns the drop fraction of the small packets generated
	 */
	public double smallPacketDropFraction() {
		return (double)(smallPackets - respTimeSmallPackets.length())
				/(double)smallPackets;
	}
}