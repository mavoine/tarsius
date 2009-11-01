package org.tarsius.gui.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class MouseRightClickListener implements MouseListener {
	
	public abstract void rightClicked(MouseEvent event);

	public void mouseClicked(MouseEvent event) {
		if(MouseEvent.BUTTON3 == event.getButton()){
			rightClicked(event);
		}
	}

	// do nothing on those events
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

}
