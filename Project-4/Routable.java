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

	private static int count = 0;
	
	protected Simulation sim;
	private final int id;
	
	
	public Routable(Simulation sim) {
		this.sim = sim;
		id = ++ count;
	}
	
	public boolean equals(Object o) {
		if(o == this) {
			return true;
		} 
		if(o instanceof Routable) {
			return this.id == ((Routable)o).id;
		}
		return false;
	}
	
	public abstract void receivePacket(Packet packet, Link link);
	
	public void startSending(final Packet packet, final Link link) {
		final Routable other = link.other(this);
		final double transmitTime = packet.transmitTime(link);
		link.close();
		sim.doAfter(transmitTime, new Event() {
			public void perform() {
				other.receivePacket(packet, link);
			}
		});
	}
}
