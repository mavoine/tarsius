package org.tarsius.gui;

import java.awt.Dialog;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.gui.component.OkCancelPanel;
import org.tarsius.i18n.I18n;

public class ImportDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(ImportDialog.class);

	protected JTextField pathTextField = null;
	protected JButton browseButton = null;
	protected JLabel resultLabel = null;
	protected OkCancelPanel okCancelPanel = null;
	
	public ImportDialog() {
		
		this.setTitle(I18n.translate("ImportPhotos"));
		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		
		MigLayout layoutManager = new MigLayout(
					"",  // layout constraints
					"",  // column constraints
					""); // row constraints

		this.setLayout(layoutManager);
		
		pathTextField = new JTextField();
		pathTextField.setEditable(false);
		
		JLabel instructionsLabel = 
			new JLabel(I18n.translate("SelectImportDirectory"));
		instructionsLabel.setVerticalAlignment(JLabel.TOP);
		
		browseButton = new JButton(I18n.translate("Browse"));
		browseButton.setMnemonic(I18n.mnemonic("Browse"));
		
		resultLabel = new JLabel(" ");
		
		okCancelPanel = new OkCancelPanel();
		okCancelPanel.getOkButton().setEnabled(false);
		
		this.add(instructionsLabel, "span, wrap");
		this.add(pathTextField, "split 2, grow");
		this.add(browseButton, "wrap");
		this.add(resultLabel, "grow");
		this.add(okCancelPanel, "dock south");
		
		this.pack();
	}

}
