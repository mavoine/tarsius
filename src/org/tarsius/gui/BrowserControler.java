package org.tarsius.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.Context;
import org.tarsius.bean.Photo;
import org.tarsius.bean.Tag;
import org.tarsius.event.Event;
import org.tarsius.event.EventBelt;
import org.tarsius.event.EventListener;
import org.tarsius.gui.event.MouseDoubleClickListener;
import org.tarsius.gui.event.MouseRightClickListener;
import org.tarsius.i18n.I18n;
import org.tarsius.persistence.PersistenceException;
import org.tarsius.persistence.dao.PhotoDAO;
import org.tarsius.persistence.dao.TagDAO;
import org.tarsius.persistence.dao.criteria.PhotoCriteria;

public class BrowserControler {
	
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(BrowserControler.class);
	private MainControler mainControler = null;
	private BrowserPane browserPane = null;
	private PhotoFilter photoFilter = null;
	
	// data
	List<Tag> tags = null;
	List<Photo> photos = null;
		
	public BrowserControler(MainControler mainControler) {
		
		this.mainControler = mainControler;
		this.photoFilter = new PhotoFilter();
		
		EventBelt.getInstance().registerListener(this);
	}
	
	public void bind(BrowserPane browserPane){
		
		this.browserPane = browserPane;
		
		// set components initial state
//		mainForm.filterPanel.setVisible(false);
		// TODO fix layout issue with invisible components, then make it disappear when no filter is in use
		
		browserPane.photoTable.addMouseListener(new MouseDoubleClickListener(){
			@Override
			public void doubleClicked(MouseEvent e) {
				viewPhoto();
			}
		});

		// TODO finish mapping ESC key on "return to photo table"
//		mainForm.centerPanel.getActionMap().put(KeyEvent.VK_ESCAPE,
//				mainForm.restoreViewAction);

		browserPane.photoTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						// TODO detect "selection empty" to disable the delete action
						photoSelectionChanged(event);
					}
				});
		
		browserPane.photoTable.addMouseListener(new MouseRightClickListener() {
			@Override
			public void rightClicked(MouseEvent event) {
				photoTableRightClicked(event);
			}
		});
		
		browserPane.tagTree.getSelectionModel().addTreeSelectionListener(
				new TreeSelectionListener() {
					public void valueChanged(TreeSelectionEvent event) {
						tagSelectionChanged(event);
					}
				});
		
		browserPane.tagTree.addMouseListener(new MouseRightClickListener(){
			@Override
			public void rightClicked(MouseEvent event) {
				tagTreeRightClicked(event);
			}
		});
		
		browserPane.tagTree.addMouseListener(new MouseDoubleClickListener(){
			@Override
			public void doubleClicked(MouseEvent event) {
				filterByTag(event);
			}
		});
		
		browserPane.clearFilterButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				clearFilter();
			}
		});
	}
	
	private void clearFilter(){
		photoFilter.reset();
		EventBelt.getInstance().dispatch(Event.Type.FILTER_CHANGED);
	}
	
	private void filterByTag(MouseEvent event){
		Tag tag = browserPane.tagTree.getTagAtLocation(event.getX(), event.getY());
		photoFilter.reset();
		photoFilter.setTag(tag);
		EventBelt.getInstance().dispatch(Event.Type.FILTER_CHANGED);
	}
	
	@EventListener(eventType=Event.Type.FILTER_CHANGED)
	public void filterChanged(){
		if(photoFilter.isEmpty()){
			browserPane.filterTextField.setText("");
			browserPane.clearFilterButton.setEnabled(false);
		} else {
			browserPane.filterTextField.setText(photoFilter.getDescription());
			browserPane.clearFilterButton.setEnabled(true);
		}
	}
	
	private void tagTreeRightClicked(MouseEvent event) {
		browserPane.tagPopupMenu.show(event.getComponent(), event.getX(), event
				.getY());
		// TODO fix tag selection stuff on right-click
//		Tag clickedTag = browserPane.tagTree.getTagAtLocation(event.getX(),
//				event.getY());
//		Tag selectedTag = browserPane.tagTree.getSelectedTag();
//		if(clickedTag != null && !clickedTag.equals(selectedTag)){
//			browserPane.tagTree.selectTagAtLocation(event.getX(), event.getY());
//		}
	}

	private void photoTableRightClicked(MouseEvent event) {
		Photo clickedPhoto = browserPane.photoTable.getPhotoAtLocation(event
				.getX(), event.getY());
		if(clickedPhoto == null){
			// if user did not click a photo, do nothing
			return;
		}
		List<Photo> selectedPhotos = browserPane.photoTable.getSelectedPhotos();
		if(selectedPhotos.isEmpty()){
			// if there is no photo selected
			browserPane.photoTable.addPhotoToSelection(clickedPhoto);
		} else if(!selectedPhotos.contains(clickedPhoto)){
			// if the user clicked a photo which was not selected
			browserPane.photoTable.clearSelection();
			browserPane.photoTable.addPhotoToSelection(clickedPhoto);
		}
		// show the popup menu at the click location
		browserPane.photoPopupMenu.show(event.getComponent(), event.getX(), event
				.getY());
	}

	private void viewPhoto(){
		// get the photo to display
		int index = browserPane.photoTable.getSelectedIndex();
		if(index < 0){
			return;
		}
		OpenViewerEvent openViewerEvent = new OpenViewerEvent(photos, index);
		EventBelt.getInstance().dispatch(openViewerEvent);
	}
	
	@EventListener(eventType={
			Event.Type.GALLERY_CHANGED,
			Event.Type.FILTER_CHANGED,
			Event.Type.TAGS_CHANGED})
	public void refreshPhotos(){
		photos = null;
//		List<Photo> selectedPhotos = null;
		
		// clear current selection
		browserPane.photoTable.getSelectionModel().clearSelection();
		
		try {
			if(Context.getGallery().isOpen()){
//				selectedPhotos = browserPane.photoTable.getSelectedPhotos();
				PhotoCriteria criteria = photoFilter.getPhotoCriteria();
				photos = PhotoDAO.getInstance().getPhotos(criteria);
				log.debug("Photos loaded for display: " + photos.size());
			} else {
				photos = new ArrayList<Photo>();
			}
		} catch (PersistenceException pe){
			log.error("Could not load photos", pe);
			// TODO notify user
		} finally {
			// set data
			//browserPane.photoTable.setListData(photos);
			browserPane.photoTableControler.setPhotos(photos);
			
//			// restore pre-refresh selection
//			if(selectedPhotos != null){
//				for(Photo photo : selectedPhotos){
//					if(photos.contains(photo)){
//						int index = photos.indexOf(photo);
//						browserPane.photoTable.addSelectionInterval(index, index);
//					}
//				}
//			}
			
			refreshInfoLabel();
		}
	}
	
	private void refreshInfoLabel(){
		if(Context.getGallery().isOpen()){
			browserPane.infoLabel.setText(I18n.translate("NumberOfPhotosAndSelected", 
				browserPane.photoTable.getModel().getSize(), browserPane.photoTable.getSelectedPhotos().size()));
		} else {
			browserPane.infoLabel.setText("");
		}
	}
	
	@EventListener(eventType=Event.Type.GALLERY_OPENED)
	public void galleryOpened(){
		// set actions
		browserPane.deletePhotoFromGalleryAction.setEnabled(false);
		browserPane.deletePhotoFromDriveAction.setEnabled(false);
		browserPane.createTagAction.setEnabled(true);
		browserPane.renameTagAction.setEnabled(false);
		browserPane.deleteTagAction.setEnabled(false);
		// set menus
		browserPane.photoMenu.setEnabled(true);
		browserPane.addTagMenu.setEnabled(false);
		browserPane.addTagMenuForPopup.setEnabled(false);
		browserPane.removeTagMenu.setEnabled(false);
		browserPane.removeTagMenuForPopup.setEnabled(false);
		browserPane.tagMenu.setEnabled(true);
		// set components
		browserPane.tagTree.setEnabled(true);
		browserPane.photoTable.setEnabled(true);
		// refresh UI
		refreshTags();
		refreshPhotos();
	}

	@EventListener(eventType=Event.Type.GALLERY_CLOSED)
	public void galleryClosed(){
		// set actions
		browserPane.deletePhotoFromGalleryAction.setEnabled(false);
		browserPane.deletePhotoFromDriveAction.setEnabled(false);
		browserPane.createTagAction.setEnabled(false);
		browserPane.renameTagAction.setEnabled(false);
		browserPane.deleteTagAction.setEnabled(false);
		// set menus
		browserPane.photoMenu.setEnabled(false);
		browserPane.tagMenu.setEnabled(false);
		browserPane.addTagMenu.setData(null);
		browserPane.addTagMenuForPopup.setData(null);
		// set components
		browserPane.photoTable.setEnabled(false);
		browserPane.tagTree.setData(null);
		browserPane.tagTree.setEnabled(false);
		// show browser UI
		mainControler.show(browserPane);
		// refresh UI
		refreshTags();
		refreshPhotos();
	}
	
	@EventListener(eventType={Event.Type.TAGS_CHANGED})
	public void refreshTags(){
		try {
			tags = null;
			if(Context.getGallery().isOpen()){
				tags = TagDAO.getInstance().getAllTags();
			}
			browserPane.tagTree.setData(tags);
			browserPane.addTagMenu.setData(tags);
			browserPane.addTagMenuForPopup.setData(tags);
		} catch (PersistenceException e) {
			// TODO notify user
			log.error("Failed to fetch tags", e);
		}
	}
	
	public void photoSelectionChanged(ListSelectionEvent event){
		log.debug("Photo selection changed; index: " + event.getFirstIndex()
				+ ", empty selection: "
				+ browserPane.photoTable.getSelectionModel().isSelectionEmpty());
		boolean enable = true;
		if(browserPane.photoTable.getSelectionModel().isSelectionEmpty()){
			enable = false;
		}
		browserPane.deletePhotoFromGalleryAction.setEnabled(enable);
		browserPane.deletePhotoFromDriveAction.setEnabled(enable);
		browserPane.addTagMenu.setEnabled(enable);
		browserPane.addTagMenuForPopup.setEnabled(enable);
		browserPane.removeTagMenu.setEnabled(enable);
		browserPane.removeTagMenuForPopup.setEnabled(enable);
		
		refreshInfoLabel();
	}

	private void tagSelectionChanged(TreeSelectionEvent event) {
		log.debug("Tag selection changed");
		boolean enable = true;
		if(browserPane.tagTree.getSelectionModel().isSelectionEmpty() ||
			browserPane.tagTree.isRootNodeSelected()){
			enable = false;
		}
		browserPane.renameTagAction.setEnabled(enable);
		browserPane.deleteTagAction.setEnabled(enable);
	}
	
	@EventListener(eventType=Event.Type.OPEN_BROWSER)
	public void openBrowser(OpenBrowserEvent openBrowserEvent){
		Photo photo = openBrowserEvent.getPhoto();
		if(photo != null){
			// FIXME finish jump to the last viewed photo
		}
		mainControler.show(browserPane);
	}
	
}
