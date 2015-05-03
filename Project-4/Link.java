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
	private boolean ready;
	
	public Link(Routable l1, Routable l2) {
		this.l1 = l1;
		this.l2 = l2;
		this.ready = true;
	}
	
	public Routable other(Routable other) {
		return this.l1.equals(other) ? l2 : l1;
	}
	
	public boolean ready() {
		return this.ready;
	}
	
	/**
	 * 
	 * @throws IllegalStateException if the link is not ready to be closed
	 */
	public void close() throws IllegalStateException {
		if(!this.ready) throw new IllegalStateException();
		this.ready = false;
	}
	
	public void open() {
		this.ready = true;
	}
}
