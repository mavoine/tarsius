package org.tarsius.gui;

import org.tarsius.i18n.I18n;

public class ImportDialogTest extends UITest {
	
	public static void main(String[] args) {
		I18n.init();
		showUI(new ImportDialog());
	}

}
