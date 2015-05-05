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
 * Class Request provides a request in the web simulation.
 *
 * @author  Alan Kaminsky
 * @version 18-Apr-2014
 */
public class Packet
{
	public static final int BIT_RATE = Link.DEFAULT_BIT_RATE;
	public final int size;
	
	private static int idCounter = 0;

	private int id;
	private Simulation sim;
	private double startTime;
	private double finishTime;
	private ListSeries respTimeSeries;


	/**
	 * Construct a new request. The request's start time is set to the current
	 * simulation time. The request's response time will be recorded in the
	 * given series.
	 *
	 * @param  sim     Simulation.
	 * @param  series  Response time series.
	 */
	public Packet(Random prng, Simulation sim, ListSeries series) {
		this.id = ++ idCounter;
		this.sim = sim;
		this.startTime = sim.time();
		this.size = prng.nextDouble() < .5 ? 
				40 * Byte.SIZE : 576 * Byte.SIZE;
//		this.size = 40 * Byte.SIZE;
//		this.size = 576 * Byte.SIZE;
		this.respTimeSeries = series;
	}
	
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
		if (respTimeSeries != null) respTimeSeries.add (responseTime());
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