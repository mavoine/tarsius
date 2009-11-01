package org.tarsius.gui;

import javax.swing.JFrame;

import org.tarsius.Context;
import org.tarsius.event.Event;
import org.tarsius.event.EventBelt;
import org.tarsius.event.EventListener;
import org.tarsius.gui.event.WindowClosingListener;
import org.tarsius.i18n.I18n;

public class MainControler {
	
	private MainWindow mainWindow = null;
	
	public MainControler() {
		EventBelt.getInstance().registerListener(this);
	}
	
	public void bind(MainWindow mainWindow){

		this.mainWindow = mainWindow;

		// set window behavior
		this.mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		// bind actions
		this.mainWindow.addWindowListener(new WindowClosingListener(){
			@Override
			public void windowClosing() {
				quit();
			}
		});
	
	}
	
	@EventListener(eventType=Event.Type.GALLERY_OPENED)
	public void galleryOpened(){
		// set window title
		mainWindow.setTitle(I18n.translate("ApplicationName") + " - " +
				Context.getGallery().getGalleryPath());
		// set actions
		mainWindow.openGalleryAction.setEnabled(false);
		mainWindow.closeGalleryAction.setEnabled(true);
		mainWindow.importPhotoAction.setEnabled(true);
	}
	
	@EventListener(eventType=Event.Type.GALLERY_CLOSED)
	public void galleryClosed(){
		// set window title
		this.mainWindow.setTitle(I18n.translate("ApplicationName"));
		// set actions
		mainWindow.openGalleryAction.setEnabled(true);
		mainWindow.closeGalleryAction.setEnabled(false);
		mainWindow.importPhotoAction.setEnabled(false);
	}
	
	private void quit(){
		EventBelt.getInstance().dispatch(Event.Type.APPLICATION_SHUTDOWN_REQUEST);
	}
	
	public void show(Perspective perspective){
		mainWindow.show(perspective);
	}
	
}
