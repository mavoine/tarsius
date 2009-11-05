package org.tarsius.gui.component;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Photo;

public class PhotoCellRenderer implements ListCellRenderer {
	
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(PhotoCellRenderer.class);
	
	private List<PhotoCell> photoCells = null;
	
	public PhotoCellRenderer() {
		log.trace("Constructor called");
	}
	
	public void prepareCells(List<Photo> listData){
		photoCells = new ArrayList<PhotoCell>();
		for(Photo photo : listData){
			PhotoCell cell = new PhotoCell(photo);
			photoCells.add(cell);
		}
	}
	
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		log.trace("getListCellRendererComponent called");
		Component component = null;
		if(value instanceof Photo && index < photoCells.size()){
			PhotoCell cell = photoCells.get(index);
			cell.setSelected(isSelected);
			cell.setHasFocus(cellHasFocus);
			component = cell;
		}
		return component;
	}
}
