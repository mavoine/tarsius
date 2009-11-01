package org.pixelys.gui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pixelys.util.ClipUtil;

public class MosaicCell extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(MosaicCell.class);

	private Image image = null;

	public MosaicCell(Image image) {
		this.image = image;
	}
	
	@Override
	public void doLayout() {
		log.trace("doLayout()");
		super.doLayout();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		log.trace("paintComponent()");
		
		int imageHeight = this.image.getHeight(null);
		int imageWidth = this.image.getWidth(null);

		Dimension clip = ClipUtil.clip(new Dimension(imageWidth, imageHeight), 
				new Dimension(this.getWidth(), this.getHeight()));
		
		int xOffset = (int)clip.getWidth() < this.getWidth() ? 
				(this.getWidth() - (int)clip.getWidth()) / 2 : 0;
		int yOffset = (int)clip.getHeight() < this.getHeight() ? 
				(this.getHeight() - (int)clip.getHeight()) / 2 : 0;
		
		g.drawImage(this.image, xOffset, yOffset, (int)clip.getWidth(), 
				(int)clip.getHeight(), Color.BLACK, null);
	}
	
}
