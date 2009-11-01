package org.tarsius.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.tarsius.i18n.I18n;

public class OkCancelPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JButton okButton = null;
	private JButton cancelButton = null;
	private OkCancellable okCancellable = null;

	public OkCancelPanel() {
		init();
	}
	
	public OkCancelPanel(OkCancellable okCancellable){
		init();
		this.okCancellable = okCancellable;
	}
	
	private void init(){
		MigLayout layoutManager = new MigLayout(
				"",  // layout constraints
				"[grow, right]",  // column constraints
				"[]"); // row constraints

		this.setLayout(layoutManager);

		okButton = new JButton(I18n.translate("OK"));
		okButton.setMnemonic(I18n.mnemonic("OK"));
		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(okCancellable != null){
					okCancellable.doOk();
				}
			}
		});
		cancelButton = new JButton(I18n.translate("Cancel"));
		cancelButton.setMnemonic(I18n.mnemonic("Cancel"));
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(okCancellable != null){
					okCancellable.doCancel();
				}
			}
		});
		// TODO implement ok/cancel platform dependant ordering behavior
		this.add(okButton, "split 2");
		this.add(cancelButton, "");
	}
	
	public JButton getOkButton() {
		return okButton;
	}
	
	public JButton getCancelButton() {
		return cancelButton;
	}
	
	public void bind(OkCancellable okCancellable){
		this.okCancellable = okCancellable;
	}
	
}
