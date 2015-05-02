import edu.rit.sim.Simulation;

//******************************************************************************
//
// File:    MrPotatoHead.java
// Package: ---
// Unit:    Class MrPotatoHead
//
//******************************************************************************

/**
 * 
 * @author Jimi Ford (jhf3617)
 * @version 5-2-2015
 */
public class MrPotatoHead {

	private static Simulation sim;
	
	public static void main(String[] args) {
		if(args.length != 0) {
			usage();
		}
		
	}
	
	private static void usage() {
		System.err.println("usage: java MrPotatoHead");
		System.exit(1);
	}
}
