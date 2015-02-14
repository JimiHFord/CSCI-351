import edu.rit.pj2.Task;


public class MonteCarlo extends Task {

	private static final String[] arguments = {
		"<seed>",
		"<min number of vertices>",
		"<max number of vertices>",
		"<vertex granularity>",
		"<edge probability granularity>",
		"<number of simulations>"
	};
	
	private static final int 
		SEED = 0,
		MIN_VERTICES = 1,
		MAX_VERTICES = 2,
		VERTEX_GRANULARITY = 3,
		EDGE_PROBABILITY_GRANULARITY = 4,
		NUMBER_OF_SIMULATIONS_INDEX = 5;
	
	@Override
	public void main(String[] args) {
		if(args.length != 6) {
			usage();
		}
		
		long seed = 0;
		int minVertices = 0, maxVertices = 0, vertexGranularity = 0, numSimulations = 0;
		double edgeProbabilityGranularity = 0;
		
		try {
			seed = Long.parseLong(args[SEED]);
		} catch (NumberFormatException e) {
			numericLongInput(arguments[SEED]);
		}
		
		try {
			minVertices = Integer.parseInt(args[MIN_VERTICES]);
			if(minVertices < 1) throw new NumberFormatException();
		} catch (NumberFormatException e) {
			nonZeroPositiveInteger(arguments[MIN_VERTICES]);
		}
		
		try {
			maxVertices = Integer.parseInt(args[MAX_VERTICES]);
			if(maxVertices < minVertices) leftGreaterThanOrEqualToRight(
					arguments[MAX_VERTICES], arguments[MIN_VERTICES]);
		} catch (NumberFormatException e) {
			nonZeroPositiveInteger(arguments[MAX_VERTICES]);
		}
		
		try {
			vertexGranularity = Integer.parseInt(args[VERTEX_GRANULARITY]);
			if(vertexGranularity < 1) throw new NumberFormatException();
		} catch (NumberFormatException e) {
			nonZeroPositiveInteger(arguments[VERTEX_GRANULARITY]);
		}
		
		try {
			numSimulations = Integer.parseInt(args[NUMBER_OF_SIMULATIONS_INDEX]);
			if(numSimulations < 1) throw new NumberFormatException();
		} catch (NumberFormatException e) {
			nonZeroPositiveInteger(arguments[NUMBER_OF_SIMULATIONS_INDEX]);
		}
		
		try {
			edgeProbabilityGranularity = Double.parseDouble(args[EDGE_PROBABILITY_GRANULARITY]);
			if(edgeProbabilityGranularity <= 0 || edgeProbabilityGranularity >= 1) 
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			zeroToOneExclusive(arguments[EDGE_PROBABILITY_GRANULARITY]);
		}
		
		new Simulator(seed, minVertices, edgeProbabilityGranularity, numSimulations).simulate();
	}

	private static void usage() {
		System.err.printf ("Usage: java pj2 MonteCarlo %1s %2s %3s %4s %5s %6s", 
				arguments[SEED],
				arguments[MIN_VERTICES],
				arguments[MAX_VERTICES],
				arguments[VERTEX_GRANULARITY],
				arguments[EDGE_PROBABILITY_GRANULARITY],
				arguments[NUMBER_OF_SIMULATIONS_INDEX]);
		System.exit(1);
	}
	
	private static void nonZeroPositiveInteger(String arg) {
		System.err.printf("Argument %1s must be numeric and between 1 and %2d inclusive.\n", 
				arg, Integer.MAX_VALUE);
		usage();
	}
	
	private static void numericLongInput(String arg) {
		System.err.printf("Argument %1s must be numeric and between %2d and %3d inclusive.\n", arg,
				Long.MIN_VALUE, Long.MAX_VALUE);
		usage();
	}
	
	private static void zeroToOneExclusive(String arg) {
		System.err.printf("Argument %1s must be numeric and between 0 and 1 exclusive.\n", arg);
		usage();
	}
	
	private static void leftGreaterThanOrEqualToRight(String left, String right) {
		System.err.printf("Argument %1s must be greater than or equal to %2s.\n", left, right);
		usage();
	}
}
