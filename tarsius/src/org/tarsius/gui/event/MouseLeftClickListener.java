package org.tarsius.gui.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class MouseLeftClickListener implements MouseListener {
	
	public abstract void leftClicked(MouseEvent event);

	public void mouseClicked(MouseEvent event) {
		if(MouseEvent.BUTTON1 == event.getButton()){
			leftClicked(event);
		}
	}

	// do nothing on those events
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

}
