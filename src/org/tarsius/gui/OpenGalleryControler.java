package org.tarsius.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.Context;
import org.tarsius.gui.component.OkCancellable;
import org.tarsius.i18n.I18n;

public class OpenGalleryControler implements OkCancellable {
	
	private static Log log = LogFactory.getLog(OpenGalleryControler.class);
	
	private OpenGalleryDialog openGalleryDialog = null;
	private String selectedGallery = null;
	
	public OpenGalleryControler() {
	}
	
	public void bind(OpenGalleryDialog openGalleryDialog){
		
		this.openGalleryDialog = openGalleryDialog;
		
		openGalleryDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		openGalleryDialog.okCancelPanel.bind(this);

		openGalleryDialog.galleryCombo.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				log.debug("dropdown");
				gallerySelectionChanged();
			}
		});
		
		openGalleryDialog.browseButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				browse();
			}
		});

		// add recently opened galleries
		if(Context.getPreferences().getRecentGalleries().size() > 0){
			for(String gallery : Context.getPreferences().getRecentGalleries()){
				openGalleryDialog.galleryCombo.addItem(gallery);
			}
		}

	}
	
	private void browse(){
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int option = jfc.showDialog(openGalleryDialog, 
				I18n.translate("Select"));
		if(option == JFileChooser.APPROVE_OPTION){
			String path = jfc.getSelectedFile().getAbsolutePath();
			openGalleryDialog.galleryCombo.getEditor().setItem(path);
			gallerySelectionChanged();
		}				
	}

	public void doCancel() {
		openGalleryDialog.dispose();
	}

	public void doOk() {
		try {
			Context.getGallery().openGallery(selectedGallery);
			if(openGalleryDialog.rememberCheckBox.isSelected()){
				Context.getPreferences().addRecentGallery(selectedGallery);
			} else {
				Context.getPreferences().removeRecentGallery(selectedGallery);
			}
			openGalleryDialog.dispose();
		} catch (FileNotFoundException e){
			log.info("Failed to open gallery: " + selectedGallery);
			JOptionPane.showMessageDialog(openGalleryDialog, 
					I18n.translate("InvalidGalleryLocation"));
		} catch (Exception e){
			log.error("Exception while opening gallery", e);
			// TODO notify user of error
		}
	}
	
	private void gallerySelectionChanged(){
		// FIXME fix bug where after using the browse button, it is not possible to switch back to the selection in the combobox
		Object selectedItem = openGalleryDialog.galleryCombo.getEditor().getItem();//getSelectedItem();
		if(selectedItem instanceof String){
			String selectedItemStr = (String)selectedItem;
			log.debug("Selected: " + selectedItemStr);
			if(selectedItemStr != null && selectedItemStr.length() > 0){
				selectedGallery = (String)selectedItem;
				openGalleryDialog.okCancelPanel.getOkButton().setEnabled(true);
			} else {
				selectedGallery = null;
				openGalleryDialog.okCancelPanel.getOkButton().setEnabled(false);
			}
		}
	}

}
