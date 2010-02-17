package org.tarsius.gui.component;

import it.tidalwave.image.EditableImage;
import it.tidalwave.image.op.ReadOp;
import it.tidalwave.image.op.RotateOp;
import it.tidalwave.image.render.AnimatedScaleController;
import it.tidalwave.image.render.DragPanningController;
import it.tidalwave.image.render.EditableImageRenderer;
import it.tidalwave.image.render.ScaleController;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.gui.ZoomDescriptor;
import org.tarsius.gui.ZoomLevelChangeListener;
import org.tarsius.gui.ZoomDescriptor.ZoomType;
import org.tarsius.imaging.MetadataInspector;
import org.tarsius.task.BackgroundTask;
import org.tarsius.task.TaskException;

public class PhotoFrame extends JPanel implements ZoomLevelChangeListener,
	ComponentListener {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(PhotoFrame.class);
	
	private EditableImageRenderer imageRenderer = null;
	private ScaleController scaleController = null;
	private DragPanningController panningController = null;
	
	private ZoomDescriptor zoomLevel = null;
	private File imageFile = null;
	
	private boolean isImageSet = false;
	private boolean zoomLevelChangeDelayed = false;

	public PhotoFrame(ZoomDescriptor initialZoomLevel) {

		MigLayout layout = new MigLayout("");
		layout.setColumnConstraints("[grow]");
		layout.setRowConstraints("[grow]");
		this.setLayout(layout);
		
		imageRenderer = new EditableImageRenderer();
		this.add(imageRenderer, "grow");
		
		this.scaleController = new AnimatedScaleController(imageRenderer);
		
		this.panningController = new DragPanningController(imageRenderer);
		this.panningController.setEnabled(true);
		
		// set default zoom level
		this.zoomLevel = initialZoomLevel;
		
		this.addComponentListener(this);

	}
	
	public void setImage(File imageFile){
		this.imageFile = imageFile;
		refresh();
	}
	
	private void refresh(){
		log.debug("Refresh image");
		
		ImageLoader il = new ImageLoader();
		il.execute();
	}
	
	public void changeZoomLevel(ZoomDescriptor zoomDescriptor) {
		this.zoomLevel = zoomDescriptor;
		// if an image is set
		if(isImageSet){
			updateZoom();
		} else {
			// delay update of the controller until an image is loaded
			log.debug("no image is set, waiting to set the new zoom level");
			zoomLevelChangeDelayed = true;
		}

		log.debug("zoom scale: " + this.scaleController.getScale());
	}
	
	private void updateZoom(){
		if(ZoomType.FIT_TO_WINDOW.equals(zoomLevel.getZoomType())){
			this.scaleController.fitToView();
		} else {
			this.scaleController.setScale((double)(zoomLevel.getValue()) / 100d);
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		log.debug("PhotoFrame resized");
		
		if(isImageSet){
			// if the component is resized and the zoom is FIT_TO_WINDOW: refresh
			if(ZoomType.FIT_TO_WINDOW.equals(zoomLevel.getZoomType())){
				if(imageFile != null){
					// force refresh of the view to keep it "fit to window"
					changeZoomLevel(zoomLevel);
				}
			}
			
			imageRenderer.centerImage();
		}
	}
	
	@Override
	public void componentHidden(ComponentEvent e) {
		// nothing to do
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// nothing to do
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// nothing to do
	}

	private class ImageLoader extends BackgroundTask {
		@Override
		protected void executeTask() throws TaskException {
			try {
				EditableImage image = EditableImage.create(new ReadOp(imageFile));

				// rotate image if required
				MetadataInspector mi = new MetadataInspector(imageFile);
				switch(mi.getOrientation()){
				case TOP_LEFT:
					// nothing to do
					break;
				case LEFT_BOTTOM:
					image = image.execute2(new RotateOp(90));
					break;
				case RIGHT_BOTTOM:
					image = image.execute2(new RotateOp(180));
					break;
				case TOP_RIGHT:
					image = image.execute2(new RotateOp(270));
					break;
				}


				imageRenderer.setImage(image);
				
				isImageSet = true;
				
				if(zoomLevelChangeDelayed){
					zoomLevelChangeDelayed = false;
					updateZoom();
				}

				imageRenderer.centerImage();

			} catch (IOException e) {
				log.error("Failed to load image " + imageFile.getAbsolutePath(), e);
			}
		}
	}
}

