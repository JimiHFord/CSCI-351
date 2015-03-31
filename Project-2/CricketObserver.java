//******************************************************************************
//
// File:    CricketObserver.java
// Package: ---
// Unit:    Class CricketObserver
//
//******************************************************************************

/**
 * Class observes a group of crickets for a given number of time ticks and
 * keeps track of whether or not they have chirped or not.
 * 
 * @author Jimi Ford (jhf3617)
 * @version 3-31-2015
 */
public class CricketObserver {

	/**
	 * the number of crickets being observed
	 */
	public final int crickets;
	
	/**
	 * the number of time ticks observing for
	 */
	public final int ticks;
	
	// private data members
	private boolean[][] chirps;
	
	/**
	 * Construct a cricket observer
	 * @param crickets the number of crickets to observe
	 * @param ticks the number of time ticks observing for
	 */
	public CricketObserver(int crickets, int ticks) {
		this.crickets = crickets;
		this.ticks = ticks;
		chirps = new boolean[ticks][crickets];
	}
	
	/**
	 * called by a cricket to inform the observer that he has chirped
	 * @param tick the time tick at which the cricket is chirping
	 * @param n the unique identifier of the cricket
	 */
	public void reportChirp(int tick, int n) {
		chirps[tick][n] = true;
	}
	
	/**
	 * lookup a given time and cricket to see if it chirped at that moment
	 * @param tick the moment in time to lookup
	 * @param cricket the unique identifier of the cricket to check
	 * @return true if it chirped
	 */
	public boolean chirped(int tick, int cricket) {
		return chirps[tick][cricket];
	}
	
	/**
	 * get the time tick at which all the crickets being observed synchronized
	 * @return a number >= to 0 if they synchronized, -1 if they didn't
	 */
	public int sync() {
		int row = 0;
		while(row < ticks) {
			if(sync(row)) return row;
			row++;
		}
		return -1;
	}
	
	/**
	 * determine whether the crickets were synchronized at a given time tick or 
	 * not
	 * @param tick the time tick to test
	 * @return true if every cricket at this time tick chirped
	 */
	private boolean sync(int tick) {
		boolean retval = true;
		for(int i = 0; i < crickets && retval; i++) {
			retval = chirps[tick][i];
		}
		return retval;
	}
}
