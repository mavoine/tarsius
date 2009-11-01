package org.tarsius.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tarsius.gui.ImportControler;
import org.tarsius.gui.ImportDialog;
import org.tarsius.i18n.I18n;

public class ImportPhotoAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;

	public ImportPhotoAction() {
		super(I18n.translate("ImportPhotos"));
		putValue(MNEMONIC_KEY, I18n.mnemonicKey("ImportPhotos"));
	}
	
	public void actionPerformed(ActionEvent e) {
		ImportDialog importDialog = new ImportDialog();
		ImportControler importControler = new ImportControler();
		importControler.bind(importDialog);
		importDialog.setVisible(true);
	}

}
