//******************************************************************************
//
// File:    SpaceNetwork.java
// Package: ---
// Unit:    Class SpaceNetwork
//
//******************************************************************************

import edu.rit.pj2.vbl.DoubleVbl;
import edu.rit.util.Random;


/**
 * Class models a network of space stations placed in random positions in 3D
 * space. The space stations' locations are limited to 
 * 1E8 million kilometers X 1E8 million kilometers X 1E8 million kilometers.
 * 
 * @author Jimi Ford (jhf3617)
 * @version 4-2-2015
 */
public class SpaceNetwork {

	/**
	 * maximum dimension value allowed in 3D space
	 */
	public static final double MAX_DIM = 1.0E8;
	
	/**
	 * number of space stations
	 */
	public final int n;
	
	// private data members
	private boolean connected;
	private double[][] adj;
	private double[][] shortest;
	private SpaceStation[] stations;
	
	/**
	 * Construct a SpaceNetwork
	 * 
	 * @param prng the pseudorandom number generator to use
	 * @param n the number of space stations in this network
	 */
	public SpaceNetwork(Random prng, final int n) {
		this.n = n;
		this.adj = new double[n][n];
		this.shortest = new double[n][n];
		this.stations = new SpaceStation[n];
		initStations(prng);
		initAdjacency();
		floydWarshall();
		checkConnectivity();
	}
	
	/**
	 * initialize the coordinates of the <TT>n</TT> stations
	 * @param prng the pseudorandom number generator to get random numbers from
	 */
	private void initStations(Random prng) {
		double x, y, z;
		for(int i = 0; i < n; i++) {
			x = prng.nextDouble() * MAX_DIM;
			y = prng.nextDouble() * MAX_DIM;
			z = prng.nextDouble() * MAX_DIM;
			stations[i] = new SpaceStation(i, x, y, z);
		}
	}
	
	/**
	 * initialize the weights of the edges between nodes with the power needed
	 * to transmit from one station to another
	 */
	private void initAdjacency() {
		SpaceStation s1, s2;
		double distance, power;
		for(int i = 0; i < n; i++) {
			adj[i][i] = 0; // not needed
			s1 = get(i);
			for(int j = i+1; j < n; j++) {
				s2 = get(j);
				distance = s1.distance(s2);
				if(distance > SpaceStation.MAX_DISTANCE) {
					power = Double.POSITIVE_INFINITY;
				} else {
					power = s1.powerNeeded(s2);
				}
				adj[i][j] = power;
				adj[j][i] = power;
			}
		}
	}

	/**
	 * Run Floyd-Warshall on the space network to determine all-pairs shortest
	 * paths. This will tell us the least amount of power a station needs to 
	 * transmit to any other station in the network by forwarding the message
	 * along the shortest path to that station.
	 */
	private void floydWarshall() {
		System.arraycopy(adj, 0, shortest, 0, n);
		double s_i_j, s_i_k, s_k_j;
		for(int k = 0; k < n; k++) {
			for(int i = 0; i < n; i++) {
				for(int j = 0; j < n; j++) {
					s_i_j = shortest[i][j];
					s_i_k = shortest[i][k];
					s_k_j = shortest[k][j];
					if(s_i_j > s_i_k + s_k_j) {
						shortest[i][j] = s_i_k + s_k_j;
					}
				}
			}
		}		
	}
	
	/**
	 * Check if the network is connected
	 */
	private void checkConnectivity() {
		boolean connected = true;
		double temp;
		for(int i = 0; i < n && connected; i++) {
			for(int j = 0; j < n && connected; j++) {
				temp = shortest[i][j];
				connected = !Double.isInfinite(temp);
			}
		}
		this.connected = connected;
	}
	
	/**
	 * get whether the network is connected or not
	 * @return true if the network is fully-connected
	 */
	public boolean isConnected() {
		return connected;
	}
	
	/**
	 * get a space station
	 * @param n the unique identifier of the space station
	 * @return the space station with identifier = n
	 */
	private SpaceStation get(int n) {
		return stations[n];
	}
	
	/**
	 * Accumulate the powers needed to transmit messages into a thread-local 
	 * copy of a DoubleVbl.Mean. This is what averages the powers across
	 * multiple networks
	 * @param power
	 */
	public void accumulatePower(DoubleVbl.Mean power) {
		double temp;
		for(int i = 0; i < n; i++) {
			for(int j = i + 1; j < n; j++) {
				temp = shortest[i][j];
				if(!Double.isInfinite(temp) && temp != 0)
					power.accumulate(temp);
			}
		}
	}
}
