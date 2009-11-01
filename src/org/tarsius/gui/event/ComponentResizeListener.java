package org.tarsius.gui.event;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public abstract class ComponentResizeListener implements ComponentListener {
	
	public abstract void resized();

	public void componentResized(ComponentEvent e) {
		resized();
	}

	// do nothing on these events
	public void componentHidden(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}
}
