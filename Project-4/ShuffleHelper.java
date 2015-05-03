import edu.rit.util.Random;


public class ShuffleHelper {

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
