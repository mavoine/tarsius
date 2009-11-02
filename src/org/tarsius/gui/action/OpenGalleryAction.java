package org.tarsius.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tarsius.gui.OpenGalleryControler;
import org.tarsius.gui.OpenGalleryDialog;
import org.tarsius.i18n.I18n;

public class OpenGalleryAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;

	public OpenGalleryAction() {
		super(I18n.translate("OpenGallery"));
		putValue(MNEMONIC_KEY, I18n.mnemonicKey("OpenGallery"));
	}
	
	public void actionPerformed(ActionEvent event) {
		OpenGalleryDialog openGalleryDialog = new OpenGalleryDialog();
		OpenGalleryControler openGalleryControler = new OpenGalleryControler();
		openGalleryControler.bind(openGalleryDialog);
		openGalleryDialog.setVisible(true);
	}

}
