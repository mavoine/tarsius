package org.tarsius.gui.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class MouseDoubleClickListener implements MouseListener {
	
	public abstract void doubleClicked(MouseEvent e);

	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() > 1){
			doubleClicked(e);
		}
	}

	// do nothing on those events
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

}
