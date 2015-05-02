import edu.rit.sim.Event;
import edu.rit.sim.Simulation;

/**
 * Class Server01 provides the server in the web simulation. The server's
 * request processing time is constant.
 *
 * @author  Alan Kaminsky
 * @version 22-Apr-2015
 */
public class Server01
extends Server
{
	private double tproc;

	/**
	 * Construct a new server. The server's request processing time is constant.
	 *
	 * @param  sim    Simulation.
	 * @param  tproc  Request processing time.
	 */
	public Server01
	(Simulation sim,
			double tproc)
	{
		super (sim);
		this.tproc = tproc;
	}

	/**
	 * Add the given request to this server.
	 *
	 * @param  request  Request.
	 */
	public void add
	(final Request request)
	{
		System.out.printf ("%.3f %s added%n", sim.time(), request);
		System.out.printf ("%.3f %s starts processing%n", sim.time(), request);
		sim.doAfter (tproc, new Event()
		{
			public void perform()
			{
				System.out.printf ("%.3f %s finishes processing%n",
						sim.time(), request);
				request.finish();
			}
		});
	}
}