package org.tarsius.gui.component;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import org.tarsius.bean.Photo;
import org.tarsius.gui.action.PhotoSelectionProvider;

public class PhotoTable extends JList implements PhotoSelectionProvider {
	
	private static final long serialVersionUID = 1L;
	
	private Vector<?> listData = null;
	
	public PhotoTable() {
		this.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		this.setCellRenderer(new PhotoCellRenderer());
		this.setVisibleRowCount(-1);
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
		super.setListData(listData);
		this.listData = listData;
	}
	
	@Override
	public void setListData(Object[] listData) {
		Vector<Object> v = new Vector<Object>();
		for(Object object : listData){
			v.add(object);
		}
		setListData(v);
	}
	
	public void setListData(List<Photo> listData){
		if(listData != null){
			setListData(listData.toArray());
		} else {
			setListData(new Photo[]{});
		}
	}
	
	public Photo getPhotoAt(int index){
		return (Photo)listData.get(index);
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
			selectedPhotos.add((Photo)listData.get(index));
		}
		return selectedPhotos;
	}
	
	public Photo getPhotoAtLocation(int x, int y){
		int index = this.locationToIndex(new Point(x, y));
		return (Photo)listData.get(index);
	}
	
	public void addPhotoToSelection(Photo photo){
		int index = listData.indexOf(photo);
		if(index != -1){
			this.addSelectionInterval(index, index);
		}
	}

}
