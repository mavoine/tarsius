package org.tarsius.util;

import org.tarsius.util.TableUtil;

import junit.framework.TestCase;

public class TableUtilTest extends TestCase {

	public void testTableToListIndex(){
		int numColumns = 3;
		int row = 1;
		int column = 2;
		assertEquals(5, TableUtil.tableToListIndex(numColumns, row, column));
		
		row = 2;
		column = 1;
		assertEquals(7, TableUtil.tableToListIndex(numColumns, row, column));
		
		numColumns = 5;
		row = 5;
		column = 4;
		assertEquals(29, TableUtil.tableToListIndex(numColumns, row, column));
	}
	
}
