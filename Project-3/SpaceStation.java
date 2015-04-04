//******************************************************************************
//
// File:    SpaceStation.java
// Package: ---
// Unit:    Class SpaceStation
//
//******************************************************************************

/**
 * 
 * @author Jimi Ford (jhf3617)
 * @version 4-2-2015
 *
 */
public class SpaceStation {

	/**
	 * maximum dimension value allowed in 3D space
	 */
	public static final double MAX_DIM = 1.0E8;
	
	/**
	 * maximum distance a space station can transmit
	 */
	public static final double MAX_DISTANCE = 40.0E6;
	
	public final double x;
	public final double y;
	public final double z;
	
	public final int id;
	
	/**
	 * Construct a new space station. It is assumed that all the parameters are
	 * less than or equal to MAX_DIM.
	 * @param x x-coordinate in 3D space
	 * @param y y-coordinate in 3D space
	 * @param z z-coordinate in 3D space
	 */
	public SpaceStation(int id, double x, double y, double z) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * compute the straight line distance to another space station
	 * @param other the other space station to compute the distance to
	 * @return the Euclidean distance to this space station
	 */
	public double distance(SpaceStation other) {
		return Math.sqrt(powerNeeded(other));
	}
	
	/**
	 * compute the power needed to transmit to another space station
	 * @param other the other space station to calculate the power needed
	 * @return the power needed to transmit to the other space station
	 */
	public double powerNeeded(SpaceStation other) {
		return 	((other.x - x)*(other.x - x)) +
				((other.y - y)*(other.y - y)) +
				((other.z - z)*(other.z - z));
	}
	
	public static void main(String[] args) {
		SpaceStation s1 = new SpaceStation(0, 0,0,0);
		SpaceStation s2 = new SpaceStation(1, 4,3,0);
		System.out.println(s1.distance(s2));
		System.out.println(s1.powerNeeded(s2));
	}
}
