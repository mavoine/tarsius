package org.tarsius.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.Context;
import org.tarsius.i18n.I18n;

public class CreateGalleryAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(CreateGalleryAction.class);
	
	public CreateGalleryAction() {
		super(I18n.translate("CreateGallery"));
		putValue(MNEMONIC_KEY, I18n.mnemonicKey("CreateGallery"));
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setDialogTitle(I18n.translate("SelectNewGalleryLocation"));
		int option = jfc.showDialog(null, 
				I18n.translate("Select"));
		if (option == JFileChooser.APPROVE_OPTION) {
			String path = jfc.getSelectedFile().getAbsolutePath();
			option = JOptionPane.showConfirmDialog(null, 
					I18n.translate("ConfirmGalleryCreation", path),
					I18n.translate("Confirmation"),
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if(option == JOptionPane.YES_OPTION){
				try {
					Context.getGallery().createGallery(path);
					Context.getGallery().openGallery(path);
					Context.getPreferences().addRecentGallery(path);
				} catch (Exception e1) {
					// TODO notify user
					log.error("CreateGalleryAction failed", e1);
				}
			}
		}				
	}
	
}
