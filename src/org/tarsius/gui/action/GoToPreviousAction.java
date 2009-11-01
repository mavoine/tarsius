package org.tarsius.gui.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GoToPreviousAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(GoToPreviousAction.class);

	private List<GoToPreviousActionListener> listeners = null;

	public GoToPreviousAction() {
		listeners = new ArrayList<GoToPreviousActionListener>();
	}

	public void actionPerformed(ActionEvent e) {
		for(GoToPreviousActionListener listener : listeners){
			try {
				listener.goToPrevious();
			} catch (Exception ex){
				log.error("Error invoking GoToPreviousActionListener", ex);
			}
		}
	}
	
	public void addActionListener(GoToPreviousActionListener listener){
		listeners.add(listener);
	}

}
