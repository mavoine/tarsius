package org.tarsius;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.config.GlobalConfig;
import org.tarsius.config.Preferences;

/**
 * Sets up the context of the execution.
 */
public class Context {
	
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(Context.class);
	
	private static GlobalConfig globalConfig = null;
	private static Gallery gallery = null;
	private static String localDirectory = null;
	private static Preferences preferences = null;
	
	static {
		globalConfig = new GlobalConfig();
		gallery = new Gallery();
		localDirectory = System.getProperty("tarsius.localdir", 
				System.getProperty("user.home") + File.separator + ".tarsius");
		// if the local directory does not exist
		File dir = new File(localDirectory);
		if(!dir.exists()){
			// create it
			dir.mkdir();
			// TODO make it hidden and readable only by current user
		}
	}
	
	/**
	 * Returns the global configuration.
	 * @return GlobalConfig
	 */
	public static GlobalConfig getGlobalConfig(){
		return globalConfig;
	}

	public static Gallery getGallery(){
		return gallery;
	}

	public static String getLocalDirectory(){
		return localDirectory;
	}
	
	public static Preferences getPreferences() {
		return preferences;
	}
	
	public static void setPreferences(Preferences preferences) {
		Context.preferences = preferences;
	}

}
