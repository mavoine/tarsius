package org.pixelys.gui.component;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MosaicCellRenderer implements TableCellRenderer {
	
	private static Log log = LogFactory.getLog(MosaicCellRenderer.class);

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		MosaicTableData data = null;
		if(value != null && value instanceof MosaicTableData){
			data = (MosaicTableData)value;
		} else {
			return new JLabel("X", JLabel.CENTER);
		}
		
		// table.getVisibleRect()
		MosaicCell mosaicCellPanel = null;
//		JPanel mosaicCellPanel = new JPanel();
		BufferedImage bi = null;
//		ImageIcon icon = null;
		try {
			File file = new File(data.getImagePath());
			bi = ImageIO.read(file);
//			icon = new ImageIcon(bi);
			
			mosaicCellPanel = new MosaicCell(bi);
//			mosaicCellPanel.add(new JLabel(icon, JLabel.CENTER));
			
//			Graphics g = table.getGraphics();//panel.getGraphics();
//			Graphics2D g2d = (Graphics2D)g;
//			AffineTransform at = g2d.getTransform();
//			AffineTransform atScale = new AffineTransform();
//			atScale.concatenate(at);
//			atScale.scale(0.10, 0.10);
//			g2d.setTransform(atScale);
			
		} catch (IOException e) {
			log.error("Image not found or cannot be accessed [" + 
					data.getImagePath() + "], cause: " + e.getMessage());
		}
		
		return mosaicCellPanel;
	}

}
