package org.pixelys.gui.component;

import java.awt.Component;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pixelys.Context;
import org.pixelys.bean.Photo;
import org.pixelys.imaging.ThumbnailsFactory;

public class PhotoCellRenderer implements TableCellRenderer {
	
	private static Log log = LogFactory.getLog(PhotoCellRenderer.class);
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		long begin = System.currentTimeMillis();
		log.trace("getTableCellRendererComponent() at (" + row + "," + column + ")");
		Photo photo = null;
		if(value != null && value instanceof Photo){
			photo = (Photo)value;
		} else if (value == null) {
			log.debug("Null value at cell (" + row + "," + column + ")");
			return null;
		} else {
			log.error("Unknown data type, cannot process: " + value.getClass());
			return new JLabel("X", JLabel.CENTER);
		}
		
		PhotoCell photoCell = null;
		Image image = null;
		String path = null;
//		try {
			path = photo.getPathRelToGallery() ?
					Context.getGallery().getPhotosPath() + photo.getPath() :
					photo.getPath();
//			File file = new File(path);
//			bi = ImageIO.read(file);
			image = ThumbnailsFactory.getInstance().getThumbnail(path);
			photoCell = new PhotoCell(image);
			
//		} catch (IOException e) {
//			log.error("Image not found or cannot be accessed [" + 
//					path + "], cause: " + e.getMessage());
//		}
		if(log.isTraceEnabled()){
			log.trace("Time taken to getTableCellRendererComponent(): " 
					+ (System.currentTimeMillis() - begin) + " ms");
		}
		
		return photoCell;
	}

}
