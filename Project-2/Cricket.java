
public class Cricket extends Vertex {

	private boolean[] chirp = new boolean[3];
	private boolean willChirp;
	private int currentTick = 0;
	private final CricketObserver observer;
	
	public Cricket(int n, CricketObserver o) {
		super(n);
		this.observer = o;
	}
	
	public void forceChirp() {
		willChirp = chirp[0] = true;
	}
	
	public void emitChirp() {
		if(willChirp) {
			willChirp = false;
			int n = super.edgeCount();
			for(int i = 0; i < n; i++) {
				edges.get(i).other(this).hearChirp();
			}
			observer.reportChirp(currentTick, super.n);
		}
	}
	
	private void hearChirp() {
		chirp[2] = true;
	}
	
	public void timeTick(int tick) {
		currentTick = tick;
		willChirp = chirp[0];
		chirp[0] = chirp[1];
		chirp[1] = chirp[2];
		chirp[2] = false;
	}
	
	public boolean directFlight(Cricket other) {
		boolean retval = false;
		int e = super.edgeCount();
		Cricket o;
		for(int i = 0; i < e && !retval; i++) {
			o = super.edges.get(i).other(this);
			retval = o.equals(other);
		}
		return retval;
	}
	
	public boolean equals(Object o) {
		if( !(o instanceof Cricket)) {
			return false;
		}
		if(o == this) {
			return true;
		}
		Cricket casted = (Cricket) o;
		
		return casted.n == this.n;
	}
}
