//******************************************************************************
//
// File:    Ticker.java
// Package: ---
// Unit:    Class Ticker
//
//******************************************************************************

/**
 * Class simulates a number of time ticks on a given network of crickets
 * @author Jimi Ford (jhf3617)
 * @version 3-31-2015
 */
public class Ticker {

	/**
	 * tick a number of time ticks on a given network of crickets
	 * @param g the network of crickets to tick
	 * @param ticks the number of ticks to simulate
	 */
	public static void tick(UndirectedGraph g, int ticks) {
		for(int i = 0; i < ticks; i++) {
			g.tick(i);
		}
	}
}
