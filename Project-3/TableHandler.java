//******************************************************************************
//
// File:    TableHandler.java
// Package: ---
// Unit:    Class TableHandler
//
//******************************************************************************

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import edu.rit.util.AList;

/**
 * Class handles writing the CSV file containing the results of the simulations.
 * 
 * @author Jimi Ford (jhf3617)
 * @version 4-4-2015
 */
public class TableHandler {

	// private data members
	private final String file;
	private final AList<SimulationResult> results;
	
	/**
	 * Construct a Tablehandler
	 * @param prefix the prefix of the file name
	 * @param results the collective results of the simulations
	 */
	public TableHandler(String prefix, AList<SimulationResult> results) {
		this.file = prefix + "-table.csv";
		this.results = results;
	}
	
	/**
	 * write a CSV file containing the results of the simulations
	 */
	public void write() {
		SimulationResult temp;
		StringBuilder builder = new StringBuilder();
		builder.append("num_stations, average_power, percent_connected,"+'\n');
		for(int i = 0; i < results.size(); i++) {
			temp = results.get(i);
			builder.append(temp.v + ", " + temp.averagePower + ", " + 
					temp.percentConnected + ", " +'\n');
		}
		PrintWriter tableWriter = null; 
		try {
			tableWriter = new PrintWriter(file);
			tableWriter.print(builder.toString());
		} catch (FileNotFoundException e) {
			System.err.println("Error writing table data to file \"" + 
					file+"\"");
		} finally {
			if(tableWriter != null) tableWriter.close();
		}
	}
}
