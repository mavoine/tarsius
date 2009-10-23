package org.tarsius.gui;

import java.awt.Dialog;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.gui.component.OkCancelPanel;
import org.tarsius.i18n.I18n;

public class OpenGalleryDialog extends JDialog {

	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(OpenGalleryDialog.class);
	private static final long serialVersionUID = 1L;
	
	protected JComboBox galleryCombo = null;
	protected JButton browseButton = null;
	protected JCheckBox rememberCheckBox = null;
	protected OkCancelPanel okCancelPanel = null;

	public OpenGalleryDialog() {
		
		this.setTitle(I18n.translate("OpenGallery"));
		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		
		MigLayout layoutManager = new MigLayout(
				"",  // layout constraints
				"",  // column constraints
				""); // row constraints

		this.setLayout(layoutManager);
		
		JLabel instructionsLabel = new JLabel(I18n.translate("SelectGalleryToOpen"));
		
		galleryCombo = new JComboBox();
		galleryCombo.setEditable(true);
		galleryCombo.setPrototypeDisplayValue(
			"/home/user/MMC-SD/CANON_DC/dev/test/testgallery");
		
		browseButton = new JButton(I18n.translate("Browse"));
		browseButton.setMnemonic(I18n.mnemonic("Browse"));
		
		rememberCheckBox = new JCheckBox(I18n.translate("RememberGallery"));
		rememberCheckBox.setMnemonic(I18n.mnemonic("RememberGallery"));
		rememberCheckBox.setSelected(true);
		
		okCancelPanel = new OkCancelPanel();
		okCancelPanel.getOkButton().setEnabled(false);
		
		this.add(instructionsLabel, "span, wrap");
		this.add(galleryCombo, "split 2, grow");
		this.add(browseButton, "wrap");
		this.add(rememberCheckBox, "span, wrap");
		this.add(okCancelPanel, "dock south");
		
		this.pack();
	}

}
