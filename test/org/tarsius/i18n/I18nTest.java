package org.tarsius.i18n;

import java.util.Locale;

import junit.framework.TestCase;

public class I18nTest extends TestCase {
	
	public void testTranslate(){
		I18n.setLocale(Locale.ENGLISH);
		assertEquals("This is a test", I18n.translate("test"));
		I18n.setLocale(Locale.FRENCH);
		assertEquals("Ceci est un test", I18n.translate("test"));
	}
	
	public void testTranslateWithParams(){
		I18n.setLocale(Locale.ENGLISH);
		assertEquals("The sum of 1 and 1 equals 2", I18n.translate("testSum",
				new Object[] { 1, 1, 2 }));
		I18n.setLocale(Locale.FRENCH);
		assertEquals("La somme de 1 et 1 font 2", I18n.translate("testSum",
				new Object[] { 1, 1, 2 }));
	}
	
	public void testMnemonic(){
		I18n.setLocale(Locale.ENGLISH);
		assertEquals('C', I18n.mnemonic("CloseGallery"));
		I18n.setLocale(Locale.FRENCH);
		assertEquals('F', I18n.mnemonic("CloseGallery"));
	}
	
	public void testMnemonicKey(){
		I18n.setLocale(Locale.ENGLISH);
		assertEquals(50, I18n.mnemonicKey("testkey").intValue());
		I18n.setLocale(Locale.FRENCH);
		assertEquals(50, I18n.mnemonicKey("testkey").intValue());
	}

}
