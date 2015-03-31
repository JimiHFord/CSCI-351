//******************************************************************************
//
// File:    Automator.java
// Package: ---
// Unit:    Class Automator
//
//******************************************************************************

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * This class automates many calls to the Chirp main method
 * by using command line arguments from an automation file.
 * 
 * Each line in the file must either be commented out with
 * a '#', or be a valid command for Chirp.java.
 * 
 * @author Jimi Ford (jhf3617)
 * @version 3-31-2015
 */
public class Automator {

	/**
	 * 
	 * @param args command line arguments
	 * args[0] = automation file
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			usage();
		}
		try {
            List<String> lines = Files.readAllLines(Paths.get(args[0]),
                    Charset.defaultCharset());
            String[] lineArr;
            int lineCount = 0;
            boolean skip, comment;
            for (String line : lines) {
            	++lineCount;
            	line = line.trim();
            	lineArr = line.split(" ");
            	skip = lineArr[0].equals(line);
            	comment = lineArr[0].startsWith("#");
            	if(skip || comment) {
            		if(comment) {
            			if(line.equals("#")) {
            				System.out.println();
            			} else {
            				System.out.println(line);
            			}
            		}
            		continue;
            	}
                Chirp.main(lineArr);
            }
        } catch (IOException e) {
            error("Error reading automation file");
        }
	}
	
	/**
	 * display usage message and exit
	 */
	private static void usage() {
		System.err.println("usage: java Automator <automation file>");
		System.exit(1);
	}
	
	/**
	 * print error message and call usage()
	 * @param msg
	 */
	private static void error(String msg) {
		System.err.println(msg);
		usage();
	}
}
