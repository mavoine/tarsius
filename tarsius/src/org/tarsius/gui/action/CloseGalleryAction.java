package org.tarsius.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tarsius.Context;
import org.tarsius.i18n.I18n;

public class CloseGalleryAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public CloseGalleryAction() {
		super(I18n.translate("CloseGallery"));
		putValue(MNEMONIC_KEY, I18n.mnemonicKey("CloseGallery"));
	}
	
	public void actionPerformed(ActionEvent e) {
		Context.getGallery().close();
	}

}
