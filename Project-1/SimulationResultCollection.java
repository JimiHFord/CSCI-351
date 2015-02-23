//******************************************************************************
//
// File:    SimulationResultCollection.java
// Package: ---
// Unit:    Class SimulationResultCollection
//
//******************************************************************************

/**
 * Class SimulationResultcollection keeps track of the average distance measured
 * for each pair of edge probability values and number of vertices. It also
 * contains the necessary computation to account for using integers as 
 * probabilities, treating them as floating point values ranging from 0 to 1. 
 * 
 * @author Jimi Ford
 * @version 2-15-2015
 */
public class SimulationResultCollection {

	// private data members
	private double[][] averages;
	private int rows, cols;
	
	/**
	 * The lower bound on number of vertices
	 */
	public final int vMin;
	
	/**
	 * The upper bound on number of vertices
	 */
	public final int vMax;
	
	/**
	 * The amount to increment the number of vertices by in each set of trials
	 */
	public final int vInc;
	
	/**
	 * The scaled lower bound on edge probability
	 */
	public final int pMin;
	
	/**
	 * The scaled upper bound on edge probability
	 */
	public final int pMax;
	
	/**
	 * The amount to increment the edge probability by in each set of trials
	 */
	public final int pInc;
	
	/**
	 * The number of decimal places necessary to convert the edge probability
	 * into an integer. This is in order to combat floating point arithmetic.
	 * One can't just loop from 0 to 1 incrementing by .1 because compounding
	 * error is accumulated on each increment. Integers play nicely when
	 * incremented.
	 */
	public final int pExp;

	/**
	 * Construct a simulation result collection. The parameter values
	 * should reflect the values passed into the program through the 
	 * command line arguments.
	 * 
	 * @param vMin The lower bound on number of vertices
	 * @param vMax The upper bound on number of vertices
	 * @param vInc The amount to increment the number of vertices by in
	 * 			   each set of trials
	 * @param pMin The scaled lower bound on edge probability
	 * @param pMax The scaled upper bound on edge probability
	 * @param pInc The scaled amount to increment the edge probability by 
	 * 			   in each set of trials
	 * @param pExp The number of decimal places used to convert the edge
	 * 			   probability into an integer
	 */
	public SimulationResultCollection (int vMin, int vMax, int vInc, 
			int pMin, int pMax, int pInc, int pExp) {
		this.vMin = vMin;
		this.vMax = vMax;
		this.vInc = vInc;
		this.pMin = pMin;
		this.pMax = pMax;
		this.pInc = pInc;
		this.pExp = pExp;
		this.rows = (vMax - vMin + vInc) / vInc;
		this.cols = (pMax - pMin + pInc) / pInc;
		this.averages = new double[rows][cols];
	}
	
	/**
	 * Add a simulation result to the data matrix. 
	 * 
	 * @param result the simulation result to record
	 */
	public void add(SimulationResult result) {
		int p = p(result.p);
		int col = col(p);
		int row = row(result.v);
		averages[row][col] = result.averageDistance;
		
	}
	
	/**
	 * Get the average distance recorded for a given vertex count
	 * and a scaled edge probability
	 * 
	 * @param v the vertex count
	 * @param p the scaled edge probability
	 * @return the average distance recorded for this pair
	 */
	public double get(int v, int p) {
		int row = row(v);
		int col = col(p);
		return averages[row][col];
	}
	
	/**
	 * Get an array of averages for varying edge probabilities and
	 * a given vertex count.
	 * 
	 * @param v the vertex count of interest
	 * @return the array of averages for this vertex count
	 */
	public double[] getAveragesForV(int v) {
		return averages[row(v)].clone();
	}
	
	/**
	 * Convert a vertex value into its associated row value in the
	 * data matrix.
	 * 
	 * @param v the vertex count (or number of vertices) to convert
	 * @return the associated row value in the data matrix
	 */
	private int row(int v) {
		return (v - vMin) / vInc;
	}
	
	/**
	 * Convert an edge probability into a scaled integer in order
	 * to get rid of floating point arithmetic errors.
	 * 
	 * @param p the edge probability to convert
	 * @return the scaled integer ranging from pMin to pMax
	 */
	private int p(double p) {
		return (int) (Math.round(p * pExp));
	}
	
	/**
	 * Convert a scaled edge probability into the associated
	 * column value in the data matrix.
	 * 
	 * @param p the scaled edge probability to convert
	 * @return the associated column value in the data matrix
	 */
	private int col(int p) {
		return (p - pMin) / pInc;
	}
	
}
