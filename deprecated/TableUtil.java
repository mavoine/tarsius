package org.tarsius.util;

/**
 * Utility functions for Tables.
 */
public class TableUtil {
	
	/**
	 * Returns the index to which a table cell would match if the table data
	 * is mapped to a list. Row and column indices start with 0.
	 * @param numberColumns
	 * @param row
	 * @param column
	 * @return list index
	 */
	public static int tableToListIndex(int numberColumns, int row, int column){
		return (row * numberColumns) + column;
	}

}
