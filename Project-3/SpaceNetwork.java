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
	
	private boolean connected;
	
	private double[][] adj;
	private double[][] shortest;
	private SpaceStation[] stations;
	
	/**
	 * 
	 * @param prng
	 * @param n
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
	
	public boolean isConnected() {
		return connected;
	}
	
	public SpaceStation get(int n) {
		return stations[n];
	}
	
	public double adj(int i, int j) {
		return adj[i][j];
	}
	
	public double fw(int i, int j) {
		return shortest[i][j];
	}
	
	public static void main(String[] args) {
		int n = Integer.parseInt(args[1]);
		Random r = new Random(Long.parseLong(args[0]));
		SpaceNetwork sn;
		for(int i = 0; i < n; i++) {
			sn = new SpaceNetwork(r, n);
			System.out.println(sn.isConnected() ? "connected" : "NOT connected");
		}
		
	}
	
	
	private static void print(SpaceNetwork sn) {
		int n = sn.n;
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				if(Double.isInfinite(sn.fw(i, j))) {
					System.out.print(-1 + ", ");
				} else {
					System.out.print(sn.fw(i, j)+", ");
				}
				
			}
			System.out.println();
		}
	}
}
