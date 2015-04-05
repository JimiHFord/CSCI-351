//******************************************************************************
//
// File:    SimulationStation.java
// Package: ---
// Unit:    Class SimulationStation
//
//******************************************************************************

import java.io.IOException;
import edu.rit.pj2.Task;
import edu.rit.util.AList;

/**
 * Class runs a number of trials simulating a network of space stations
 * 
 * @author Jimi Ford (jhf3617)
 * @version 4-2-2015
 */
public class SimulationStation extends Task {

	// java pj2 SimulationStation 
	// [0] = <lower_bound_stations> 
	// [1] = <upper_bound_stations>
	// [2] = <increment_stations>
	// [3] = <seed>
	// [4] = <file_prefix>
	
	private static final int 
		LOWER_INDEX = 0,
		UPPER_INDEX = 1,
		INCREMENT_INDEX = 2,
		TRIALS_INDEX = 3,
		SEED_INDEX = 4,
		FILE_INDEX = 5;
	
	/**
	 * main method
	 * @param args command line arguments
	 */
	public void main(String[] args) {
		if(args.length != 6) {
			usage();
		}
		int lowerBound = 0, upperBound = 0, increment = 0, trials = 0;
		long seed = 0;
		String filePrefix = null;
		try {
			lowerBound = Integer.parseInt(args[LOWER_INDEX]);
			upperBound = Integer.parseInt(args[UPPER_INDEX]);
			increment = Integer.parseInt(args[INCREMENT_INDEX]);
			trials = Integer.parseInt(args[TRIALS_INDEX]);
			seed = Long.parseLong(args[SEED_INDEX]);
			filePrefix = args[FILE_INDEX];
		} catch (NumberFormatException e) {
			error("Detected non-numeric input where expected numeric value");
		}
		AList<SimulationResult> results = new AList<SimulationResult>();
		for(int vertices = lowerBound; vertices <= upperBound; 
				vertices += increment) {
			results.addLast(
					new Simulator(this, vertices, trials, seed).simulate());
		}
		try {
			new PlotHandler(filePrefix, results).write();
			new TableHandler(filePrefix, results).write();
		} catch (IOException e) {
			error("Error writing results file(s) using prefix: " + filePrefix);
		}
		
	}

	/**
	 * print usage statement and gracefully exit
	 */
	private static void usage() {
		System.err.println("java pj2 SimulationStation "
				+ "<lower_bound_stations> "
				+ "<upper_bound_stations> "
				+ "<increment_stations> "
				+ "<num_trials> "
				+ "<seed> "
				+ "<file_prefix>");
		System.exit(1);
	}
	
	/**
	 * print an error message and call the usage() method
	 * @param msg the error message to print
	 */
	private static void error(String msg) {
		System.err.println(msg);
		usage();
	}
}
