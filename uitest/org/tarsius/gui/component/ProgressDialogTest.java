package org.tarsius.gui.component;

import javax.swing.JDialog;

import org.tarsius.gui.UITest;
import org.tarsius.i18n.I18n;

public class ProgressDialogTest extends UITest {
	
	public static void main(String[] args) {
		I18n.init();
		ProgressDialog dialog = new ProgressDialog("Progress dialog test",
				"Testing the dialog...", true,
				ProgressDialog.TextStatusType.Steps, 0, 4, 2);
		dialog.setStatus("Now testing");
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.pack();
		showUI(dialog);
	}

}
