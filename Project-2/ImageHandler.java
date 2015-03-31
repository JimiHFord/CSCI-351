//******************************************************************************
//
// File:    ImageHandler.java
// Package: ---
// Unit:    Class ImageHandler
//
//******************************************************************************

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import edu.rit.image.ByteImageQueue;
import edu.rit.image.Color;
import edu.rit.image.IndexPngWriter;
import edu.rit.util.AList;


/**
 * Class takes care of saving the results of the simulation as an image
 * 
 * @author Jimi Ford (jhf3617)
 * @version 3-31-2015
 */
public class ImageHandler {

	// private data members
	private static final byte SILENT = 0,
							 CHIRPED = 1,
							 SYNC = 2;
	
	/**
	 * 
	 * @param o the cricket observer that holds the results of the simulation
	 * @param out the name of the image file to save
	 * @throws FileNotFoundException if there was an error writing to the given
	 * file
	 */
	public static void handle(CricketObserver o, String out) 
			throws FileNotFoundException {
		AList<Color> palette = new AList<Color>(); 
		Color green = new Color().rgb(0, 255, 0);// green
		Color red = new Color().rgb(255, 0, 0); // red
		Color blue = new Color().rgb(0,0,255); // blue
		palette.addLast (green);
		palette.addLast (red);
		palette.addLast (blue);
		
		
		OutputStream imageout =
				new BufferedOutputStream (new FileOutputStream (new File(out)));
		IndexPngWriter imageWriter = new IndexPngWriter
				(o.ticks, o.crickets, imageout, palette);
		ByteImageQueue imageQueue = imageWriter.getImageQueue();
		byte[] bytes;
		boolean chirped;
		int sync = o.sync();
		for(int i = 0; i < o.ticks; i++) {
			bytes = new byte[o.crickets];
			for(int j = 0, cricket = 0; j < bytes.length; j++, cricket++) {
				if(i != sync) {
					chirped = o.chirped(i, cricket);
					bytes[j] = chirped ? CHIRPED : SILENT;
				} else {
					bytes[j] = SYNC;
				}
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
