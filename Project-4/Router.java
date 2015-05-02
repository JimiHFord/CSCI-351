import edu.rit.util.Random;

//******************************************************************************
//
// File:    Router.java
// Package: ---
// Unit:    Class Router
//
//******************************************************************************

/**
 * 
 * @author Jimi Ford (jhf3617)
 * @version 5-2-2015
 */
public class Router implements Linkable {

	public static final int BIT_RATE = 9600;
	
	private final Random prng;
	private final Linkable primary;
	private final Linkable[] secondary;
	
	public Router(Random prng, Linkable primary, Linkable[] secondary) {
		this.prng = prng;
		this.primary = primary;
		this.secondary = secondary;
	}
}
