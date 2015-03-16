
public class Ticker {

	public static void tick(UndirectedGraph g, int ticks) {
		for(int i = 0; i < ticks; i++) {
			g.tick(i);
		}
	}
}
