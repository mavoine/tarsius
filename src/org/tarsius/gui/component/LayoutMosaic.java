package org.tarsius.gui.component;

import javax.swing.JComponent;


public class LayoutMosaic {

	private static int CELL_WIDTH = 280;
	private static int CELL_HEIGHT = 315;

	private JComponent containerComponent;
	
	public LayoutMosaic() {
	}
	
	public void setContainer(JComponent containerComponent){
		this.containerComponent = containerComponent;
	}
	
	public int getPageSize(){
		int numVertical = containerComponent.getSize().height / CELL_HEIGHT;
		int numHorizontal = containerComponent.getSize().width / CELL_WIDTH;
		int numTotal = (numHorizontal * numVertical);
		return numTotal;
	}

}
