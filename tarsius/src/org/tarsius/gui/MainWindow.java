package org.tarsius.gui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.tarsius.gui.action.CloseGalleryAction;
import org.tarsius.gui.action.CreateGalleryAction;
import org.tarsius.gui.action.ImportPhotoAction;
import org.tarsius.gui.action.OpenGalleryAction;
import org.tarsius.gui.action.QuitAction;
import org.tarsius.i18n.I18n;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	// menu
	private JMenuBar menuBar = null;
	protected JMenu fileMenu = null;

	// actions
	protected CreateGalleryAction createGalleryAction = null;
	protected OpenGalleryAction openGalleryAction = null;
	protected CloseGalleryAction closeGalleryAction = null;
	protected ImportPhotoAction importPhotoAction = null;
	protected QuitAction quitAction = null;

	public MainWindow() {

		// set window title
		this.setTitle(I18n.translate("ApplicationName"));
		
		// create actions
		createGalleryAction = new CreateGalleryAction();
		openGalleryAction = new OpenGalleryAction();
		closeGalleryAction = new CloseGalleryAction();
		importPhotoAction = new ImportPhotoAction();
		quitAction = new QuitAction();
		
		// init menu bar
		menuBar = new JMenuBar();

		// create menus
		// file
		fileMenu = new JMenu(I18n.translate("File"));
		fileMenu.setMnemonic(I18n.mnemonic("File"));
		fileMenu.add(createGalleryAction);
		fileMenu.add(openGalleryAction);
		fileMenu.add(closeGalleryAction);
		fileMenu.addSeparator();
		fileMenu.add(importPhotoAction);
		fileMenu.addSeparator();
		fileMenu.add(quitAction);

	}

	public void show(Perspective perspective){
		
		// set content pane
		setContentPane(perspective.getContentPane());
		
		// prepare menu bar
		// free all menus currently associated with this bar
		menuBar.removeAll();
		menuBar.add(fileMenu);
		// add menus from the perspective
		JMenu[] perspectiveMenus = perspective.getMenus();
		if(perspectiveMenus != null){
			for(JMenu menu : perspectiveMenus){
				menuBar.add(menu);
			}
		}
		
		// set menu bar
		setJMenuBar(menuBar);
	}

}
