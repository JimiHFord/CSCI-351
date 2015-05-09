//******************************************************************************
//
// File:    Packet.java
// Package: ---
// Unit:    Packet Link
//
//******************************************************************************

import edu.rit.numeric.ListSeries;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

/**
 * Class Packet provides a packet model in the web simulation. It contains the
 * logic necessary to determine the size of the packet and the amount of time
 * it would take to transmit along a link. It also reports to several instances
 * of ListSeries objects that keep track of the response time of packets based
 * on their size.
 *
 * @author  Alan Kaminsky
 * @author Jimi Ford (jhf3617)
 * @version 5-6-2015
 */
public class Packet
{
	/**
	 * size of the packet in bits
	 */
	public final int size;
	
	/**
	 * unique identifier across all other packets
	 */
	public final int id;
	
	/**
	 * true if this packet is 576 bytes, false otherwise
	 */
	public final boolean isLarge;
	
	// private data member
	
	private static int idCounter = 0;	
	private Simulation sim;
	private double startTime;
	private double finishTime;
	private ListSeries respTimeSeries;
	private ListSeries respTimeLargePackets;
	private ListSeries respTimeSmallPackets;
	
	private static final int 
		SMALL = 40 * Byte.SIZE,
		LARGE = 576 * Byte.SIZE;


	/**
	 * Construct a new packet. The packet's response time will be recorded in
	 * the ListSeries.
	 * @param prng a pseudorandom number generator
	 * @param sim the current simulation object
	 * @param series the series to keep track of response times in
	 * @param seriesLargePackets series to keep track of large packet response
	 * times in
	 * @param seriesSmallPackets series to keep track of small packet response
	 * times in
	 */
	public Packet(Random prng, Simulation sim, ListSeries series,
			ListSeries seriesLargePackets, ListSeries seriesSmallPackets) {
		this.id = ++ idCounter;
		this.sim = sim;
		this.startTime = sim.time();
		this.size = prng.nextDouble() < .5 ? SMALL : LARGE;
		this.isLarge = this.size == LARGE;
		this.respTimeSeries = series;
		this.respTimeLargePackets = seriesLargePackets;
		this.respTimeSmallPackets = seriesSmallPackets;
	}
	
	/**
	 * get the time it would take this packet to transmit along a given link
	 * 
	 * @param link the given link to transmit on
	 * @return the time in seconds it would take to transmit on the given link
	 */
	public double transmitTime(Link link) {
		if(link.infiniteBitRate) {
			return 0;
		}
		return ((double)size) / link.bitRate;
	}
	
	/**
	 * Mark this request as finished. The request's finish time is set to the
	 * current simulation time. The request's response time is recorded in the
	 * response time series.
	 */
	public void finish()
	{
		finishTime = sim.time();
		respTimeSeries.add (responseTime());
		if(isLarge) respTimeLargePackets.add(responseTime());
		else respTimeSmallPackets.add(responseTime());
	}

	/**
	 * Returns this request's response time.
	 *
	 * @return  Response time.
	 */
	public double responseTime()
	{
		return finishTime - startTime;
	}

	/**
	 * Returns a string version of this request.
	 *
	 * @return  String version.
	 */
	public String toString()
	{
		return "Packet " + id;
	}
}