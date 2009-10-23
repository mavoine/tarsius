package org.tarsius.gui;

public interface ProgressObserver {
	
	public void setMinimum(int min);
	public void setMaximum(int max);
	public void setProgress(int progress);
	public void setStatus(String statusText);
	public void finished();

}
