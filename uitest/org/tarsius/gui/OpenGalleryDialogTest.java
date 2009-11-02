package org.tarsius.gui;

import org.tarsius.i18n.I18n;

public class OpenGalleryDialogTest extends UITest {
	
	public static void main(String[] args) {
		I18n.init();
		OpenGalleryDialog ogf = new OpenGalleryDialog();
//		OpenGalleryControler ogfc = new OpenGalleryControler();
//		ogfc.bind(ogf);
		showUI(ogf);
	}

}
