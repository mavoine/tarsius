package org.pixelys.gui.component;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MosaicTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(MosaicTableModel.class);
	
	private int rowCount = 3;
	private int columnCount = 3;
	private MosaicTableData[][] data = null;

	public MosaicTableModel(int rowCount, int columnCount) {
		if(rowCount < 1){
			throw new RuntimeException("The number of rows cannot be negative");
		}
		if(columnCount < 1){
			throw new RuntimeException("The number of columns cannot be negative");
		}
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.data = new MosaicTableData[rowCount][columnCount];
	}

	public int getColumnCount() {
		return rowCount;
	}

	public int getRowCount() {
		return columnCount;
	}
	
	public int getPageSize() {
		return rowCount * columnCount;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return this.data[rowIndex][columnIndex];
	}
	
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if(value == null || value instanceof MosaicTableData){
			this.data[rowIndex][columnIndex] = (MosaicTableData)value;
		} else {
			log.error("Unable to handle data type " + value.getClass());
		}
		fireTableCellUpdated(rowIndex, columnIndex);
	}
	
	public void setData(List<MosaicTableData> data) {
		int index = 0;
		for(int rowIndex = 0; rowIndex < this.rowCount; rowIndex++){
			for(int columnIndex = 0; columnIndex < this.columnCount; columnIndex++){
				if(index >= data.size()){
					this.data[rowIndex][columnIndex] = null;
				} else {
					this.data[rowIndex][columnIndex] = data.get(index);
				}
				index++;
			}
		}
		fireTableDataChanged();
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return MosaicColumn.class;
	}
	
}
