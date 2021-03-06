package org.tarsius;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.config.Preferences;
import org.tarsius.config.PreferencesPersister;
import org.tarsius.event.Event;
import org.tarsius.event.EventBelt;
import org.tarsius.event.EventListener;
import org.tarsius.gui.BrowserControler;
import org.tarsius.gui.BrowserPane;
import org.tarsius.gui.MainControler;
import org.tarsius.gui.MainWindow;
import org.tarsius.gui.ViewerControler;
import org.tarsius.gui.ViewerPane;
import org.tarsius.i18n.I18n;

public class Application {
	
	private static Log log = LogFactory.getLog(Application.class);
	
	private Application() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			// Set platform L&F
			String lnfClassName = UIManager.getSystemLookAndFeelClassName();
			log.debug("Setting look&feel: "+ lnfClassName);
			UIManager.setLookAndFeel(lnfClassName);
		} catch (Exception e) {
			log.error("Error setting the Look&Feel", e);
		}
		
		Application app = new Application();
		app.launch();
	}

	private void launch(){
		
		log.debug("Begin launch");
		
		log.debug("Retrive user preferences");
		// TODO move file access/creation some place else
		String filePath = Context.getLocalDirectory() + File.separator + 
			"preferences.properties";
		PreferencesPersister prefPersister = new PreferencesPersister();
		Preferences preferences = null;
		try {
			FileInputStream fis = new FileInputStream(filePath);
			preferences = prefPersister.load(fis);
		} catch (FileNotFoundException e) {
			log.warn(
			"The preferences file does not exist; using default values");
			preferences = new Preferences();
		}
		
		log.debug("Setup Context");
		Context.setPreferences(preferences);
		
		log.debug("Setup locale");
		I18n.setLocale(Context.getPreferences().getLocale());

		log.debug("Create main window");
		MainWindow mainWindow = new MainWindow();
		MainControler mainControler = new MainControler();
		mainControler.bind(mainWindow);
		
		log.debug("Create browser");
		BrowserPane browserPane = new BrowserPane();
		BrowserControler browserControler = new BrowserControler(
				mainControler);
		browserControler.bind(browserPane);

		log.debug("Create viewer");
		ViewerPane viewerPane = new ViewerPane();
		ViewerControler viewerControler = new ViewerControler(mainControler);
		viewerControler.bind(viewerPane);
		
		// set browser perspective
		mainWindow.show(browserPane);
		mainWindow.pack();

		// show window
		mainWindow.setVisible(true);
		if (java.awt.Toolkit.getDefaultToolkit().isFrameStateSupported(
				JFrame.MAXIMIZED_BOTH)){
			log.debug("Maximizing window");
			mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			log.debug("Window maximize not supported on the platform");
		}

		processGallerySwitch();
		
		EventBelt.getInstance().registerListener(this);

		log.debug("Launched");

	}
	
	@EventListener(eventType=Event.Type.APPLICATION_SHUTDOWN_REQUEST)
	public void shutdown(){
		log.debug("Begin shutdown procedure");
		if(Context.getGallery().isOpen()){
			log.debug("Closing the gallery");
			Context.getGallery().close();
		}
		
		log.debug("Save user preferences");
		// TODO provide a switch to select a test location for preferences to protect the user's stable setup
		// TODO move file access/creation some place else
		String filePath = Context.getLocalDirectory() + File.separator + 
			"preferences.properties";
		FileOutputStream fos = null;
		try {
			File file = new File(filePath);
			if(!file.exists()){
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			log.error("Preferences file not found after creation attempt", e);
		} catch (IOException e) {
			log.error("Unable to open preferences file for writing", e);
		}
		PreferencesPersister prefPersister = new PreferencesPersister();
		prefPersister.save(Context.getPreferences(), fos);
		
		log.debug("End of shutdown procedure");

		log.debug("Shutting down now");
		System.exit(0); // normal program termination
	}

	private void processGallerySwitch(){
		
		String gallery = System.getProperty("tarsius.gallery");
		if(gallery != null){
			log.info("Using gallery switch: [" + gallery + "]");
			// TODO move logic of creation if not exist to Gallery
			try {
				Context.getGallery().openGallery(gallery);
			} catch (FileNotFoundException e1){
				log.info("Gallery doesn't exist so it will be created now");
				try {
					Context.getGallery().createGallery(gallery);
					Context.getGallery().openGallery(gallery);
				} catch (Exception e2) {
					log.error("Could not create the gallery", e2);
				}
			}
		}
		
	}

}
