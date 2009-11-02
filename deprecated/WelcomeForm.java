package org.pixelys.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pixelys.Context;
import org.pixelys.Shutdown;

import com.jeta.forms.components.panel.FormPanel;

public class WelcomeForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(WelcomeForm.class);

	public WelcomeForm() {
		
		this.addWindowListener(new WindowListener(){
			public void windowClosing(WindowEvent e) {
				windowClose();
			}

			public void windowActivated(WindowEvent e) {
			}

			public void windowClosed(WindowEvent e) {
			}

			public void windowDeactivated(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowOpened(WindowEvent e) {
			}
		});
		
		FormPanel formPanel = new FormPanel("org/pixelys/welcome.xml");
		getContentPane().add(formPanel);
		
		AbstractButton createButton = formPanel.getButton("createButton");
		createButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				createButtonPressed();
			}
		});

		AbstractButton openButton = formPanel.getButton("openButton");
		openButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				openButtonPressed();
			}
		});

		// set the size and location of this frame
		setSize(300, 200);
//		setLocation(200, 100);
	}
	
	private void createButtonPressed(){
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int option = jfc.showDialog(this, "Create"); // TODO i18n
		if(option == JFileChooser.APPROVE_OPTION){
			File file = jfc.getSelectedFile();
			try {
				Context.createGallery(file.getAbsolutePath());
			} catch (IOException e) {
				log.fatal("Failed to create gallery", e);
				// TODO feedback to user
			}
		}
	}
	
	private void openButtonPressed(){
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int option = jfc.showDialog(this, "Open"); // TODO i18n
		if(option == JFileChooser.APPROVE_OPTION){
			File file = jfc.getSelectedFile();
			try {
				Context.openGallery(file.getAbsolutePath());
			} catch (IOException e) {
				log.fatal("Failed to open gallery", e);
				// TODO feedback to user
			}
		}
	}
	
	private void windowClose(){
		Shutdown shutter = new Shutdown(this);
		shutter.shutdown();
	}
		
}
