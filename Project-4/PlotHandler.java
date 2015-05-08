//******************************************************************************
//
// File:    PlotHandler.java
// Package: ---
// Unit:    Class PlotHandler
//
//******************************************************************************

import java.awt.Color;
import java.io.IOException;
import java.text.DecimalFormat;
import edu.rit.numeric.ListXYSeries;
import edu.rit.numeric.plot.Dots;
import edu.rit.numeric.plot.Plot;

/**
 * Class PlotHandler is the delegate for dealing with visualizing the data 
 * generated by the "number crunching" program, MrPotatoHead. 
 * Its purpose is to be instantiated in MrPotatoHead with the data to plot,
 * where the write() method should then be called.
 * <P>
 * Running this program and specifying in the command line arguments the plot 
 * files previously generated will open a graphical representation of these 
 * plots for each file.
 * </P>
 * @author Jimi Ford
 * @version 5-6-2015
 *
 */
public class PlotHandler {

	// private data members
	private final String rtTotalFile;
	private final String dfTotalFile;
	private final String rtLargeFile;
	private final String dfLargeFile;
	private final String rtSmallFile;
	private final String dfSmallFile;
	private final String routerDropFile;
	private final ListXYSeries dfTotal;
	private final ListXYSeries rtTotal;
	private final ListXYSeries dfLarge;
	private final ListXYSeries rtLarge;
	private final ListXYSeries dfSmall;
	private final ListXYSeries rtSmall;
	private final ListXYSeries aDrop;
	private final ListXYSeries bDrop;
	private final ListXYSeries cDrop;
	private final ListXYSeries dDrop;
	
	/**
	 * Construct a new PlotHandler object
	 * 
	 * @param prefix the prefix to use for saving the files
	 * @param dfTotal the xy-series that contains the drop fraction info
	 * @param rtTotal the xy-series that contains the response time info
	 */
	public PlotHandler(String prefix, 
		ListXYSeries dfTotal, ListXYSeries rtTotal, 
		ListXYSeries dfLarge, ListXYSeries rtLarge, 
		ListXYSeries dfSmall, ListXYSeries rtSmall, 
		ListXYSeries aDrop, ListXYSeries bDrop, ListXYSeries cDrop,
		ListXYSeries dDrop) {
		rtTotalFile = prefix + "-traversal-time.dwg";
		dfTotalFile = prefix + "-drop-fraction.dwg";
		rtLargeFile = prefix + "-traversal-time-large.dwg";
		rtSmallFile = prefix + "-traversal-time-small.dwg";
		dfLargeFile = prefix + "-drop-fraction-large.dwg";
		dfSmallFile = prefix + "-drop-fraction-small.dwg";
		routerDropFile = prefix + "-router-drop-fraction.dwg";
		this.dfTotal = dfTotal;
		this.rtTotal = rtTotal;
		this.dfLarge = dfLarge;
		this.rtLarge = rtLarge;
		this.dfSmall = dfSmall;
		this.rtSmall = rtSmall;
		this.aDrop = aDrop;
		this.bDrop = bDrop;
		this.cDrop = cDrop;
		this.dDrop = dDrop;
	}
	
	/**
	 * Save the plot information into a file and display the plots.
	 * 
	 * @throws IOException if it can't write to the file specified
	 */
	public void write() throws IOException {
		write("Total", "0.0", dfTotal, dfTotalFile, rtTotal, rtTotalFile);
		write("Large Pkt", "0.0", dfLarge, dfLargeFile, rtLarge, rtLargeFile);
		write("Small Pkt", "0.00", dfSmall, dfSmallFile, rtSmall, rtSmallFile);
		writeRouter();
	}
	
	private void writeRouter() throws IOException {
		Plot routerDropFraction = new Plot()
		.plotTitle("Router Drop Fraction")
		.xAxisTitle ("Mean arrival rate (pkt/sec)")
		.yAxisTitle ("Drop fraction")
		.yAxisStart (0.0)
		.yAxisEnd (1.0)
		.yAxisTickFormat (new DecimalFormat ("0.0"))
		.seriesDots(null)
		.seriesColor(Color.RED)
		.xySeries(aDrop)
		.seriesColor(Color.ORANGE)
		.seriesDots(Dots.circle(Color.ORANGE))
		.xySeries(bDrop)
		.seriesDots(null)
		.seriesColor(Color.GREEN)
		.xySeries(cDrop)
		.seriesColor(Color.BLUE)
		.xySeries(dDrop)
		.labelColor(Color.RED)
		.label("<b>A</b>", 42.5, .85)
		.labelColor(Color.ORANGE)
		.label("<b>B</b>", 42.5, .75)
		.labelColor(Color.GREEN)
		.label("<b>C</b>", 42.5, .65)
		.labelColor(Color.BLUE)
		.label("<b>D</b>", 42.5, .55);
		Plot.write(routerDropFraction, routerDropFile);
	}
	
	/**
	 * Save the plot information into files.
	 * 
	 * @param titlePrefix Prefix of the plot's title
	 * @param yFormat decimal format of the traversal time y-axis labels
	 * @param df drop fraction series
	 * @param dfFile drop fraction file name
	 * @param rt response time series
	 * @param rtFile response time file
	 * @throws IOException if it fails to write to any of the specified files
	 */
	private void write(String titlePrefix, String yFormat, ListXYSeries df,
			String dfFile, ListXYSeries rt, String rtFile) throws IOException {
		Plot responseTime = new Plot()
		.plotTitle (titlePrefix+" Traversal Time")
		.xAxisTitle ("Mean arrival rate (pkt/sec)")
		.yAxisTitle ("Mean traversal time (sec)")
		.yAxisTickFormat (new DecimalFormat (yFormat))
		.seriesDots (null)
		.xySeries (rt);
		Plot dropFraction = new Plot()
		.plotTitle (titlePrefix+" Drop Fraction")
		.xAxisTitle ("Mean arrival rate (pkt/sec)")
		.yAxisTitle ("Drop fraction")
		.yAxisStart (0.0)
		.yAxisEnd (1.0)
		.yAxisTickFormat (new DecimalFormat ("0.0"))
		.seriesDots (null)
		.xySeries (df);
		Plot.write(responseTime, rtFile);
		Plot.write(dropFraction, dfFile);
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
