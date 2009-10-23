package org.tarsius.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.i18n.I18n;
import org.tarsius.imaging.ImageOperationUtil;
import org.tarsius.util.ClipUtil;

public class PhotoViewForm_bk extends JDialog {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(PhotoViewForm_bk.class);
	
	protected JPanel infoPanel = null;
	protected JPanel photoPanel = null;
	protected JLabel fileNameLabel = null;
	protected JLabel dateLabel = null;
	protected ImageCanvas imageCanvas = null;
	
	public PhotoViewForm_bk() {
		
		// set layout
		MigLayout frameLayout = new MigLayout(
				"",              // layout constraints
				"[][grow]",      // column constraints
				"[grow]");             // row constraints
		this.setLayout(frameLayout);
		
		// create components
		
		// info panel
		MigLayout infoPanelLayout = new MigLayout();
		infoPanelLayout.setLayoutConstraints("wrap 1");
		infoPanelLayout.setColumnConstraints("");
		infoPanelLayout.setRowConstraints("");
		JLabel infoTitle = new JLabel();
		infoTitle.setText(I18n.translate("Info"));
		infoPanel = new JPanel(infoPanelLayout);
		fileNameLabel = new JLabel("");
		dateLabel = new JLabel("");
		infoPanel.add(infoTitle, "");
		infoPanel.add(fileNameLabel, "");
		infoPanel.add(dateLabel, "");
		
		// photo panel
		MigLayout photoPanelLayout = new MigLayout("debug");
		photoPanelLayout.setColumnConstraints("[grow]");
		photoPanelLayout.setRowConstraints("[grow]");
		photoPanel = new JPanel(photoPanelLayout);
		imageCanvas = new ImageCanvas();
		photoPanel.add(imageCanvas, "grow");

		// assemble the dialog
		this.add(infoPanel, "grow");
		this.add(photoPanel, "grow");

		this.setMinimumSize(new Dimension(400, 300));
		
		this.pack();
	}
	
//	public void setImage(Image image){
//		this.imageCanvas.image = image;
//	}
	
	public void setImage(File imageFile){
		this.imageCanvas.photoFile = imageFile;
	}
	
	private class ImageCanvas extends JComponent {
		
		private static final long serialVersionUID = 1L;

//		private Image image = null;
		private File photoFile = null;
//		private VolatileImage backBuffer = null;
//		private BufferStrategy strategy = null;
		
		public ImageCanvas() {
			super();
			createBufferStrategy(2);
		}
		
		@Override
		public void paint(Graphics g) {
			if(photoFile == null){
				super.paint(g);
				return;
			}
						
//			if (backBuffer == null || bufferSizeDiffer()) {
//				backBuffer = this.createVolatileImage(this.getWidth(), this
//						.getHeight());
//				Painter painter = new Painter();
//				painter.setImage(photoFile);
//				painter.setBackBuffer(backBuffer);
//				painter.execute();
//			} else {
//				this.getGraphics().drawImage(backBuffer, 0, 0, null);
//			}
			
			// TODO explore usage of DisplayJAI
			
//			Dimension imageDimension = new Dimension(image.getWidth(null),
//					image.getHeight(null));
//			Dimension canvasDim = new Dimension(this.getWidth(), this
//					.getHeight());
//			Dimension clip = ClipUtil.clip(imageDimension, canvasDim);
//			int xOffset = (int)(this.getWidth() - clip.getWidth()) / 2;
//			int yOffset = (int)(this.getHeight() - clip.getHeight()) / 2;
//			g.drawImage(image, xOffset, yOffset, (int) clip.getWidth(),
//					(int) clip.getHeight(), null);

			
//			BufferedImage image = null;
//			try {
//				image = ImageOperationUtil.rotateAndScale(
//						this.photoFile, this.getWidth(), this.getHeight());
//			} catch (IOException e) {
//				// FIXME render default "not found" image
//				return;
//			}
//
//			int xOffset = (int)(this.getWidth() - image.getWidth()) / 2;
//			int yOffset = (int)(this.getHeight() - image.getHeight()) / 2;
//			g.drawImage(image, xOffset, yOffset, image.getWidth(),
//					image.getHeight(), null);
			
			
			
			
		}
		
//		private boolean bufferSizeDiffer() {
//			return backBuffer != null
//					&& backBuffer.getWidth() == this.getWidth()
//					&& backBuffer.getHeight() == this.getHeight();
//		}
		
	}
	
	private class Painter extends SwingWorker {
		private File imageFile = null;
//		private VolatileImage backBuffer = null;
		private BufferStrategy strategy = null;
		private int width = 0;
		private int height = 0;
		@Override
		protected Object doInBackground() throws Exception {
			log.debug("Painter execution");
//			BufferedImage image = ImageOperationUtil.rotateAndScale(imageFile,
//					backBuffer.getWidth(), backBuffer.getHeight());
			BufferedImage image = ImageOperationUtil.makeThumbnail(imageFile,
					width, height);

			Dimension imageDimension = new Dimension(image.getWidth(null),
			image.getHeight(null));
			Dimension canvasDim = new Dimension(width, height);
			Dimension clip = ClipUtil.clip(imageDimension, canvasDim);
			int xOffset = (int)(width - clip.getWidth()) / 2;
			int yOffset = (int)(height - clip.getHeight()) / 2;
			
			strategy.getDrawGraphics().drawImage(image, xOffset, yOffset, (int) clip.getWidth(),
					(int) clip.getHeight(), null);
			strategy.show();
			
			return null;
		}
		public void setImage(File imageFile){
			this.imageFile = imageFile;
		}
		public void setStrategy(BufferStrategy strategy){
			this.strategy = strategy;
		}
		public void setSize(int width, int height){
			this.width = width;
			this.height = height;
		}
//		public void setBackBuffer(VolatileImage backBuffer){
//			this.backBuffer = backBuffer;
//		}
	};



}
