package org.tarsius.gui.component;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Photo;

public class PhotoCellRenderer implements ListCellRenderer {
	
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(PhotoCellRenderer.class);
	
	public PhotoCellRenderer() {
		log.trace("Constructor called");
	}
	
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		log.trace("getListCellRendererComponent called");
		// FIXME building a new PhotoCell everytime this method is called causes serious display issues
		// solution: build the list of PhotoCell objects once, then make this method fetch the object from
		// a collection, set properties (ex. for selection and focus) and return it  
		Component component = null;
		if(value instanceof Photo){
			Photo photo = (Photo)value;
			PhotoCell cell = new PhotoCell(photo, cellHasFocus, isSelected);
			component = cell;
		}
		return component;
	}
}
