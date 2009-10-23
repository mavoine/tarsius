package org.tarsius.gui.component;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;

import org.tarsius.gui.ZoomDescriptor;
import org.tarsius.gui.ZoomLevelChangeListener;
import org.tarsius.gui.action.ChangeZoomLevelAction;

public class ZoomMenuItem extends JRadioButtonMenuItem implements ZoomLevelChangeListener {
	
	private static final long serialVersionUID = 1L;
	
	public ZoomMenuItem(ButtonGroup buttonGroup,
			ChangeZoomLevelAction changeZoomLevelAction) {
		this.setAction(changeZoomLevelAction);
		changeZoomLevelAction.addChangeListener(this);
		buttonGroup.add(this);
	}

	/**
	 * The menu item waits to be notified if the action is is assigned to is fired.
	 * When that happens, it ensures it is selected.
	 */
	public void changeZoomLevel(ZoomDescriptor zoomDescriptor) {
		this.setSelected(true);
	}

}
