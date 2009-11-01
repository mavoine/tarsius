package org.tarsius.gui;

import javax.swing.JDialog;
import javax.swing.JFrame;

public abstract class UITest {
	
	static protected void showUI(JFrame ui){
		ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ui.setVisible(true);
	}

	static protected void showUI(JDialog ui){
		ui.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		ui.setVisible(true);
	}

}
