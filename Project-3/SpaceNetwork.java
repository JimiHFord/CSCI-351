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
		initAdjacency();
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
	
	public SpaceStation get(int n) {
		return stations[n];
	}
	
	public double adj(int i, int j) {
		return adj[i][j];
	}
	
	public static void main(String[] args) {
		int n = Integer.parseInt(args[1]);
		SpaceNetwork sn = new SpaceNetwork(new Random(Long.parseLong(args[0])),
				n);
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				if(Double.isInfinite(sn.adj(i, j))) {
					System.out.print(-1 + ", ");
				} else {
					System.out.print(sn.adj(i, j)+", ");
				}
				
			}
			System.out.println();
		}
	}
}
