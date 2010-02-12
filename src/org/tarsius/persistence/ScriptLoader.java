package org.tarsius.persistence;

import java.io.File;
import java.io.FileReader;

public class ScriptLoader {
	
	private String delimiter = "@@";
	
	/**
	 * Returns the current statement delimiter.
	 * @return delimiter
	 */
	public String getDelimiter() {
		return delimiter;
	}
	
	/**
	 * Change the statement delimiter.
	 * @param delimiter
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	
	/**
	 * Loads a script from a file and returs an array of String, each
	 * containing a statement.
	 * @param path
	 * @return statements
	 * @throws Exception
	 */
	public String[] loadScript(String path) throws Exception{
		String fullScript = "";
		String[] chunkedScript;
		
		// load script from file
		try {
			File file = new File(path);
			FileReader fr = new FileReader(file);
			StringBuffer sb = new StringBuffer();
			int c = -1;
			do {
				c = fr.read();
				if(c != -1){
					sb.append((char)c);
				}
			} while (c != -1);
			fullScript = sb.toString();
		} catch (Exception ex){
			throw new Exception("Cannot read script file", ex);
		}
		
		// chunk script in statements
		chunkedScript = fullScript.split(delimiter);
		
		return chunkedScript;
	}

}
