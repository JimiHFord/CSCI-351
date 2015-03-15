
public class CricketObserver {

	private int crickets, ticks;
	private boolean[][] chirps;
	
	public CricketObserver(int crickets, int ticks) {
		this.crickets = crickets;
		this.ticks = ticks;
		chirps = new boolean[ticks][crickets];
	}
	
	public void reportChirp(int tick, int n) {
		chirps[tick][n] = true;
	}
}
