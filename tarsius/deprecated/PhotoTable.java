package org.pixelys.gui.component;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JTable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pixelys.Context;
import org.pixelys.bean.Photo;

public class PhotoTable extends JTable {
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(PhotoTable.class);
	
	private PhotoTableModel photoTableModel = null;
	
	public PhotoTable() {
		log.debug("Construction of PhotoTable");
		// no table header
		this.setTableHeader(null);
//		this.addColumn(new TableColumn(75, 75, new MosaicCellRenderer(), null));
		this.photoTableModel = new PhotoTableModel(3);//mosaicTableModel = new MosaicTableModel(3, 3);
		this.setModel(this.photoTableModel);
		// TODO fix cell selection
		this.setRowSelectionAllowed(false);
		this.setColumnSelectionAllowed(false);
		this.setCellSelectionEnabled(true);
		this.setDefaultRenderer(PhotoColumn.class, new PhotoCellRenderer());
		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	}
	
	@Override
	public void setSize(Dimension d) {
		super.setSize(d);
		// set row height equal to the width of a column
		this.setRowHeight(d.width / this.photoTableModel.getColumnCount());
	}

	public void refreshTable(){
		List<Photo> results = Context.getDisplayManager().getResults();
		this.photoTableModel.setData(results);
	}

}
