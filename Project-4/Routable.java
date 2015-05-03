//******************************************************************************
//
// File:    Routable.java
// Package: ---
// Unit:    Class Routable
//
//******************************************************************************

import edu.rit.sim.Event;
import edu.rit.sim.Simulation;

/**
 * 
 * @author Jimi Ford (jhf3617)
 *
 */
public abstract class Routable {

	protected Simulation sim;
	
	public Routable(Simulation sim) {
		this.sim = sim;
	}
	
	public abstract void receivePacket(Packet packet, Link link);
	
	public void startSending(final Packet packet, final Link link) {
		link.close();
		final Routable other = link.other(this);
		sim.doAfter(packet.transmitTime(), new Event() {
			public void perform() {
				other.receivePacket(packet, link);
			}
		});
	}
}
