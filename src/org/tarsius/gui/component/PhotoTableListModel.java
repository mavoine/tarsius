package org.tarsius.gui.component;

import java.util.List;
import java.util.Vector;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Photo;


public class PhotoTableListModel implements ListModel {
	
	private static final Log log = LogFactory.getLog(PhotoTableListModel.class);
	
	private Vector<ListDataListener> listeners = new Vector<ListDataListener>();
	private List<Photo> listData = null;
	private PhotoTable photoTable = null;
	
	public PhotoTableListModel(PhotoTable photoTable) {
		this.photoTable = photoTable;
	}
	
	public void addListDataListener(ListDataListener listener) {
		listeners.add(listener);
	}
	
	public void removeListDataListener(ListDataListener listener) {
		listeners.remove(listener);
	}
	
	public Object getElementAt(int index) {
		
		if(this.listData == null){
			throw new ArrayIndexOutOfBoundsException("Index out of bounds, no data is set");
		}
		if(index < 0){
			throw new ArrayIndexOutOfBoundsException("Index < 0");
		}
		if(index > this.listData.size() - 1){
			throw new ArrayIndexOutOfBoundsException("Index > size of data array");
		}
		
		return this.listData.get(index);
	}
	
	public int getSize() {
		return listData != null ? listData.size() : 0;
	}
	
	public void setListData(List<Photo> listData) {
		this.listData = listData;
		ListDataEvent dataChangedEvent = new ListDataEvent(this.photoTable, ListDataEvent.CONTENTS_CHANGED, 0,
				listData.size());
		for(ListDataListener listener : listeners){
			try {
				listener.contentsChanged(dataChangedEvent);
			} catch (Exception ex){
				log.error("Exception raised while notifying data changed listeners", ex);
			}
		}
	}
	
	public int indexOf(Photo photo){
		return this.listData.indexOf(photo);
	}
	
}
