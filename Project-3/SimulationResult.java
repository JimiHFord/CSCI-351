//******************************************************************************
//
// File:    SimulationResult.java
// Package: ---
// Unit:    Class SimulationResult
//
//******************************************************************************

/**
 * Class SimulationResult is designed to be just a data container for recording
 * the results of running <I>n</I> simulations given a number of space stations
 * 
 * @author Jimi Ford (jhf3617)
 * @version 4-4-2015
 */
public class SimulationResult {

	/**
	 * the percentage of connected networks generated
	 */
	public final double percentConnected;
	
	/**
	 * the average power needed by the stations to transmit to any other station
	 * in each network
	 */
	public final double averagePower;
	
	/**
	 * the number of vertices (i.e. nodes or space stations)
	 */
	public final int v;
	
	/**
	 * the number of trials that were run to generate this result
	 */
	public final int trials;
	
	/**
	 * Construct a SimulationResult
	 * @param v number of space stations
	 * @param trials number of trials that were run
	 * @param connectedCount the number of connected networks produced
	 * @param averagePower the average power needed by each space station to
	 * 		transmit messages to any other space station in the network
	 */
	public SimulationResult(int v, int trials, int connectedCount, 
			double averagePower) {
		this.v = v;
		this.trials = trials;
		this.percentConnected = connectedCount / (double) trials;
		this.averagePower = averagePower;
	}
}
