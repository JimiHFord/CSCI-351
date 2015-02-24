//******************************************************************************
//
// File:    MonteCarlo.java
// Package: ---
// Unit:    Class MonteCarlo
//
//******************************************************************************

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import edu.rit.pj2.Task;

/**
 * Class MonteCarlo takes in a seed value for a random number 
 * generator, the upper and lower boundaries for the number of vertices in 
 * each graph as well as a number to increment by, the upper and lower 
 * boundaries for the edge probability as well as a number to increment by, 
 * the number of random graphs to generate for each combination of V (vertices) 
 * and p (edge probability), and finally, a prefix for naming each plot 
 * generated by this program. After checking for valid input, this program 
 * loops through each combination of vertices and edge probabilities, running 
 * the specified number of simulations on each combination. Each random graph 
 * (or simulation) is generated by looking at every possible pair of vertices, 
 * generating a random floating point between 0 and 1, and marking these 
 * vertices with an edge connecting them if the random value is less than or 
 * equal to the specified edge probability (for that unique graph). In each 
 * simulation, the distance values of each graph are calculated with a breadth 
 * first search from vertex A to vertex B using the depth of the search as the 
 * distance from A to B.
 * 
 * @author  Jimi Ford
 * @version 2-15-2015
 */
public class MonteCarlo extends Task {

	// Private constants
	private static final String[] arguments = {
		"<seed>",
		"<min_v>",
		"<max_v>",
		"<v_grain>",
		"<min_p>",
		"<max_p>",
		"<p_grain>",
		"<num_simulations>",
		"<optional plotfile prefix>"
	};
	
	private static final int
		SEED = 0,
		MIN_VERTICES = 1,
		MAX_VERTICES = 2,
		VERTEX_GRANULARITY = 3,
		MIN_P = 4,
		MAX_P = 5,
		P_GRANULARITY = 6,
		NUMBER_OF_SIMULATIONS = 7,
		PLOT_FILE_PREFIX = 8;
	
	/**
	 * MonteCarlo's main method to be invoked by Prof. Alan Kaminsky's
	 * Parallel Java 2 library. 
	 * 
	 * @param args command line arguments
	 * 
	 * <P>
	 * usage: java pj2 MonteCarlo &lt;seed&gt; &lt;min_v&gt; &lt;max_v&gt; 
	 * &lt;v_grain&gt; &lt;min_p&gt; &lt;max_p&gt; &lt;p_grain&gt; 
	 * &lt;num_simulations&gt; &lt;optional plotfile prefix&gt;
	 * <P>
	 */
	public void main(String[] args) {
		if(args.length != 8 && args.length != 9) {
			usage();
		}
		
		long seed = 0;
		int minVertices = 0, maxVertices = 0, vertexGranularity = 0, 
				numSimulations = 0;
		double pGrain = 0, minP = 0, maxP = 0;
		
		try {
			seed = Long.parseLong(args[SEED]);
		} catch (NumberFormatException e) {
			displayError(
					String.format("Argument %1s must be numeric and between %2d "+
					"and %3d inclusive.\n", arguments[SEED],
				Long.MIN_VALUE, Long.MAX_VALUE));
		}

		try {
			minVertices = Integer.parseInt(args[MIN_VERTICES]);
			if(minVertices < 1) throw new NumberFormatException();
		} catch (NumberFormatException e) {
			displayError(
				String.format("Argument %1s must be numeric and between 1 "+
						"and %2d inclusive.\n", arguments[MIN_VERTICES], 
						Integer.MAX_VALUE));
		}
		
		try {
			maxVertices = Integer.parseInt(args[MAX_VERTICES]);
			if(maxVertices < minVertices)
				displayError(String.format(
					"Argument %1s must be greater than or equal to %2s.\n",
					arguments[MAX_VERTICES], arguments[MIN_VERTICES]));
		} catch (NumberFormatException e) {
			displayError(String.format(
				"Argument %1s must be numeric and between 1 and %2d inclusive.\n", 
					arguments[MAX_VERTICES], Integer.MAX_VALUE));
		}
		
		try {
			vertexGranularity = Integer.parseInt(args[VERTEX_GRANULARITY]);
			if(vertexGranularity < 1) throw new NumberFormatException();
		} catch (NumberFormatException e) {
			displayError(String.format(
				"Argument %1s must be numeric and between 1 and %2d inclusive.\n", 
					arguments[VERTEX_GRANULARITY], Integer.MAX_VALUE));
		}
		
		try {
			minP = Double.parseDouble(args[MIN_P]);
			if(minP < 0 || minP > 1) throw new NumberFormatException();
		} catch (NumberFormatException e) {
			displayError(String.format(
					"Argument %1s must be numeric and between "+
					"0 inclusive and 1 inclusive.\n", 
					arguments[MIN_P]));
		}
		
		try {
			maxP = Double.parseDouble(args[MAX_P]);
			if(maxP < minP)
				displayError(String.format(
						"Argument %1s must be greater than or equal to %2s.\n", 
						arguments[MAX_P], arguments[MIN_P]));
			if(maxP > 1) throw new NumberFormatException();
		} catch (NumberFormatException e) {
			displayError(String.format(
				"Argument %1s must be numeric and between "+
				"0 inclusive and 1 inclusive.\n",
					arguments[MAX_P]));
		}
		
		try {
			pGrain = Double.parseDouble(args[P_GRANULARITY]);
			if(pGrain <= 0 || pGrain > 1) 
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			displayError(String.format(
				"Argument %1s must be numeric and between "+
				"0 exclusive and 1 inclusive.\n", 
					arguments[P_GRANULARITY]));
		}
		
		try {
			numSimulations = Integer.parseInt(args[NUMBER_OF_SIMULATIONS]);
			if(numSimulations < 1) throw new NumberFormatException();
		} catch (NumberFormatException e) {
			displayError(String.format(
				"Argument %1s must be numeric and between 1 and %2d inclusive.\n", 
					arguments[NUMBER_OF_SIMULATIONS], Integer.MAX_VALUE));
		}
		
		// store file prefix
		final String plotFilePrefix = args.length == 9 ? 
				args[PLOT_FILE_PREFIX] : "plot";
		
		String pMinStr = Double.toString(minP);
		String pMaxStr = Double.toString(maxP);
		String pGrainStr = Double.toString(pGrain);
		final int sigFig = 
				Math.max(Math.max(
						pGrainStr.length() - pGrainStr.indexOf('.') - 1,
						pMaxStr.length() - pMaxStr.indexOf('.') - 1),
						pMinStr.length() - pMinStr.indexOf('.') - 1);
		int exp = 1;
		for(int i = 0; i < sigFig; i++) {
			exp *= 10;
		}
		final int pMax = (int) (Math.round(maxP * exp));
		final int pInc = (int) (Math.round(pGrain * exp));
		// if 0 is the lower bound, set pMin to the next "step" of edge probability
		// which is pInc
		final int pMin = ((int) (Math.round(minP * exp))) == 0 ?
				pInc : ((int) (Math.round(minP * exp)));
		pGrainStr = null;
		
		
		
		SimulationResultCollection results = new SimulationResultCollection(
				minVertices, maxVertices, vertexGranularity, pMin, pMax, pInc, exp);
		
		// loop through number of vertices
		for(int vCount = minVertices; vCount <= maxVertices; 
				vCount += vertexGranularity) {
			// loop through edgeProbability
			for(int p = pMin; p <= pMax; p += pInc) {
				double prob = p / (double) exp;
				// loop through each simulation
				results.add(new Simulation(this, seed, vCount, prob, 
						numSimulations).simulate());
			}
			try {
				new PlotHandler(plotFilePrefix, results, vCount).write();
			} catch (IOException e) {
				System.err.println("Error writing file for v="+vCount);	
			}
		}
				
		StringBuilder builder = new StringBuilder();
		for(int p = pMin; p<= pMax; p+= pInc) {
			builder.append(", " + (p / ((double) exp)));
		}
		builder.append('\n');
		for(int v = minVertices; v<= maxVertices; v+= vertexGranularity) {
			builder.append(v + ", ");
			for(int p = pMin; p <= pMax; p+=pInc) {
				builder.append(results.get(v,p)+", ");
			}
			builder.append('\n');
		}
		PrintWriter tableWriter = null;
		final String tableSuffix = "-table.csv"; 
		try {
			tableWriter = new PrintWriter(plotFilePrefix + tableSuffix);
			tableWriter.print(builder.toString());
		} catch (FileNotFoundException e) {
			System.err.println("Error writing table data to file \""+ 
				plotFilePrefix + tableSuffix +"\"");
		} finally {
			if(tableWriter != null) tableWriter.close();
		}
		System.out.println("Finished simulations! run \"java PlotHandler\" "+
		"followed by any number of .dwg files (that were previously generated) "+
		"to visualize the results.");
	} // main


	/**
	 * Display the proper usage of this program and exit.
	 */
	private static void usage() {
		System.err.printf ("Usage: java pj2 MonteCarlo "+
				"%1s %2s %3s %4s %5s %6s %7s %8s %9s\n", 
				arguments[SEED],
				arguments[MIN_VERTICES],
				arguments[MAX_VERTICES],
				arguments[VERTEX_GRANULARITY],
				arguments[MIN_P],
				arguments[MAX_P],
				arguments[P_GRANULARITY],
				arguments[NUMBER_OF_SIMULATIONS],
				arguments[PLOT_FILE_PREFIX]);
		System.exit(1);
	}
	
	/**
	 * Print an error message to System.err and gracefully exit
	 * @param msg the error message to display
	 */
	private static void displayError(String msg) {
		System.err.println(msg);
		usage();
	}
}
