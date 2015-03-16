import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import edu.rit.image.ByteImageQueue;
import edu.rit.image.Color;
import edu.rit.image.IndexPngWriter;
import edu.rit.util.AList;


public class ImageHandler {

	public static void handle(CricketObserver o, String out) throws FileNotFoundException {
		AList<Color> palette = new AList<Color>();
		palette.addLast (new Color() .rgb (255,   0, 0)); // red
		palette.addLast (new Color() .rgb (  0, 255, 0)); // green
		OutputStream imageout =
				new BufferedOutputStream (new FileOutputStream (new File(out)));
		IndexPngWriter imageWriter = new IndexPngWriter
				(o.ticks + 1, o.crickets, imageout, InfectionState.palette());
		ByteImageQueue imageQueue = imageWriter.getImageQueue();
	}
}
