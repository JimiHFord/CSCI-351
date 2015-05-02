import edu.rit.numeric.ExponentialPrng;
import edu.rit.sim.Event;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;
import java.util.LinkedList;

/**
 * Class Server03 provides the server in the web simulation. The server's
 * request processing time is exponentially distributed with a given mean.
 * Requests are added to the server's queue at any time. The queue is unbounded.
 *
 * @author  Alan Kaminsky
 * @version 22-Apr-2015
 */
public class Server03
extends Server
{
	private ExponentialPrng tprocPrng;
	private LinkedList<Request> queue;

	/**
	 * True to print transcript, false to omit transcript.
	 */
	public boolean transcript = true;

	/**
	 * Construct a new server. The server's request processing time is
	 * exponentially distributed with the given mean.
	 *
	 * @param  sim    Simulation.
	 * @param  tproc  Mean request processing time.
	 * @param  prng   Pseudorandom number generator.
	 */
	public Server03
	(Simulation sim,
			double tproc,
			Random prng)
	{
		super (sim);
		this.tprocPrng = new ExponentialPrng (prng, 1.0/tproc);
		this.queue = new LinkedList<Request>();
	}

	/**
	 * Returns the number of requests in this server's queue.
	 *
	 * @return  Queue size.
	 */
	public int queueSize()
	{
		return queue.size();
	}

	/**
	 * Add the given request to this server's queue.
	 *
	 * @param  request  Request.
	 */
	public void add
	(Request request)
	{
		if (transcript)
			System.out.printf ("%.3f %s added%n", sim.time(), request);
		queue.addLast (request);
		if (queue.size() == 1) startProcessing();
	}

	/**
	 * Start processing the first request in this server's queue.
	 */
	private void startProcessing()
	{
		Request request = queue.getFirst();
		if (transcript)
			System.out.printf ("%.3f %s starts processing%n",
					sim.time(), request);
		sim.doAfter (tprocPrng.next(), new Event()
		{
			public void perform()
			{
				finishProcessing();
			}
		});
	}

	/**
	 * Finish processing the first request in this server's queue.
	 */
	private void finishProcessing()
	{
		Request request = queue.removeFirst();
		if (transcript)
			System.out.printf ("%.3f %s finishes processing%n",
					sim.time(), request);
		request.finish();
		if (! queue.isEmpty()) startProcessing();
	}
}