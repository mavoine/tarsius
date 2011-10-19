package org.tarsius.gui;

public class Pager {

	private int pageNumber = 1;
	private int pageSize = 1;
	private int totalSize = 0;

	public Pager() {
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		this.pageNumber = 1;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}
	
	public void goToPage(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public int getTotalSize() {
		return totalSize;
	}
	
	public boolean hasNextPage(){
		int numPages = getNumberOfPages();
		if(this.pageNumber < numPages){
			return true;
		}
		return false;
	}

	public void nextPage() {
		int numPages = getNumberOfPages();
		if(this.pageNumber + 1 <= numPages){
			this.pageNumber++;
		}
	}
	
	public boolean hasPreviousPage(){
		if(this.pageNumber - 1 > 0){
			return true;
		}
		return false;
	}

	public void previousPage() {
		if(this.pageNumber - 1 > 0){
			this.pageNumber--;
		}
	}

	public int getNumberOfPages() {
		boolean leftOver = (totalSize % pageSize) > 0;
		return (totalSize / pageSize) + (leftOver ? 1 : 0);
	}
	
	public int getFirstElementIndex(){
		return (pageNumber - 1) * pageSize;
	}
	
	public int getLastElementIndex(){
		return Math.min(((pageNumber - 1) * pageSize) + (pageSize - 1), totalSize - 1);
	}

}
