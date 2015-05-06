//******************************************************************************
//
// File:    Host.java
// Package: ---
// Unit:    Class Host
//
//******************************************************************************

import edu.rit.sim.Simulation;

/**
 * Class Host provides the server in the web simulation. The server's
 * request processing time is exponentially distributed with a given mean.
 * Requests are added to the server's queue at any time. The queue has a given
 * maximum size.
 *
 * @author Jimi Ford (jhf3617)
 * @version 22-Apr-2015
 */
public class Host extends Routable
{

	/**
	 * Construct a new server. The server's request processing time is
	 * exponentially distributed with the given mean. The server's request queue
	 * has the given maximum size.
	 *
	 * @param  sim    Simulation.
	 */
	public Host(Simulation sim) {
		super(sim);
	}

	/**
	 * Called when this routable object finished receiving a packet on a certain
	 * link
	 * @param packet the packet this object received
	 * @param link the link that the packet was received on
	 */
	public void receivePacket(final Packet packet, final Link link) {
		link.open();
		packet.finish();
	}
}