package org.tarsius.gui;

import junit.framework.TestCase;


public class PagerTest extends TestCase {
	
	private Pager pager = null;
	
	@Override
	protected void setUp() throws Exception {
		pager = new Pager();
		pager.setPageSize(2);
		pager.setTotalSize(5);
		pager.goToPage(2);
	}
	
	public void testPageSize(){
		assertEquals(2, pager.getPageSize());
		pager.setPageSize(3);
		assertEquals(3, pager.getPageSize());
	}
	
	public void testTotalSize(){
		assertEquals(5, pager.getTotalSize());
		pager.setTotalSize(10);
		assertEquals(10, pager.getTotalSize());
	}
	
	public void testNumberPages(){
		assertEquals(3, pager.getNumberOfPages());
		pager.setTotalSize(10);
		assertEquals(5, pager.getNumberOfPages());
	}
	
	public void testPageNumber(){
		assertEquals(2, pager.getPageNumber());
		pager.goToPage(3);
		assertEquals(3, pager.getPageNumber());
	}
	
	public void testNextPage(){
		assertTrue(pager.hasNextPage());
		pager.nextPage();
		assertFalse(pager.hasNextPage());
		assertEquals(3, pager.getPageNumber());
	}
	
	public void testPreviousPage(){
		assertTrue(pager.hasPreviousPage());
		pager.previousPage();
		assertFalse(pager.hasPreviousPage());
		assertEquals(1, pager.getPageNumber());
	}
	
	public void testElementIndex(){
		assertEquals(2, pager.getFirstElementIndex());
		assertEquals(3, pager.getLastElementIndex());
		pager.previousPage();
		assertEquals(0, pager.getFirstElementIndex());
		assertEquals(1, pager.getLastElementIndex());
		pager.goToPage(3);
		assertEquals(4, pager.getFirstElementIndex());
		assertEquals(4, pager.getLastElementIndex());
	}

}
