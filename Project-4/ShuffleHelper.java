//******************************************************************************
//
// File:    ShuffleHelper.java
// Package: ---
// Unit:    Class ShuffleHelper
//
//******************************************************************************

import edu.rit.util.Random;

/**
 * Class provides helper methods for picking secondary links to transmit
 * packets on when primary links are currently in use.
 * 
 * @author Jimi Ford (jhf3617)
 * @version 5-3-2015
 */
public class ShuffleHelper {

	/**
	 * Shuffle an array in place
	 * 
	 * @param prng pseudorandom number generator used with shuffling
	 * @param array the array to shuffle
	 */
	private static void shuffleArray(Random prng, int[] array) {
		for (int i = array.length - 1; i > 0; i--) {
			int index = prng.nextInt(i + 1);
			int a = array[index];
			array[index] = array[i];
			array[i] = a;
		}
	}
	
	/**
	 * Create an array with <I>size</I> elements ranging from 0 to 
	 * <I>size - 1</I>.
	 * 
	 * @param size the number of elements the array should contain
	 * @return the array containing elements from 0 to <I>size - 1</I>
	 */
	private static int[] indexArray(int size) {
		int[] retval = new int[size];
		for(int i = 0; i < size; i++) {
			retval[i] = i;
		}
		return retval;
	}
	
	/**
	 * Create a shuffled array with <I>size</I> elements ranging from 0 to
	 * <I>size - 1</I>.
	 * 
	 * @param prng pseudorandom number generator used for shuffling
	 * @param size number of elements to contain
	 * @return the shuffled array
	 */
	public static int[] shuffledArray(Random prng, int size) {
		int[] arr = indexArray(size);
		shuffleArray(prng, arr);
		return arr;
	}
}
