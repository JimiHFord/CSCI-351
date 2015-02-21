
public class SimulationResultCollection {

	private double[][] averages;
	private int rows, cols;
	
	public final int vMin, vMax, vInc, pMin, pMax, pInc, pExp;

	public SimulationResultCollection (int vMin, int vMax, int vInc, int pMin, int pMax, int pInc, int pExp) {
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
	
	public void add(SimulationResult result) {
		int p = p(result.getP());
		int col = col(p);
		int row = row(result.getV());
		try {
			averages[row][col] = result.getAverageDistance();
		} catch (Exception e) {
		}
	}
	
	public double get(int v, int p) {
		int row = row(v);
		int col = col(p);
		return averages[row][col];
	}
	
	public double[] getAveragesForV(int v) {
		return averages[row(v)].clone();
	}
	
	private int row(int v) {
		return (v - vMin) / vInc;
	}
	
	private int p(double p) {
		return (int) (Math.round(p * pExp));
	}
	
	private int col(int p) {
		return (p - pMin) / pInc;
	}
	
}
