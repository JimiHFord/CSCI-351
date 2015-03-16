import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import edu.rit.image.ByteImageQueue;
import edu.rit.image.IndexPngWriter;
import edu.rit.util.Random;


public class Chirp {

	private static final int GRAPH_TYPE_INDEX = 0,
							 NUM_VERTICES_INDEX = 1,
							 NUM_TICKS_INDEX = 2,
							 OUTPUT_IMAGE_INDEX = 3,
							 SEED_INDEX = 4,
							 EDGE_PROBABILITY_INDEX = 5;
	
	public static void main(String[] args) {
		if(args.length != 4 && args.length != 6) usage();
		int crickets = 0, ticks = 0;
		long seed = 0;
		double edgeProbability = 0;
		char mode;
		String outputImage = args[OUTPUT_IMAGE_INDEX];
		
		try {
			crickets = Integer.parseInt(args[NUM_VERTICES_INDEX]);
		} catch (NumberFormatException e) {
			error("<num vertices> must be a number");
		} 
		try {
			ticks = Integer.parseInt(args[NUM_TICKS_INDEX]) + 1;
		} catch (NumberFormatException e) {
			error("<num ticks> must be numeric");
		}
		mode = args[GRAPH_TYPE_INDEX].toLowerCase().charAt(0);
		if(!(mode == 'c' || mode == 'r')) {
			error("<graph type> must be either 'c' for cycle or 'r' for random");
		}
		UndirectedGraph g = null;
		CricketObserver o = new CricketObserver(crickets, ticks);
		if(mode == 'r') {
			try {
				seed = Long.parseLong(args[SEED_INDEX]);
				edgeProbability = Double.parseDouble(args[EDGE_PROBABILITY_INDEX]);
				g = UndirectedGraph.randomGraph(new Random(seed), crickets, edgeProbability, o);
			} catch(NumberFormatException e) {
				error("<seed> and <edge probability> must be numeric");
			} catch(IndexOutOfBoundsException e) {
				error("<seed> and <edge probability> must be included with random graph mode");
			}
		} else {
			g = UndirectedGraph.cycleGraph(crickets, o);
		}
		g.vertices.get(0).forceChirp();
		Ticker.tick(g, ticks);
		
		
		
		try {
			ImageHandler.handle(o, outputImage);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void error(String msg) {
		System.err.println(msg);
		usage();
	}
	
	private static void usage() {
		System.err.println("usage: java Chirp <graph type> <num vertices> <output image> (<seed> <edge probability>)");
		System.exit(1);
	}
}
