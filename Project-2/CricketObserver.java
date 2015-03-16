
public class CricketObserver {

	public final int crickets, ticks;
	private boolean[][] chirps;
	
	public CricketObserver(int crickets, int ticks) {
		this.crickets = crickets;
		this.ticks = ticks;
		chirps = new boolean[ticks][crickets];
	}
	
	public void reportChirp(int tick, int n) {
		chirps[tick][n] = true;
	}
	
	public boolean chirped(int tick, int cricket) {
		return chirps[tick][cricket];
	}
	
	private boolean sync(int tick) {
		
		return false;
	}
	
	private boolean equal(boolean[] a, boolean[] b) {
		boolean retval = true;
		if(a.length == b.length) {
			for(int i = 0; i < a.length && retval; i++) {
				retval = a[i] == b[i];
			}
		}
		return retval;
	}
}
