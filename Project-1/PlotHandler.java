import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import edu.rit.numeric.ListXYSeries;
import edu.rit.numeric.plot.Dots;
import edu.rit.numeric.plot.Plot;


public class PlotHandler {

	private final String fileName;
	private final int v;
	private final SimulationResultCollection collection;
	public PlotHandler(String plotFilePrefix, SimulationResultCollection collection, int v) {
		fileName = plotFilePrefix + "-V-" + v + ".dwg";
		this.v = v;
		this.collection = collection;
	}
	
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
//            .seriesStroke (null)
            .xySeries (results);
		Plot.write(plot, new File(fileName));
	}
	
	public static void main(String args[]) {
		if(args.length < 1) {
			usage();
		}
		try {
			for(int i = 0; i < args.length; i++) {
				Plot plot = Plot.read(args[i]);
				plot.getFrame().setVisible(true);
			}
		} catch (ClassNotFoundException e) {
			System.err.println("Could not deserialize plot file.");
			usage();
		} catch (IOException e) {
			System.err.println("Could not open " + args[0]);
			usage();
		}
	}
	
	private static void usage() {
		System.err.println("usage: java PlotHandler <plot-file-1> (<plot-file-2> <plot-file-3>... etc.)");
		System.exit(1);
	}
}
