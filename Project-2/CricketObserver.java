
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
	
	public int sync() {
		int retval = -1;
		int row = ticks -1;
		while(row > 3) {
			if(!sync(row)) return retval;
			retval = row--;
		}
		return retval;
	}
	
	private boolean sync(int tick) {
		if(tick + 3 >= ticks) {
			tick = ticks - 4;
		}
		return 
			equal(chirps[tick], chirps[tick+1]) &&
			equal(chirps[tick+1], chirps[tick+2]) &&
			equal(chirps[tick+2], chirps[tick+3]);
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
