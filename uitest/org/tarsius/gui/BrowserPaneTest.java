package org.tarsius.gui;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.tarsius.Context;
import org.tarsius.bean.Photo;
import org.tarsius.bean.Tag;
import org.tarsius.gui.component.PhotoTableController;
import org.tarsius.i18n.I18n;
import org.tarsius.imaging.PhotoLoader;
import org.tarsius.imaging.ThumbnailsFactory;


public class BrowserPaneTest extends UITest {
	
	public static void main(String[] args) throws FileNotFoundException {

		I18n.init();
		
		String userDir = System.getProperty("user.dir");
		Context.getGallery().openGallery(userDir + File.separatorChar + "testdata", null);
		ThumbnailsFactory.getInstance().setPermanentCaching(false);

		MainWindow mainWindow = new MainWindow();
		BrowserPane browserPane = new BrowserPane();

		// add some Tag test data
		List<Tag> tags = new ArrayList<Tag>();
		tags.add(new Tag(1, "Tag1", null));
		tags.add(new Tag(4, "Tag4", 2));
		tags.add(new Tag(2, "Tag2", null));
		tags.add(new Tag(3, "Tag3", 2));
		tags.add(new Tag(5, "Tag5", 4));
		browserPane.tagTree.setData(tags);
		browserPane.addTagMenu.setData(tags);

		// add some Photo test data
		String photoDir = Context.getGallery().getPhotosPath() + File.separator;
		PhotoLoader pl = new PhotoLoader();
		List<Photo> photos = new ArrayList<Photo>();
		photos.add(pl.load(new File(photoDir + "IMG_3887.jpg")));
		photos.add(pl.load(new File(photoDir + "IMG_1444.jpg")));
		photos.add(pl.load(new File(photoDir + "IMG_1450.jpg")));
		photos.add(pl.load(new File(photoDir + "IMG_1586.jpg")));
//		browserPane.photoTable.setListData(photos);
		
		browserPane.photoTable.setEnabled(true);
		browserPane.tagTree.setEnabled(true);
		
		browserPane.infoLabel.setText(I18n.translate("NumberOfPhotosAndSelected", photos.size(), 0));
		
//		mainWindow.pack();
		mainWindow.setSize(new Dimension(600, 400));
		mainWindow.show(browserPane);

		PhotoTableController ctlr = new PhotoTableController();
		ctlr.setPhotoTable(browserPane.photoTable);
		ctlr.setPhotos(photos);

		showUI(mainWindow);
		
	}

}
