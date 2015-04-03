import edu.rit.util.Random;

//******************************************************************************
//
// File:    SpaceNetwork.java
// Package: ---
// Unit:    Class SpaceNetwork
//
//******************************************************************************

/**
 * 
 * @author Jimi Ford (jhf3617)
 * @version 4-2-2015
 */
public class SpaceNetwork {

	/**
	 * number of space stations
	 */
	public final int n;
	
	private double[][] adj;
	private SpaceStation[] stations;
	
	
	public SpaceNetwork(Random prng, int n) {
		this.n = n;
		this.adj = new double[n][n];
		this.stations = new SpaceStation[n];
		initStations(prng);
	}
	
	private void initStations(Random prng) {
		double x, y, z;
		for(int i = 0; i < n; i++) {
			x = prng.nextDouble() * SpaceStation.MAX_DIM;
			y = prng.nextDouble() * SpaceStation.MAX_DIM;
			z = prng.nextDouble() * SpaceStation.MAX_DIM;
			stations[i] = new SpaceStation(i, x, y, z);
		}
	}
}
