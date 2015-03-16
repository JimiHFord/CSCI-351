import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import edu.rit.image.ByteImageQueue;
import edu.rit.image.Color;
import edu.rit.image.IndexPngWriter;
import edu.rit.image.PixelUnit;
import edu.rit.util.AList;


public class ImageHandler {

	public static final byte SILENT = 0,
							 CHIRPED = 1;
	
	public static void handle(CricketObserver o, String out) throws FileNotFoundException {
		AList<Color> palette = new AList<Color>();
		palette.addLast (new Color() .rgb (  0, 255, 0)); // green
		palette.addLast (new Color() .rgb (255,   0, 0)); // red
		
		Color green = new Color().rgb(0, 255, 0);
		Color red = new Color().rgb(255, 0, 0);
		OutputStream imageout =
				new BufferedOutputStream (new FileOutputStream (new File(out)));
		IndexPngWriter imageWriter = new IndexPngWriter
				(o.ticks, o.crickets, imageout, palette);
		imageWriter.setPixelDimensions(600, 600, PixelUnit.PIXELS_PER_CENTIMETER);
		ByteImageQueue imageQueue = imageWriter.getImageQueue();
		byte[] bytes;
		boolean chirped;
		for(int i = 0; i < o.ticks; i++) {
			bytes = new byte[o.crickets];
			for(int j = 0, cricket = 0; j < bytes.length; j++, cricket++) {
				chirped = o.chirped(i, cricket);
				bytes[j] = chirped ? CHIRPED : SILENT;
			}
			try {
				imageQueue.put(i, bytes);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			imageWriter.write();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
