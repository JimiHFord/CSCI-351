
import edu.rit.util.Random;

/**
 * Class GraphFactory is the abstract base class for an object that creates
 * graphs.
 *
 * @author  Alan Kaminsky
 * @version 28-Feb-2015
 */
public abstract class GraphFactory
{
	protected Random prng;
	protected int V;

	// Construct a new graph factory.
	public GraphFactory
	(int V) // Number of vertices
	{
		this.V = V;
	}

	// Set the pseudorandom number generator for this graph factory.
	public void setPrng
	(Random prng)
	{
		this.prng = prng;
	}

	// Manufacture a graph. If g is null, then a new graph is constructed and
	// the new graph is returned; otherwise g is set to a freshly created graph
	// and g is returned.
	public abstract Graph manufacture
	(Graph g);
}