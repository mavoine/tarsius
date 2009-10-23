package org.tarsius.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.event.Event;
import org.tarsius.event.EventBelt;
import org.tarsius.gui.component.OkCancellable;
import org.tarsius.i18n.I18n;
import org.tarsius.task.ImportPhotoTask;

public class ImportControler implements OkCancellable {

	private Log log = LogFactory.getLog(ImportControler.class);
	
	private ImportDirectoryScanner scanner = new ImportDirectoryScanner();
	private ImportDialog importDialog = null;
	private List<String> fileList = null;

	public ImportControler() {
	}
	
	public void bind(ImportDialog importDialog){
		
		this.importDialog = importDialog;
		
		importDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		importDialog.browseButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				doSelectPath();
			}
		});
		
		importDialog.okCancelPanel.bind(this);

	}
	
	private void doSelectPath(){
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int option = jfc.showDialog(importDialog, I18n.translate("Select"));
		if(option == JFileChooser.APPROVE_OPTION){
			File folder = jfc.getSelectedFile();
			importDialog.pathTextField.setText(folder.getAbsolutePath());
			scanFolder(folder);
		}
	}
	
	private void scanFolder(File folder){
		// TODO implement progress feedback and cancel mecanism
		fileList = scanner.scan(folder);
		importDialog.resultLabel.setText("Found " + fileList.size() + " photos");
		importDialog.okCancelPanel.getOkButton().setEnabled(fileList.size() > 0);
	}

	public void doOk() {
		// TODO display import results
		importDialog.dispose();
		ImportPhotoTask task = new ImportPhotoTask(this.fileList);
		try {
			task.execute();
		} catch (Exception e){
			// TODO notify user
			log.error("Error occured while executing import photos task", e);
		}
		EventBelt.getInstance().dispatch(Event.Type.GALLERY_CHANGED);
	}

	public void doCancel() {
		importDialog.dispose();
	}


}
