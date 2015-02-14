//******************************************************************************
//
// File:    Concordance04.java
// Package: edu.rit.pjmr.example
// Unit:    Class edu.rit.pjmr.example.Concordance04
//
// This Java source file is copyright (C) 2014 by Alan Kaminsky. All rights
// reserved. For further information, contact the author, Alan Kaminsky, at
// ark@cs.rit.edu.
//
// This Java source file is part of the Parallel Java 2 Library ("PJ2"). PJ2 is
// free software; you can redistribute it and/or modify it under the terms of
// the GNU General Public License as published by the Free Software Foundation;
// either version 3 of the License, or (at your option) any later version.
//
// PJ2 is distributed in the hope that it will be useful, but WITHOUT ANY
// WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
// A PARTICULAR PURPOSE. See the GNU General Public License for more details.
//
// A copy of the GNU General Public License is provided in the file gpl.txt. You
// may also obtain a copy of the GNU General Public License on the World Wide
// Web at http://www.gnu.org/licenses/gpl.html.
//
//******************************************************************************

package edu.rit.pjmr.example;

import edu.rit.pj2.vbl.LongVbl;
import edu.rit.pjmr.Combiner;
import edu.rit.pjmr.Customizer;
import edu.rit.pjmr.Mapper;
import edu.rit.pjmr.PjmrJob;
import edu.rit.pjmr.Reducer;
import edu.rit.pjmr.TextDirectorySource;
import edu.rit.pjmr.TextId;
import java.util.Scanner;

/**
 * Class Concordance04 is the main program for a {@linkplain edu.rit.pjmr PJMR}
 * map-reduce job that computes a concordance of one or more text files in a
 * directory.
 * <P>
 * Usage: <TT>java pj2 [threads=<I>NT</I>] edu.rit.pjmr.example.Concordance04
 * <I>directory</I> [<I>pattern</I>]</TT>
 * <P>
 * The program examines all the files in the given directory whose names match
 * the given pattern. The pattern syntax is that of class {@linkplain
 * java.util.regex.Pattern Pattern}. If the pattern is omitted, the program
 * examines all the files in the given directory.
 * <P>
 * The program prints a list of the unique words found in the given text files,
 * along with the number of occurrences of each word. A word is a sequence of
 * consecutive non-whitespace characters; uppercase and lowercase are
 * equivalent. The words are printed in descending order of occurrence. For
 * equal occurrence, the words are printed in ascending lexicographic order.
 * <P>
 * The program runs one mapper task with one or more mappers. The number of
 * mappers is given by the <TT>threads</TT> property (default 1).
 * <P>
 * The program illustrates usage of the following classes: {@linkplain PjmrJob},
 * {@linkplain TextDirectorySource}, {@linkplain Mapper}, and {@linkplain
 * Customizer}.
 *
 * @author  Alan Kaminsky
 * @version 05-Nov-2014
 */
public class Concordance04
	extends PjmrJob<TextId,String,String,LongVbl>
	{

	/**
	 * PJMR job main program.
	 *
	 * @param  args  Command line arguments.
	 */
	public void main
		(String[] args)
		{
		if (args.length < 1 || args.length > 2) usage();
		String directory = args[0];
		String pattern = args.length == 2 ? args[1] : ".*";

		int NT = Math.max (threads(), 1);

		mapperTask()
			.source (new TextDirectorySource (directory, pattern))
			.mapper (NT, MyMapper.class);

		reducerTask()
			.customizer (MyCustomizer.class)
			.reducer (MyReducer.class);

		startJob();
		}

	/**
	 * Print a usage message and exit.
	 */
	private static void usage()
		{
		System.err.println ("Usage: java pj2 [threads=<NT>] edu.rit.pjmr.example.Concordance04 <directory> [<pattern>]");
		throw new IllegalArgumentException();
		}

	/**
	 * Mapper class.
	 */
	private static class MyMapper
		extends Mapper<TextId,String,String,LongVbl>
		{
		private static final LongVbl ONE = new LongVbl.Sum (1L);

		public void map
			(TextId inKey,   // Line number
			 String inValue, // Line from file
			 Combiner<String,LongVbl> combiner)
			{
			Scanner scanner = new Scanner (inValue);
			while (scanner.hasNext())
				{
				// For each word, remove leading and trailing non-letters and
				// convert to lowercase.
				String s = scanner.next();
				int i = 0;
				while (i < s.length() && ! Character.isLetter (s.charAt (i)))
					++ i;
				int j = s.length() - 1;
				while (j >= 0 && ! Character.isLetter (s.charAt (j)))
					-- j;
				if (i <= j)
					combiner.add (s.substring(i,j+1).toLowerCase(), ONE);
				}
			}
		}

	/**
	 * Reducer task customizer class.
	 */
	private static class MyCustomizer
		extends Customizer<String,LongVbl>
		{
		public boolean comesBefore
			(String key_1, LongVbl value_1,
			 String key_2, LongVbl value_2)
			{
			if (value_1.item > value_2.item)
				return true;
			else if (value_1.item < value_2.item)
				return false;
			else
				return key_1.compareTo (key_2) < 0;
			}
		}

	/**
	 * Reducer class.
	 */
	private static class MyReducer
		extends Reducer<String,LongVbl>
		{
		public void reduce
			(String key,    // Word
			 LongVbl value) // Number of occurrences
			{
			System.out.printf ("%s %s%n", value, key);
			System.out.flush();
			}
		}

	}
