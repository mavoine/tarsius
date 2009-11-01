package org.pixelys.gui.component;

public class MosaicTableData {
	
	private String imagePath = null;
	
	public MosaicTableData(String imagePath) {
		this.imagePath = imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public String getImagePath() {
		return imagePath;
	}

}
