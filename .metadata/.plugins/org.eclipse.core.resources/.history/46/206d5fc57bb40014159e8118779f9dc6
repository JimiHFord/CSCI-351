import edu.rit.pj2.Task;


public class MonteCarlo extends Task {

	private static final String[] arguments = {
		"<seed>",
		"<number of vertices>",
		"<edge probability>",
		"<number of simulations>"
	};
	
	private static final int SEED_INDEX = 0,
			VERTICES_INDEX = 1,
			EDGE_PROBABILITY_INDEX = 2,
			NUMBER_OF_SIMULATIONS_INDEX = 3;
	
	@Override
	public void main(String[] args) {
		if(args.length != 4) {
			usage();
		}
		
		int numVertices, numSimulations;
		long seed;
		double edgeProbability;
		
		try {
			seed = Long.parseLong(args[SEED_INDEX]);
		} catch (NumberFormatException e) {
			numericLongInput(arguments[SEED_INDEX]);
		}
		
		try {
			numVertices = Integer.parseInt(args[VERTICES_INDEX]);
		} catch (NumberFormatException e) {
			positiveInteger(arguments[VERTICES_INDEX]);
		}
		
		try {
			numSimulations = Integer.parseInt(args[NUMBER_OF_SIMULATIONS_INDEX]);
		} catch (NumberFormatException e) {
			positiveInteger(arguments[NUMBER_OF_SIMULATIONS_INDEX]);
		}
		
		try {
			edgeProbability = Double.parseDouble(args[EDGE_PROBABILITY_INDEX]);
			if(edgeProbability < 0 || edgeProbability > 1) throw new NumberFormatException();
		} catch (NumberFormatException e) {
			zeroToOne(arguments[EDGE_PROBABILITY_INDEX]);
		}
	}

	private static void usage() {
		System.err.printf ("Usage: java pj2 MonteCarlo %1s %2s %3s %4s", 
				arguments[SEED_INDEX],
				arguments[VERTICES_INDEX],
				arguments[EDGE_PROBABILITY_INDEX],
				arguments[NUMBER_OF_SIMULATIONS_INDEX]);
		System.exit(1);
	}
	
	private static void positiveInteger(String arg) {
		System.err.printf("Argument %1s must be numeric and between 0 and %2d inclusive.\n", 
				arg, Integer.MAX_VALUE);
		usage();
	}
	
	private static void numericLongInput(String arg) {
		System.err.printf("Argument %1s must be numeric and between %2d and %3d inclusive.\n", arg,
				Long.MIN_VALUE, Long.MAX_VALUE);
		usage();
	}
	
	private static void zeroToOne(String arg) {
		System.err.printf("Argument %1s must be numeric and between 0 and 1 inclusive.\n", arg);
		usage();
	}
}
