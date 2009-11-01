package org.tarsius.gui;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.i18n.I18n;

public class ViewerPaneTest extends UITest {
	
	private static Log log = LogFactory.getLog(ViewerPaneTest.class);
	
	public static void main(String[] args) {
		I18n.init();
		String runDir = System.getProperty("user.dir");
		// TODO use an image in the testdata set
		
		ViewerPane viewerPane = new ViewerPane();
		
		// set test data
		viewerPane.fileNameLabel.setText("IMG_0452.jpg");
		viewerPane.dateLabel.setText("2009-07-11");
		File imageFile = new File("/home/user/MMC-SD/CANON_DC/samples/IMG_1810.JPG");//runDir + "/testdata/photos/rc0005.jpg");
		viewerPane.photoFrame.setImage(imageFile);

		// show the dialog
		MainWindow mainWindow = new MainWindow();
		mainWindow.show(viewerPane);
		mainWindow.pack();
		
		showUI(mainWindow);
	}
	
}
