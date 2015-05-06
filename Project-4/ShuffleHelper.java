//******************************************************************************
//
// File:    ShuffleHelper.java
// Package: ---
// Unit:    Class ShuffleHelper
//
//******************************************************************************

import edu.rit.util.Random;

/**
 * 
 * 
 * @author Jimi Ford (jhf3617)
 * @version 5-3-2015
 */
public class ShuffleHelper {

	/**
	 * 
	 * @param prng
	 * @param array
	 */
	public static void shuffleArray(Random prng, int[] array) {
		for (int i = array.length - 1; i > 0; i--) {
			int index = prng.nextInt(i + 1);
			int a = array[index];
			array[index] = array[i];
			array[i] = a;
		}
	}
	
	public static int[] array(int size) {
		int[] retval = new int[size];
		for(int i = 0; i < size; i++) {
			retval[i] = i;
		}
		return retval;
	}
}
