package org.tarsius.i18n;

import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.KeyStroke;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A singleton to access internationalization utilities. \n
 * Note: this singleton provides static methods rather than a getInstance()
 * method for better code readibility.
 */
public class I18n {
	
	private static I18n instance = null;
	private static Log log = LogFactory.getLog(I18n.class);
	
	private Locale locale = null;
	private ResourceBundle rb = null;
	private MessageFormat formatter = new MessageFormat("");
	
	static {
		instance = new I18n(Locale.ENGLISH); // init to default locale
	}
	
	private I18n(Locale locale) {
		this.locale = locale;
		// check the locale override switch
		String localeOverride = System.getProperty("tarsius.locale");
		if(localeOverride != null && !localeOverride.isEmpty()){
			log.info("Locale overriden, using: " + localeOverride);
			this.locale = new Locale(localeOverride);
		}
		// load the localization bundles
		rb = ResourceBundle.getBundle("translation", this.locale);
		// setup the formatter
		formatter.setLocale(this.locale);
		// set the default locale of the VM
		Locale.setDefault(this.locale);
	}
	
	/**
	 * Makes sure the I18n resources are set up. Use where the locale must be set 
	 * early.
	 */
	public static void init(){
		// nothing to do
	}
	
	public static void setLocale(Locale locale){
		instance = new I18n(locale);
	}
	
	public static Locale getLocale(){
		return instance.locale;
	}
	
	public static String translate(String key){
		String value = null;
		try{
			value = instance.rb.getString(key);
		} catch (MissingResourceException mre){
			log.warn("Missing translation key [" + key + "]");
			value = "???" + key + "???";
		}
		return value;
	}

	public static String translate(String key, Object...param){
		String value = null;
		String pattern = null;
		try{
			pattern = instance.rb.getString(key);
			instance.formatter.applyPattern(pattern);
			value = instance.formatter.format(param);
		} catch (MissingResourceException mre){
			log.warn("Missing translation key [" + key + "]");
			value = "???" + key + "???";
		}
		return value;
	}

	public static char mnemonic(String key){
		char mnemonic = KeyEvent.CHAR_UNDEFINED;
		try{
			mnemonic = instance.rb.getString(key + ".mnemonic").charAt(0);
		} catch (MissingResourceException mre){
			log.warn("Missing mnemonic for key [" + key + "]");
		}
		return mnemonic;
	}
	
	public static Integer mnemonicKey(String key){
		Integer mnemonicKey = new Integer(KeyEvent.CHAR_UNDEFINED);
		try {
			char c = instance.rb.getString(key + ".mnemonic").charAt(0);
			mnemonicKey = (int)c;
		} catch (MissingResourceException mre){
			log.warn("Missing mnemonic key for key [" + key + "]");
		} catch (ClassCastException cce){
			log.warn("Unsuitable mnemonic key for key [" + key + "]");
		}
		return mnemonicKey;
	}
	
	public static KeyStroke accelerator(String key){
		KeyStroke acc = KeyStroke.getKeyStroke(KeyEvent.CHAR_UNDEFINED);
		try {
			String accString = instance.rb.getString(key + ".accelerator");
			if(accString != null && !accString.isEmpty()){
				acc = KeyStroke.getKeyStroke(accString);
			} else {
				log.warn("Missing accelerator for key [" + key + "]");
			}
		} catch (MissingResourceException mre){
			log.warn("Missing accelerator for key [" + key + "]");
		}
		return acc;
	}
	
}
