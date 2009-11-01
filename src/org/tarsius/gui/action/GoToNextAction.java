package org.tarsius.gui.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GoToNextAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(GoToNextAction.class);

	private List<GoToNextActionListener> listeners = null;

	public GoToNextAction() {
		listeners = new ArrayList<GoToNextActionListener>();
	}

	public void actionPerformed(ActionEvent e) {
		for(GoToNextActionListener listener : listeners){
			try {
				listener.goToNext();
			} catch (Exception ex){
				log.error("Error invoking GoToNextActionListener", ex);
			}
		}
	}
	
	public void addActionListener(GoToNextActionListener listener){
		listeners.add(listener);
	}

}
