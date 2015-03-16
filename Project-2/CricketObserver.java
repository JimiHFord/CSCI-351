
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
}
