package org.tarsius.gui.component;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Photo;
import org.tarsius.gui.Pager;
import org.tarsius.util.Debounce;


public class PhotoTableController implements ComponentListener {
	
	private static Log log = LogFactory.getLog(PhotoTableController.class);
	
	private PhotoTable photoTable;
	private List<Photo> photos;
	private Debounce debounce;
	private Pager pager;
	
	public PhotoTableController() {
		pager = new Pager();
		debounce = new Debounce(0, 1000) {
			@Override
			public void execute() {
				adjustDisplay();
			}
		};
	}
	
	public void setPhotoTable(PhotoTable photoTable){
		if(this.photoTable != null){
			this.photoTable.removeComponentListener(this);
		}
		this.photoTable = photoTable;
		this.photoTable.addComponentListener(this);
	}
	
	public void setPhotos(List<Photo> photos){
		this.photos = photos;
		if(this.photos != null){
			pager.setTotalSize(this.photos.size());
		} else {
			pager.setPageSize(0);
		}
		
		if(this.photoTable.isVisible()){
			debounce.hit();
			//adjustDisplay();
		}
	}
	
	private void adjustDisplay(){
		Dimension tableDimension = this.photoTable.getSize();

		// 280 x 315
		
		int numVertical = tableDimension.height / 280;
		int numHorizontal = tableDimension.width / 280;
		int pageSize = (numHorizontal * numVertical);
		
		if(pageSize == 0){
			// the table is too small, defer rendering
			debounce.hit();
			return;
		}
		
		int cellWidth = tableDimension.width / numHorizontal;
		int cellHeight = tableDimension.height / numVertical;
//		Dimension cellDimension = new Dimension(cellWidth, cellHeight);
		
		// set cell size to display the maximum possible thumbnails
//		JComponent prototype = new JLabel();
//		prototype.setFont(new Font("Courier New", Font.ITALIC, 12)); // set a default font
//		prototype.setPreferredSize(cellDimension);
//		this.photoTable.setPrototypeCellValue(prototype);
		this.photoTable.setFixedCellHeight(cellHeight);
		this.photoTable.setFixedCellWidth(cellWidth);
		
		this.pager.setPageSize(pageSize);
		
		List<Photo> displaybleList = null;
		if(this.photos != null){
			displaybleList = this.photos.subList(pager.getFirstElementIndex(), pager.getLastElementIndex());
//			if(numTotal >= this.photos.size()){
//				displaybleList = this.photos;
//			} else {
//				displaybleList = this.photos.subList(0,  (numTotal > 0) ? numTotal : 0);
//			}
			this.photoTable.setListData(displaybleList);
		}
		
		this.photoTable.repaint();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		log.debug("table hidden");
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		log.debug("table moved");
	}

	@Override
	public void componentResized(ComponentEvent e) {
		log.debug("table resized");
		//adjustDisplay();
		debounce.hit();
	}

	@Override
	public void componentShown(ComponentEvent e) {
		log.debug("table shown");
		//adjustDisplay();
		debounce.hit();
	}

}
