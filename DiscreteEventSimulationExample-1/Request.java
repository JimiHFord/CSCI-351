import edu.rit.numeric.ListSeries;
import edu.rit.sim.Simulation;

/**
 * Class Request provides a request in the web simulation.
 *
 * @author  Alan Kaminsky
 * @version 18-Apr-2014
 */
public class Request
{
	private static int idCounter = 0;

	private int id;
	private Simulation sim;
	private double startTime;
	private double finishTime;
	private ListSeries respTimeSeries;

	/**
	 * Construct a new request. The request's start time is set to the current
	 * simulation time.
	 *
	 * @param  sim     Simulation.
	 */
	public Request
	(Simulation sim)
	{
		this.id = ++ idCounter;
		this.sim = sim;
		this.startTime = sim.time();
	}

	/**
	 * Construct a new request. The request's start time is set to the current
	 * simulation time. The request's response time will be recorded in the
	 * given series.
	 *
	 * @param  sim     Simulation.
	 * @param  series  Response time series.
	 */
	public Request
	(Simulation sim,
			ListSeries series)
	{
		this (sim);
		this.respTimeSeries = series;
	}

	/**
	 * Mark this request as finished. The request's finish time is set to the
	 * current simulation time. The request's response time is recorded in the
	 * response time series.
	 */
	public void finish()
	{
		finishTime = sim.time();
		if (respTimeSeries != null) respTimeSeries.add (responseTime());
	}

	/**
	 * Returns this request's response time.
	 *
	 * @return  Response time.
	 */
	public double responseTime()
	{
		return finishTime - startTime;
	}

	/**
	 * Returns a string version of this request.
	 *
	 * @return  String version.
	 */
	public String toString()
	{
		return "Request " + id;
	}
}