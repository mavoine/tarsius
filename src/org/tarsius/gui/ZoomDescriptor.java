package org.tarsius.gui;

public class ZoomDescriptor {

	public static enum ZoomType {FIT_TO_WINDOW, PERCENTAGE}
	
	private ZoomType zoomType = null;
	private Integer value = null;

	public ZoomDescriptor(ZoomType zoomType, Integer value) {
		this.zoomType = zoomType;
		this.value = value;
	}
	
	public ZoomType getZoomType() {
		return zoomType;
	}
	
	public Integer getValue() {
		return value;
	}
	
	public void setZoomType(ZoomType zoomType) {
		this.zoomType = zoomType;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "ZoomDescriptor [type=" + this.zoomType + ", value="
				+ this.value + "]";
	}
	
}
