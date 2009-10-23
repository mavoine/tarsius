package org.tarsius.gui.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.config.Resources;
import org.tarsius.gui.ZoomDescriptor;
import org.tarsius.gui.ZoomLevelChangeListener;
import org.tarsius.gui.ZoomDescriptor.ZoomType;
import org.tarsius.gui.event.ComponentResizeListener;
import org.tarsius.imaging.ImageOperationUtil;
import org.tarsius.task.BackgroundTask;
import org.tarsius.task.TaskException;
import org.tarsius.util.Debounce;

public class PhotoFrame extends JPanel implements ZoomLevelChangeListener {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(PhotoFrame.class);
	
	private JScrollPane photoScrollPane = null;
	private JLabel imageLabel = null;
	private ZoomDescriptor zoomLevel = null;
	private ImageLoader imageLoader = null;
	private File imageFile = null;

	public PhotoFrame(ZoomDescriptor initialZoomLevel) {

		MigLayout layout = new MigLayout("");
		layout.setColumnConstraints("[grow]");
		layout.setRowConstraints("[grow]");
		this.setLayout(layout);
		
		imageLabel = new JLabel(new ImageIcon()){
			private static final long serialVersionUID = 1L;
			// TODO there must be a better way to get a black background...
			@Override
			public void paint(Graphics g) {
				// fill background
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				super.paint(g);
			}
		};
		photoScrollPane = new JScrollPane(imageLabel);
		this.add(photoScrollPane, "grow");
		
		// set default zoom level
		this.zoomLevel = initialZoomLevel;
		
		// if the component is resized and the zoom is FIT_TO_WINDOW: refresh
		this.addComponentListener(new ComponentResizeListener(){
			private Debounce debounce = new Debounce(500, 100){
				@Override
				public void execute() {
					refresh();
				}
			};
			@Override
			public void resized() {
				if(zoomLevel.getZoomType().equals(ZoomType.FIT_TO_WINDOW)){
					debounce.hit();
				}
			}
		});
	}
	
	public void setImage(File imageFile){
		this.imageFile = imageFile;
		refresh();
	}
	
	private void refresh(){
		log.debug("Refresh image");
		// if an imageLoader is already running
		if(imageLoader != null && !imageLoader.isDone()){
			imageLoader.cancel(true);
		}
		imageLoader = new ImageLoader(imageFile, this.imageLabel, this.zoomLevel);
		imageLoader.execute();
	}
	
	public void changeZoomLevel(ZoomDescriptor zoomDescriptor) {
		this.zoomLevel = zoomDescriptor;
		refresh();
	}
	
	private class ImageLoader extends BackgroundTask {
		
		private File imageFile = null;
		private JLabel destinationLabel = null;
		private ZoomDescriptor zoomLevel = null;
		
		public ImageLoader(File imageFile, JLabel destinationLabel,
				ZoomDescriptor zoomLevel) {
			this.imageFile = imageFile;
			this.destinationLabel = destinationLabel;
			this.zoomLevel = zoomLevel;
		}
		
		@Override
		protected void executeTask() throws TaskException {
			try {
				executeTaskPrivate();
			} catch (Exception ex){
				log.error("Loading image failed", ex);
			}
		}
		
		private void executeTaskPrivate(){
			if(this.imageFile == null){
				return;
			}
			log.debug("Loading image: " + this.imageFile.getName());
			ImageIcon icon = null;
			Integer width = null;
			Integer height = null;
			if(this.zoomLevel.getZoomType() == ZoomType.FIT_TO_WINDOW){
				// image will be resized to fit in the available space
				width = this.destinationLabel.getParent().getWidth();
				height = this.destinationLabel.getParent().getHeight();
			} else if(this.zoomLevel.getZoomType() == ZoomType.PERCENTAGE) {
				log.trace("Calculating size required using the percentage");
				// image will be resized to a percentage defined by the zoom level
				BufferedImage srcImage = null;
				try {
					log.trace("Reading image");
					srcImage = ImageIO.read(this.imageFile);
					// calculate the smallest square into which the percentage of 
					// the image would fit, regardless of the ratio
					log.trace("Performing calculation");
					width = (int) (Math.max(srcImage.getWidth(), srcImage
							.getHeight()) * (this.zoomLevel.getValue() / 100.0));
					height = width;
				} catch (IOException e) {
					log.warn("Failed to read image " + this.imageFile.getPath());
					icon = new ImageIcon(Resources.getInstance().getNotFoundIcon());
					return;
				}
			} else {
				log.warn("Zoom level not supported: " + this.zoomLevel.getZoomType());
				// fallback to "fit to window"
				width = this.destinationLabel.getWidth();
				height = this.destinationLabel.getHeight();
			}
			try {
				log.trace("Creating the properly sized and oriented image");
				BufferedImage resizedImage = ImageOperationUtil.makeThumbnail(
						this.imageFile, width, height);
				log.trace("Image ready, setting the icon");
				icon = new ImageIcon(resizedImage);
			} catch (IOException e) {
				log.warn("Failed to read image: " + this.imageFile.getPath(), e);
				icon = new ImageIcon(Resources.getInstance().getNotFoundIcon());
			}
			this.destinationLabel.setIcon(icon);
			log.debug("Loading image done");
		}
		
		@Override
		protected void done() {
			if(this.isCancelled()){
				log.debug("ImageLoader was cancelled");
			}
		}
		
		@Override
		public String toString() {
			return ImageLoader.class.getName() + " [" + this.imageFile != null ? 
					this.imageFile.getName() : "null" + "]";
		}
	}

}
