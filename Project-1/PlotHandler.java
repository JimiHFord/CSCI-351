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
import edu.rit.numeric.plot.Strokes;

/**
 * Class PlotHandler is the delegate for dealing with visualizing the data 
 * generated by the "number crunching" program, MonteCarlo. Its purpose is to
 * be instantiated in MonteCarlo with the data to plot, where the write()
 * method should then be called. Running this program and specifying in
 * the command line arguments the plot files previously generated will
 * open a graphical representation of these plots for each file.
 * 
 * @author Jimi Ford
 * @version 2-15-2015
 *
 */
public class PlotHandler {

	// private data members
	private final String fileName;
	private final int v;
	private final double p;
	private final boolean vMode;
	private final SimulationResultCollection collection;
	
	/**
	 * Construct a new plot handler that plots average distances for a fixed
	 * vertex count v, while varying the edge probability p
	 * 
	 * @param 	plotFilePrefix prefix to be used in the name of
	 * 			the plot file
	 * @param 	collection collection of results of the finished set of
	 * 			simulations. 
	 * @param v number of vertices used in each simulation
	 */
	public PlotHandler(String plotFilePrefix, 
			SimulationResultCollection collection, int v) {
		fileName = plotFilePrefix + "-V-" + v + ".dwg";
		this.v = v;
		this.vMode = true;
		this.p = 0;
		this.collection = collection;
	}
	
	/**
	 * Construct a new plot handler that plots average distances for a fixed
	 * edge probability p, while varying number of vertices v
	 * 
	 * @param plotFilePrefix prefix to be used in the name of
	 * 			the plot file
	 * @param collection collection of results of the finished set of
	 * 			simulations. 
	 * @param p edge probability used in each simulation
	 */
	public PlotHandler(String plotFilePrefix, 
			SimulationResultCollection collection, double p) {
		this.fileName = plotFilePrefix + "-p-" + p + ".dwg";
		this.v = 0;
		this.vMode = false;
		this.p = p;
		this.collection = collection;
	}
	
	/**
	 * Save the plot information into a file to visualize by running
	 * the main method of this class
	 * 
	 * @throws IOException if it can't write to the file specified
	 */
	public void write() throws IOException {
		if(this.vMode) {
			ListXYSeries results = new ListXYSeries();
			double[] values = collection.getAveragesForV(v);
			for(int i = 0, p = collection.pMin; i < values.length; i++, 
					p += collection.pInc) {
				results.add(p / ((double) collection.pExp), values[i]);
			}
			
			Plot plot = new Plot()
	            .plotTitle (String.format
	               ("Random Graphs, <I>V</I> = %1s", Integer.toString(v)))
	            .xAxisTitle ("Edge Probability <I>p</I>")
	            .xAxisTickFormat(new DecimalFormat("0.0"))
	            .yAxisTitle ("Average Distance <I>d</I>")
	            .yAxisTickFormat (new DecimalFormat ("0.0"))
	            .seriesDots(null)
	            .seriesStroke (Strokes.solid(2))
	            .xySeries (results);
			Plot.write(plot, new File(fileName));
		} else {
			ListXYSeries results = new ListXYSeries();
			double[] values = collection.getAveragesForP(p);
			for(int i = 0, v = collection.vMin; i < values.length; i++, 
					v += collection.vInc) {
				results.add(v, values[i]);
			}
			
			Plot plot = new Plot()
	            .plotTitle (String.format
	               ("Random Graphs, <I>p</I> = %1s", Double.toString(p)))
	            .xAxisTitle ("Number of Vertices <I>V</I>")
	            .xAxisTickFormat(new DecimalFormat("0"))
	            .yAxisTitle ("Average Distance <I>d</I>")
	            .yAxisTickFormat (new DecimalFormat ("0.0"))
	            .seriesDots(null)
	            .seriesStroke (Strokes.solid(2))
	            .xySeries (results);
			Plot.write(plot, new File(fileName));
		}
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
			} catch (IllegalArgumentException e) {
				System.err.println("Error in file " + args[i]);
			}
		}
		
	}
	
	/**
	 * Print the usage message for this program and gracefully exit.
	 */
	private static void usage() {
		System.err.println("usage: java PlotHandler <plot-file-1> "+
				"(<plot-file-2> <plot-file-3>... etc.)");
		System.exit(1);
	}
}
