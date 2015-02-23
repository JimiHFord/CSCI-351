//******************************************************************************
//
// File:    SimulationResult.java
// Package: ---
// Unit:    Class SimulationResult
//
//******************************************************************************


/**
 * Class SimulationResult is designed to be just a data container for recording
 * the results of running <I>n</I> simulations given a number of vertices
 * <I>v</I> and an edge probability <I>p</I>.
 * 
 * @author Jimi Ford
 * @version 2-15-2015
 */
public class SimulationResult {

	/**
	 * The average distance between each pair of vertices in <I>n</I> graphs 
	 */
	public final double averageDistance;
	
	/**
	 * The edge probability used in these <I>n</I> simulations
	 */
	public final double p;
	
	/**
	 * The number of vertices in each graph
	 */
	public final int v;
	
	/**
	 * Construct a simulation result in order to store the outcome of
	 * a certain number of graphs generated with the given number of
	 * vertices and edge probability.
	 * 
	 * @param v number of vertices
	 * @param p edge probability used
	 * @param averageDistance the resulting average distance measured
	 */
	public SimulationResult(int v, double p, double averageDistance) {
		this.averageDistance = averageDistance;
		this.v = v;
		this.p = p;
	}
	
}
