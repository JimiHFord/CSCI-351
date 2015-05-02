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

	private final Linkable l1;
	private final Linkable l2;
	
	public Link(Linkable l1, Linkable l2) {
		this.l1 = l1;
		this.l2 = l2;
	}
	
	public Linkable other(Linkable other) {
		return this.l1.equals(other) ? l2 : l1;
	}
}
