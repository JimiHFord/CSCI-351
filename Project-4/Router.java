//******************************************************************************
//
// File:    Router.java
// Package: ---
// Unit:    Class Router
//
//******************************************************************************

import edu.rit.sim.Simulation;
import edu.rit.util.AList;
import edu.rit.util.Random;

/**
 * 
 * @author Jimi Ford (jhf3617)
 * @version 5-2-2015
 */
public class Router extends Routable {

	
	
	private final Random prng;
	private Link primary;
	private AList<Link> secondary;
	
	
	public Router(Random prng, Simulation sim) {
		super(sim);
		this.prng = prng;
		this.sim = sim;
		this.secondary = new AList<Link>();
	}
	
	public void setPrimary(Link destination) {
		this.primary = destination;
	}
	
	public void addSecondary(Link destination) {
		this.secondary.addLast(destination);
	}

	public void receivePacket(final Packet packet, final Link l) {
		l.open();
		Link link = null;
		boolean goodToGo = false;
		if(primary.ready()) {
			goodToGo = true;
			link = primary;
		} else if(secondary.size() > 0) {
			
			int[] indices = ShuffleHelper.array(secondary.size());
			ShuffleHelper.shuffleArray(prng, indices);
			
			for(int i = 0; i < indices.length && !goodToGo; i++) {
				link = secondary.get(indices[i]);
				if(link.ready()) {
					goodToGo = true;
				}
			}
		}
		if(goodToGo) {
			startSending(packet, link);
		} else {
			// drop packet
		}
	}
}
