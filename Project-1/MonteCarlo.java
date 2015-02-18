import edu.rit.pj2.Task;


public class MonteCarlo extends Task {

	private static final String[] arguments = {
		"<seed>",
		"<min_v>",
		"<max_v>",
		"<v_grain>",
		"<min_p>",
		"<max_p>",
		"<p_grain>",
		"<num_simulations>"
	};
	
	private static final int // indices 
		SEED = 0,
		MIN_VERTICES = 1,
		MAX_VERTICES = 2,
		VERTEX_GRANULARITY = 3,
		MIN_P = 4,
		MAX_P = 5,
		P_GRANULARITY = 6,
		NUMBER_OF_SIMULATIONS = 7;
	
	@Override
	public void main(String[] args) {
		if(args.length != 8) {
			usage();
		}
		
		long seed = 0;
		int minVertices = 0, maxVertices = 0, vertexGranularity = 0, numSimulations = 0;
		double pGrain = 0, minP = 0, maxP = 0;
		
		try {
			seed = Long.parseLong(args[SEED]);
		} catch (NumberFormatException e) {
			numericLongInput(arguments[SEED]);
		}
//		parallelFor();
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
			minP = Double.parseDouble(args[MIN_P]);
			if(minP < 0 || minP > 1) throw new NumberFormatException();
		} catch (NumberFormatException e) {
			zeroToOneInclusive(arguments[MIN_P]);
		}
		
		try {
			maxP = Double.parseDouble(args[MAX_P]);
			if(maxP < minP) MonteCarlo.leftGreaterThanOrEqualToRight(arguments[MAX_P], arguments[MIN_P]);
			if(maxP > 1) throw new NumberFormatException();
		} catch (NumberFormatException e) {
			zeroToOneInclusive(arguments[MAX_P]);
		}
		
		try {
			pGrain = Double.parseDouble(args[P_GRANULARITY]);
			if(pGrain <= 0 || pGrain > 1) 
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			zeroToOneExclusive(arguments[P_GRANULARITY]);
		}
		
		try {
			numSimulations = Integer.parseInt(args[NUMBER_OF_SIMULATIONS]);
			if(numSimulations < 1) throw new NumberFormatException();
		} catch (NumberFormatException e) {
			nonZeroPositiveInteger(arguments[NUMBER_OF_SIMULATIONS]);
		}
		
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
		final int pMin = (int) (Math.round(minP * exp));
		final int pInc = (int) (Math.round(pGrain * exp));
		pGrainStr = null;
		
		
		SimulationResultCollection results = new SimulationResultCollection(
				minVertices, maxVertices, vertexGranularity, pMin, pMax, pInc, exp);
		// loop through number of vertices
		
		for(int vCount = minVertices; vCount <= maxVertices; vCount += vertexGranularity) {
			// loop through edgeProbability
			for(int p = pMin; p <= pMax; p += pInc) {
				double prob = p / (double) exp;
				// loop through each simulation
				results.add(new Simulation(this, seed, vCount, prob, numSimulations).simulate());
			}
		}
		StringBuilder builder = new StringBuilder();
		for(int p = pMin; p <= pMax; p += pInc) {
			builder.append(", " + (p / ((double) exp))); // probabilities across the top
		}
		builder.append('\n');
		for(int v = minVertices; v <= maxVertices; v += vertexGranularity) {
			
			builder.append(v + ", ");
			for(int p = pMin; p <= pMax; p += pInc) {
				builder.append(results.get(v, p)+", ");
			}
			
			builder.append('\n');
		}
		System.out.print(builder.toString());
	}

	private static void usage() {
		System.err.printf ("Usage: java pj2 MonteCarlo "+
				"%1s %2s %3s %4s %5s %6s %7s %8s\n", 
				arguments[SEED],
				arguments[MIN_VERTICES],
				arguments[MAX_VERTICES],
				arguments[VERTEX_GRANULARITY],
				arguments[MIN_P],
				arguments[MAX_P],
				arguments[P_GRANULARITY],
				arguments[NUMBER_OF_SIMULATIONS]);
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
		System.err.printf("Argument %1s must be numeric and between 0 exclusive and 1 inclusive.\n", arg);
		usage();
	}
	
	private static void zeroToOneInclusive(String arg) {
		System.err.printf("Argument %1s must be numeric and between 0 inclusive and 1 inclusive.\n", arg);
		usage();
	}
	
	private static void leftGreaterThanOrEqualToRight(String left, String right) {
		System.err.printf("Argument %1s must be greater than or equal to %2s.\n", left, right);
		usage();
	}
}
