import java.awt.Color;
import java.io.IOException;

import edu.rit.numeric.plot.Dots;
import edu.rit.numeric.plot.Plot;
import edu.rit.numeric.plot.Strokes;


public class PlotHandler {

	private final String fileName;
	public PlotHandler(String plotFilePrefix, SimulationResultCollection collection, int v) {
		fileName = plotFilePrefix + "-V-" + v + ".dwg";
	}
	
	public void write() throws IOException {
		
		Plot.write
		(new Plot()
            .plotTitle (String.format
               ("Random Graphs, <I>V</I> = %1s", args[1]))
            .xAxisTitle ("Edge Probability <I>p</I>")
//            .xAxisLength (360)
            .yAxisTitle ("pr(<I>d</I>)")
//            .yAxisLength (222)
//            .yAxisTickFormat (new DecimalFormat ("0.0"))
//            .yAxisMajorDivisions (5)
//            .seriesDots (null)
            .seriesStroke (Strokes.solid (1))
            .seriesColor (Color.RED)
            .xySeries (expectSeries)
            .seriesDots (Dots.circle (5))
            .seriesStroke (null)
            .xySeries (plotResults),
         plotFile);
		
		throw new IOException();
	}
}
