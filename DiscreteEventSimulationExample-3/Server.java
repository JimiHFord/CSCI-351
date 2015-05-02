import edu.rit.sim.Simulation;

/**
 * Class Server is the abstract base class for a server in the web simulation.
 *
 * @author  Alan Kaminsky
 * @version 22-Apr-2015
 */
public abstract class Server
{
	protected Simulation sim;

	/**
	 * Construct a new server.
	 *
	 * @param  sim  Simulation.
	 */
	public Server
	(Simulation sim)
	{
		this.sim = sim;
	}

	/**
	 * Add the given request to this server.
	 *
	 * @param  request  Request.
	 */
	public abstract void add
	(Request request);
}