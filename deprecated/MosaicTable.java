package org.pixelys.gui.component;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pixelys.Context;
import org.pixelys.action.Frame;
import org.pixelys.bean.Photo;
import org.pixelys.event.AbstractEvent;
import org.pixelys.event.Event;
import org.pixelys.event.EventBelt;
import org.pixelys.event.EventListener;

public class MosaicTable extends JTable implements EventListener {
	
	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(MosaicTable.class);
	
	private MosaicTableModel mosaicTableModel = null;

	public MosaicTable() {
		log.debug("Construction of MosaicTable");
		// no table header
//		this.setTableHeader(null);
//		this.addColumn(new TableColumn(75, 75, new MosaicCellRenderer(), null));
		this.mosaicTableModel = new MosaicTableModel(3, 3);
		this.setModel(mosaicTableModel);
		// TODO fix cell selection
		this.setRowSelectionAllowed(false);
		this.setColumnSelectionAllowed(false);
		this.setCellSelectionEnabled(true);
		this.setDefaultRenderer(MosaicColumn.class, new MosaicCellRenderer());
		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		EventBelt.getInstance().registerListener(Event.Type.RESULTS_CHANGED, this);

//		List<MosaicTableData> dataList = new ArrayList<MosaicTableData>();
//		dataList.add(new MosaicTableData(Context.getPhotosPath() + "/2006/05/10/rc0005.jpg"));
//		dataList.add(new MosaicTableData(Context.getPhotosPath() + "/2006/05/11/rc0010.jpg"));
//		dataList.add(new MosaicTableData(Context.getPhotosPath() + "/2006/05/12/rc0013.jpg"));
//		dataList.add(new MosaicTableData(Context.getPhotosPath() + "/2006/05/13/rc0014.jpg"));
//		model.setData(dataList);
		
//		for(int i = 0; i < 3; i++){
//			for(int j = 0; j < 3; j++){
//				MosaicTableData data = new MosaicTableData();
//				data.setImagePath(Context.getPhotosPath() + File.separatorChar + "rc0005.jpg");
//				model.setValueAt(data, i, j);
//			}
//		}
	}
	
	@Override
	public void doLayout() {
		super.doLayout();
		setRowHeight(getHeight() / 3);
	}

	public void processEvent(AbstractEvent event) {
		switch (event.getEventType()){
		case RESULTS_CHANGED :
			refreshTable();
			break;
		}
	}

	private void refreshTable(){
		Frame frame = Context.getDisplayManager().getFrame();
		List<MosaicTableData> dataList = new ArrayList<MosaicTableData>();
		for(Photo photo : frame.getPhotos()){
			String path = null;
			if(photo.getPathRelToGallery()){
				path = Context.getPhotosPath() + photo.getPath();
			} else {
				path = photo.getPath();
			}
			dataList.add(new MosaicTableData(path));
		}
		this.mosaicTableModel.setData(dataList);
	}

}
