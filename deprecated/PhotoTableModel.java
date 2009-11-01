package org.pixelys.gui.component;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pixelys.bean.Photo;
import org.pixelys.util.TableUtil;

public class PhotoTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(PhotoTableModel.class);
	
	private int columnCount = 3; // default value
	private List<Photo> data = null;

	public PhotoTableModel(int columnCount) {
		if(columnCount < 1){
			throw new RuntimeException("The number of columns cannot be negative");
		}
		this.columnCount = columnCount;
		this.data = new ArrayList<Photo>();
	}

	public int getColumnCount() {
		return columnCount;
	}

	public int getRowCount() {
		if(this.data == null || this.data.size() == 0){
			return 0;
		}
		return this.data.size() / this.columnCount;
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		int index = TableUtil.tableToListIndex(this.columnCount, 
				rowIndex, columnIndex); 
		return this.data.get(index);
	}
	
	public void setData(List<Photo> data) {
		this.data = data;
		fireTableDataChanged();
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return PhotoColumn.class;
	}
	
}
