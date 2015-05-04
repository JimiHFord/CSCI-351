//******************************************************************************
//
// File:    Link.java
// Package: ---
// Unit:    Class Link
//
//******************************************************************************

/**
 * 
 * @author Jimi Ford (jhf3617)
 *
 */
public class Link {

	public static final int BIT_RATE = 9600;
	
	private final Routable l1;
	private final Routable l2;
	public final boolean infiniteBandwidth;
	private boolean ready;
	
	public Link(Routable l1, Routable l2) {
		this(false, l1, l2);
	}
	
	public Link(boolean infiniteBandwidth, Routable l1, Routable l2) {
		this.l1 = l1;
		this.l2 = l2;
		this.ready = true;
		this.infiniteBandwidth = infiniteBandwidth;
	}
	
	public Routable other(Routable current) {
		return this.l1.equals(current) ? l2 : l1;
	}
	
	public boolean ready() {
		return this.ready;
	}
	
	/**
	 * 
	 * @throws IllegalStateException if the link is not ready to be closed and
	 * this link has finite bandwidth
	 */
	public void close() throws IllegalStateException {
		if(!this.infiniteBandwidth) {
			if(!this.ready) {
				throw new IllegalStateException();
			}
			this.ready = false;
		} 
	}
	
	public void open() {
		this.ready = true;
	}
}
