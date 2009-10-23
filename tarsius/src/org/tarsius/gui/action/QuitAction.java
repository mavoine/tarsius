package org.tarsius.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tarsius.event.Event;
import org.tarsius.event.EventBelt;
import org.tarsius.i18n.I18n;

public class QuitAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public QuitAction() {
		super(I18n.translate("Quit"));
		putValue(MNEMONIC_KEY, I18n.mnemonicKey("Quit"));
		putValue(ACCELERATOR_KEY, I18n.accelerator("Quit"));
	}
	
	public void actionPerformed(ActionEvent e) {
		EventBelt.getInstance().dispatch(Event.Type.APPLICATION_SHUTDOWN_REQUEST);
	}
	
}
