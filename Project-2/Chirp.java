import java.io.FileNotFoundException;
import edu.rit.util.Random;


public class Chirp {

	private static final int GRAPH_TYPE_INDEX = 0,
							 NUM_VERTICES_INDEX = 1,
							 NUM_TICKS_INDEX = 2,
							 OUTPUT_IMAGE_INDEX = 3,
							 SEED_INDEX = 4,
							 K_INDEX = 4,
							 EDGE_PROBABILITY_INDEX = 5,
							 K_SEED_INDEX = 5,
							 REWIRE_PROBABILITY_INDEX = 6;
	
	public static void main(String[] args) {
		if(args.length != 4 && args.length != 5 && 
				args.length != 6 && args.length != 7) usage();
		int crickets = 0, ticks = 0, k = 0;
		long seed = 0;
		double prob = 0;
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
		if(!(mode == 'c' || mode == 'r' || mode == 'k' || mode == 's')) {
			error("<graph type> must be either 'c' for cycle, "
					+ "'r' for random, "
					+ "'k' for k-regular, "
					+ "'s' for small-world");
		}
		UndirectedGraph g = null;
		CricketObserver o = new CricketObserver(crickets, ticks);
		switch(mode) {
		case 'r':
			try {
				seed = Long.parseLong(args[SEED_INDEX]);
				prob = Double.parseDouble(args[EDGE_PROBABILITY_INDEX]);
				g = UndirectedGraph.randomGraph(new Random(seed), crickets, prob, o);
			} catch(NumberFormatException e) {
				error("<seed> and <edge probability> must be numeric");
			} catch(IndexOutOfBoundsException e) {
				error("<seed> and <edge probability> must be included with random graph mode");
			}
			break;
		case 'c':
			g = UndirectedGraph.cycleGraph(crickets, o);
			break;
		case 'k':
			try {
				k = Integer.parseInt(args[K_INDEX]);
				g = UndirectedGraph.kregularGraph(crickets, k, o);
			} catch (NumberFormatException e) {
				error("<k> must be an integer");
			}
			break;
		case 's':
			try {
				k = Integer.parseInt(args[K_INDEX]);
				prob = Double.parseDouble(args[REWIRE_PROBABILITY_INDEX]);
				seed = Long.parseLong(args[K_SEED_INDEX]);
				g = UndirectedGraph.smallWorldGraph(new Random(seed), crickets, k, prob, o);
			} catch (NumberFormatException e) {
				error("<k> must be an integer and <rewire probability> must be a number "
						+ "between 0 and 1, and <seed> must be numeric");
			}
			break;
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
		System.err.println("usage: java Chirp <graph type> <num vertices> <num ticks> "
				+ "<output image> {(<seed> <edge probability>), or "
				+ "(<k>), or (<k> <seed> <rewire probability>)}");
		System.exit(1);
	}
}
