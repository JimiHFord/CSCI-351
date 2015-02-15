
public class SimulationResultCollection {

	private double[][] averages;
	private int rows, cols;
	
	private int vMin, vMax, vInc, pMin, pMax, pInc, pExp;

	private SimulationResultCollection() {
		
	}
	
	public SimulationResultCollection (int vMin, int vMax, int vInc, int pMin, int pMax, int pInc, int pExp) {
		this();
		this.vMin = vMin;
		this.vMax = vMax;
		this.vInc = vInc;
		this.pMin = pMin;
		this.pMax = pMax;
		this.pInc = pInc;
		this.pExp = pExp;
		this.rows = (vMax - vMin + vInc) / vInc;
		this.cols = (pMax - pMin + pInc) / pInc;
		this.averages = new double[rows][cols];
	}
	
	@SuppressWarnings("unused")
	public void add(SimulationResult result) {
		int p = p(result.getP());
		int col = col(p);
		int row = row(result.getV());
		int x;
		try {
		averages[row][col] = result.getAverageDistance();
		} catch (Exception e) {
			x = 0;
		}
	}
	
	public double get(int v, int p) {
		int row = row(v);
		int col = col(p);
		return averages[row][col];
	}
	
	private int row(int v) {
		return (v - vMin + 1) / vInc;
	}
	
	private int p(double p) {
		return (int) (Math.round(p * pExp));
	}
	
	private int col(int p) {
		return (p - pMin) / pInc;
	}
	
}
