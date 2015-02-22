//******************************************************************************
//
// File:    PlotHandler.java
// Package: ---
// Unit:    Class PlotHandler
//
//******************************************************************************

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import edu.rit.numeric.ListXYSeries;
import edu.rit.numeric.plot.Dots;
import edu.rit.numeric.plot.Plot;

/**
 * 
 * 
 * @author Jimi Ford
 * @version 2-15-2015
 *
 */
public class PlotHandler {

	private final String fileName;
	private final int v;
	private final SimulationResultCollection collection;
	
	/**
	 * Construct a new plot handler
	 * 
	 * @param 	plotFilePrefix prefix to be used in the name of
	 * 			the plot file
	 * @param 	collection collection of results of the finished set of
	 * 			simulations. 
	 * @param v number of vertices used in each simulation
	 */
	public PlotHandler(String plotFilePrefix, SimulationResultCollection collection, int v) {
		fileName = plotFilePrefix + "-V-" + v + ".dwg";
		this.v = v;
		this.collection = collection;
	}
	
	/**
	 * Save the plot information into a file to visualize by running
	 * the main method of this class
	 * 
	 * @throws IOException if it can't write to the file specified
	 */
	public void write() throws IOException {
		ListXYSeries results = new ListXYSeries();
		double[] values = collection.getAveragesForV(v);
		for(int i = 0, p = collection.pMin; i < values.length; i++, p += collection.pInc) {
			results.add(p / ((double) collection.pExp), values[i]);
		}
		
		Plot plot = new Plot()
            .plotTitle (String.format
               ("Random Graphs, <I>V</I> = %1s", Integer.toString(v)))
            .xAxisTitle ("Edge Probability <I>p</I>")
            .xAxisTickFormat(new DecimalFormat("0.0"))
            .yAxisTitle ("Average Distance <I>d</I>")
            .yAxisTickFormat (new DecimalFormat ("0.0"))
            .seriesDots (Dots.circle (5))
            .seriesStroke (null)
            .xySeries (results);
		Plot.write(plot, new File(fileName));
	}
	
	/**
	 * Open a GUI for each plot in order to visualize the results of a
	 * previously run set of simulations.
	 * 
	 * @param args each plot file generated that you wish to visualize
	 */
	public static void main(String args[]) {
		if(args.length < 1) {
			System.err.println("Must specify at least 1 plot file.");
			usage();
		}
		
		for(int i = 0; i < args.length; i++) {
			try {
				Plot plot = Plot.read(args[i]);
				plot.getFrame().setVisible(true);
			} catch (ClassNotFoundException e) {
				System.err.println("Could not deserialize " + args[i]);
			} catch (IOException e) {
				System.err.println("Could not open " + args[i]);
			}
		}
		
	}
	
	// Private helper method
	private static void usage() {
		System.err.println("usage: java PlotHandler <plot-file-1> (<plot-file-2> <plot-file-3>... etc.)");
		System.exit(1);
	}
}
