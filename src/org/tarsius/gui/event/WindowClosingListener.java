package org.tarsius.gui.event;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public abstract class WindowClosingListener implements WindowListener {
	
	public abstract void windowClosing();

	public void windowClosing(WindowEvent e) {
		windowClosing();
	}

	// do nothing on those events
	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}

}
