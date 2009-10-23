package org.tarsius.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.tarsius.bean.Photo;
import org.tarsius.bean.Tag;
import org.tarsius.i18n.I18n;
import org.tarsius.imaging.PhotoLoader;


public class BrowserPaneTest extends UITest {
	
	public static void main(String[] args) {

		I18n.init();

		MainWindow mainWindow = new MainWindow();
		BrowserPane browserPane = new BrowserPane();

		// add some Tag test data
		Tag tag1 = new Tag(1, "Tag1", null);
		Tag tag4 = new Tag(4, "Tag4", 2);
		Tag tag2 = new Tag(2, "Tag2", null);
		Tag tag3 = new Tag(3, "Tag3", 2);
		Tag tag5 = new Tag(5, "Tag5", 4);
		List<Tag> tags = new ArrayList<Tag>();
		tags.add(tag1);
		tags.add(tag2);
		tags.add(tag3);
		tags.add(tag4);
		tags.add(tag5);
		browserPane.tagTree.setData(tags);
		browserPane.addTagMenu.setData(tags);

		// add some Photo test data
		String photoDir = System.getProperty("user.dir") + File.separator
				+ "testdata" + File.separator + "photos" + File.separator;
		PhotoLoader pl = new PhotoLoader();
		Photo photo1 = pl.load(new File(photoDir + "rc0005.jpg"));
		Photo photo2 = pl.load(new File(photoDir + "rc0010.jpg"));
		Photo photo3 = pl.load(new File(photoDir + "rc0013.jpg"));
		Photo photo4 = pl.load(new File(photoDir + "rc0014.jpg"));
		Photo photo5 = pl.load(new File(photoDir + "rc0015.jpg"));
		List<Photo> photos = new ArrayList<Photo>();
		photos.add(photo1);
		photos.add(photo2);
		photos.add(photo3);
		photos.add(photo4);
		photos.add(photo5);
		browserPane.photoTable.setListData(photos);
		
		browserPane.photoTable.setEnabled(true);
		browserPane.tagTree.setEnabled(true);
		
		mainWindow.show(browserPane);
		
		mainWindow.pack();
		
		showUI(mainWindow);
	}

}
