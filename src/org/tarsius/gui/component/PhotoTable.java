package org.tarsius.gui.component;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Photo;
import org.tarsius.gui.action.PhotoSelectionProvider;

public class PhotoTable extends JList implements PhotoSelectionProvider {
	
	private static final long serialVersionUID = 1L;
	
	private static final Log log = LogFactory.getLog(PhotoTable.class);
	
	private PhotoTableListModel listModel = null;
	private PhotoCellRenderer photoCellRenderer = null;
	
	public PhotoTable() {
		log.debug("Building " + PhotoTable.class.getSimpleName());
		this.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		this.photoCellRenderer = new PhotoCellRenderer();
		this.setCellRenderer(this.photoCellRenderer);
		this.setVisibleRowCount(-1);
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.listModel = new PhotoTableListModel(this);
		this.setModel(listModel);
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		int num = width / 260;
		int rem = width % 260;
		this.setFixedCellWidth(260 + (rem / (num > 0 ? num : 1)));
	}
	
	@Override
	public void setListData(Vector<?> listData) {
		List<Photo> tmpList = new ArrayList<Photo>();
		for(Object obj : listData){
			if(obj instanceof Photo){
				tmpList.add((Photo)obj);
			}
		}
		setListData(tmpList);
	}
	
	@Override
	public void setListData(Object[] listData) {
		List<Photo> tmpList = new ArrayList<Photo>();
		for(Object obj : listData){
			if(obj instanceof Photo){
				tmpList.add((Photo)obj);
			}
		}
		setListData(tmpList);
	}

	public void setListData(List<Photo> listData){
		this.listModel.setListData(listData);
		this.photoCellRenderer.prepareCells(listData);
	}
	
	public Photo getPhotoAt(int index){
		return (Photo)this.listModel.getElementAt(index);
	}

	/**
	 * Returns a List containing all the photos currently selected in the table.
	 * If no photos are selected, an empty List is returned.
	 * @return List of selected photos
	 */
	public List<Photo> getSelectedPhotos() {
		List<Photo> selectedPhotos = new ArrayList<Photo>();
		int[] selectedIndices = getSelectedIndices();
		for(int index : selectedIndices){
			selectedPhotos.add((Photo)this.listModel.getElementAt(index));
		}
		return selectedPhotos;
	}
	
	public Photo getPhotoAtLocation(int x, int y){
		int index = this.locationToIndex(new Point(x, y));
		return (Photo)this.listModel.getElementAt(index);
	}
	
	public void addPhotoToSelection(Photo photo){
		int index = listModel.indexOf(photo);
		if(index != -1){
			this.addSelectionInterval(index, index);
		}
	}

}
