package org.tarsius;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.ImageCapabilities;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.imaging.ImageOperationUtil;
import org.tarsius.imaging.MetadataInspector;
import org.tarsius.imaging.MetadataInspector.Orientation;
import org.tarsius.util.ClipUtil;

import com.mortennobel.imagescaling.ThumpnailRescaleOp;

public class ThumbnailTest {
	
	private static Log log = LogFactory.getLog(ThumbnailTest.class);

	JFrame frame = null;
	JButton button = new JButton("Go");
	JLabel label = new JLabel();
	ImageIcon icon = new ImageIcon();
	JLabel labelJAI = new JLabel();
	ImageIcon iconJAI = new ImageIcon();
	JLabel labelGimp = new JLabel();
	ImageIcon iconGimp = new ImageIcon("/home/math/test/IMG_1810_small_gimp.JPG");

	public static void main(String[] args) {
		ThumbnailTest t = new ThumbnailTest();
		t.run();
	}
	
	public void run(){
		BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		icon.setImage(image);
		label.setIcon(icon);
		label.setText("custom");
		label.setVerticalTextPosition(JLabel.BOTTOM);
		label.setHorizontalTextPosition(JLabel.CENTER);
		iconJAI.setImage(image);
		labelJAI.setIcon(iconJAI);
		labelJAI.setText("JAI");
		labelJAI.setVerticalTextPosition(JLabel.BOTTOM);
		labelJAI.setHorizontalTextPosition(JLabel.CENTER);
		labelGimp.setIcon(iconGimp);
		labelGimp.setText("Gimp");
		labelGimp.setVerticalTextPosition(JLabel.BOTTOM);
		labelGimp.setHorizontalTextPosition(JLabel.CENTER);
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		frame.add(button);
		frame.add(labelGimp);
		frame.add(label);
		frame.add(labelJAI);
		frame.pack();
		
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				go();
			}
		});
		
		frame.setVisible(true);
	}
	
	public void go(){
		log.debug("go");
		File imageFile = new File("/home/math/test/IMG_1810.JPG");

		long time = now();

//		try {
//			MagickImage image = new MagickImage(new ImageInfo(imageFile.getAbsolutePath()));
//			MagickImage smaller = image.scaleImage(256, 256);
//			ImageInfo ii = new ImageInfo("/home/math/imagemagick.png");
//			smaller.writeImage(ii);
//		} catch (Exception ex){
//			log.debug("oops", ex);
//		}
//		log.debug("total time taken: " + (now() - time));
		
		time = now();
		BufferedImage thumb = prepareThumbnail(imageFile);
		log.debug("total time taken: " + (now() - time));
		icon.setImage(thumb);
		label.setIcon(icon);
		label.repaint();

		time = now();
		BufferedImage thumbJAI = prepareThumbnailJAI(imageFile);
		log.debug("total time taken: " + (now() - time));
		iconJAI.setImage(thumbJAI);
		labelJAI.setIcon(iconJAI);
		labelJAI.repaint();
		frame.pack();
	}
	
	private BufferedImage drawImage(BufferedImage image, int width, int height){
		BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = scaledImage.createGraphics();
		g2.getRenderingHints().clear();
//		g2.getRenderingHints().add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		g2.getRenderingHints().add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
		g2.getRenderingHints().add(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR));
//		g2.getRenderingHints().add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
//		g2.getRenderingHints().add(new RenderingHints(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE));
//		g2.getRenderingHints().add(new RenderingHints(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY));
		g2.drawImage(image, 0, 0, scaledImage.getWidth(), scaledImage.getHeight(), null);
		g2.dispose();
		return scaledImage;
	}
	
	private BufferedImage prepareThumbnail(File originalFile){
		log.trace("");
		log.trace("-------------------------");
		log.trace("Begin preparing thumbnail");
		
		long time = System.currentTimeMillis();
		BufferedImage thumbnailImage = null;
		MetadataInspector mi = new MetadataInspector(originalFile);
		log.debug("reading meta: " + (now() - time));
		
		try {
			BufferedImage originalImage = ImageIO.read(originalFile);
			Integer width = originalImage.getWidth();
			Integer height = originalImage.getHeight();

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gd = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gd.getDefaultConfiguration();

			log.debug("priority: " + originalImage.getAccelerationPriority());
			ImageCapabilities ic = originalImage.getCapabilities(gc);
			log.debug("accelerated?: " + ic.isAccelerated());

			time = now();
			log.trace("Calculate clipping");
			Dimension clip = ClipUtil.clip(new Dimension(width, height), 
					new Dimension(getThumbnailMaxWidth(), getThumbnailMaxHeight()));
			log.debug("calculate clip: " + (now() - time));

			time = now();
			log.trace("Make a scaled-down copy of the image");
//			BufferedImage scaledImage = new BufferedImage(clip.width, clip.height, BufferedImage.TYPE_INT_RGB);
//			ImageCapabilities imgCap = scaledImage.getCapabilities(gc);
//			Graphics2D g2 = scaledImage.createGraphics();
//			RenderingHints rh = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//			g2.setRenderingHints(rh);
//			g2.drawImage(originalImage, 0, 0, scaledImage.getWidth(), scaledImage.getHeight(), null);
//			g2.dispose();
			// TODO try multi-step scaling

//			Dimension clipStep1 = ClipUtil.clip(new Dimension(width, height), new Dimension(500, 500));
//			BufferedImage scaledImage = new BufferedImage(clipStep1.width, clipStep1.height, BufferedImage.TYPE_INT_RGB);
//			Graphics2D g2 = scaledImage.createGraphics();
//			g2.getRenderingHints().clear();
//			g2.getRenderingHints().add(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR));
//			g2.getRenderingHints().add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
//			g2.drawImage(originalImage, 0, 0, scaledImage.getWidth(), scaledImage.getHeight(), null);
//			g2.dispose();
//			BufferedImage scaledImage2 = new BufferedImage(clip.width, clip.height, BufferedImage.TYPE_INT_RGB);
//			Graphics2D g2a = scaledImage2.createGraphics();
//			g2a.getRenderingHints().clear();
//			g2a.getRenderingHints().add(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR));
//			g2a.getRenderingHints().add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
//			g2a.drawImage(scaledImage, 0, 0, scaledImage2.getWidth(), scaledImage2.getHeight(), null);
//			g2a.dispose();
//			scaledImage = scaledImage2;

//			BufferedImage stepImage = originalImage;
//			while(stepImage.getWidth() / 2 > clip.getWidth()){
//				Dimension clip2 = ClipUtil.clip(new Dimension(stepImage.getWidth(), stepImage.getHeight()), new Dimension(stepImage.getWidth() / 2, stepImage.getHeight() / 2));
//				log.debug("scale to " + clip2);
////				BufferedImage scaledImage = new BufferedImage(clip2.width, clip2.height, BufferedImage.TYPE_INT_RGB);
////				Graphics2D g2 = scaledImage.createGraphics();
////				g2.getRenderingHints().clear();
////				g2.getRenderingHints().add(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR));
////				g2.getRenderingHints().add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
////				g2.drawImage(tmpImage, 0, 0, scaledImage.getWidth(), scaledImage.getHeight(), null);
////				g2.dispose();
////				tmpImage = scaledImage;
//				stepImage = drawImage(stepImage, clip2.width, clip2.height);
//			}
//			log.debug("final scale to " + clip);
//			//BufferedImage scaledImage = drawImage(stepImage, clip.width, clip.height);
//			ResampleOp resampleOp = new ResampleOp (clip.width, clip.height);
//			ThumpnailRescaleOp resampleOp = new ThumpnailRescaleOp(clip.width, clip.height);
//			BufferedImage scaledImage = resampleOp.filter(stepImage, null);
			
			
			//ResampleOp resampleOp =
			ThumpnailRescaleOp resampleOp = new ThumpnailRescaleOp(clip.width, clip.height);
			BufferedImage scaledImage = resampleOp.filter(originalImage, null);
			
			
			log.debug("scale: " + (now() - time));

			time = now();
			log.trace("Perform rotation if necessary");
			Integer thumbWidth = null;
			Integer thumbHeight = null;
			Orientation orientation = mi.getOrientation();
			AffineTransform transform = new AffineTransform();
			if(orientation == Orientation.TOP_LEFT){
				thumbHeight = clip.height;
				thumbWidth = clip.width;
			} else if(orientation == Orientation.LEFT_BOTTOM){
				thumbHeight = clip.width;
				thumbWidth = clip.height;
				transform.translate(0, thumbHeight);
				transform.rotate(Math.toRadians(-90.0));
			} else {
				thumbHeight = clip.height;
				thumbWidth = clip.width;
			}
			thumbnailImage = new BufferedImage(thumbWidth, thumbHeight, 
					BufferedImage.TYPE_INT_RGB);
			thumbnailImage.createGraphics().drawImage(scaledImage, transform, null);
			log.debug("rotate: " + (now() - time));
			
			
//			time = now();
//			log.trace("Make a scaled-down copy of the image");
//			Image scaledImage = //originalImage;
//				originalImage.getScaledInstance(clip.width, clip.height, Image.SCALE_SMOOTH);
//			log.debug("scale: " + (now() - time));
//
//			time = now();
//			log.trace("Perform rotation if necessary");
//			Integer thumbWidth = null;
//			Integer thumbHeight = null;
//			Orientation orientation = mi.getOrientation();
//			AffineTransform transform = new AffineTransform();
//			if(orientation == Orientation.TOP_LEFT){
//				thumbHeight = clip.height;
//				thumbWidth = clip.width;
//			} else if(orientation == Orientation.LEFT_BOTTOM){
//				thumbHeight = clip.width;
//				thumbWidth = clip.height;
//				transform.translate(0, thumbHeight);
//				transform.rotate(Math.toRadians(-90.0));
//			} else {
//				thumbHeight = clip.height;
//				thumbWidth = clip.width;
//			}
//			thumbnailImage = new BufferedImage(thumbWidth, thumbHeight, 
//					BufferedImage.TYPE_INT_RGB);
//			thumbnailImage.createGraphics().drawImage(scaledImage, transform, null);
//			log.debug("rotate: " + (now() - time));

			log.trace("Done preparing thumbnail");

		} catch (IOException e) {
			log.error("Unable to read image", e);
			// TODO manage exception
		} catch (Exception e) {
			log.error("Unexpected exception", e);
		}
		return thumbnailImage;
	}
	
	private BufferedImage prepareThumbnailJAI(File originalFile){
		log.trace("-----------------------------");
		log.trace("Begin preparing thumbnail JAI");
		
		BufferedImage thumbnailImage = null;
		
		try {
			thumbnailImage = ImageOperationUtil.makeThumbnail(originalFile,
					getThumbnailMaxWidth(), getThumbnailMaxHeight());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		BufferedImage originalImage = null;
//		long time = now();
//		MetadataInspector mi = new MetadataInspector(originalFile);
//		log.debug("reading meta: " + (now() - time));
//		
//		try {
//			originalImage = ImageIO.read(originalFile);
//			Integer width = originalImage.getWidth();
//			Integer height = originalImage.getHeight();
//
//			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//			GraphicsDevice gd = ge.getDefaultScreenDevice();
//			GraphicsConfiguration gc = gd.getDefaultConfiguration();
//
//			log.debug("priority: " + originalImage.getAccelerationPriority());
//			ImageCapabilities ic = originalImage.getCapabilities(gc);
//			log.debug("accelerated?: " + ic.isAccelerated());
//
//			Dimension clip = ClipUtil.clip(new Dimension(width, height), 
//					new Dimension(getThumbnailMaxWidth(), getThumbnailMaxHeight()));
//			
//			time = now();
//			log.trace("Make a scaled-down copy of the image");
//			float heightScale = clip.height / (float)height;
//			float widthScale = clip.width / (float)width;
//			ParameterBlock pb = new ParameterBlock();
//			pb.addSource(originalImage);
//			pb.add(widthScale);
//			pb.add(heightScale);
//			pb.add(0.0f);
//			pb.add(0.0f);
//			pb.add(new InterpolationBilinear());
//			RenderingHints antialising = new RenderingHints(
////					RenderingHints.KEY_ANTIALIASING,
////					RenderingHints.VALUE_ANTIALIAS_ON);
//					RenderingHints.KEY_RENDERING,
//					RenderingHints.VALUE_RENDER_QUALITY);
////			pb.add(antialising);
//			PlanarImage scaledImage = JAI.create("scale", pb, antialising);
//			log.debug("scale: " + (now() - time) + " w: "
//					+ scaledImage.getWidth() + ", h: "
//					+ scaledImage.getHeight());
//
//			time = now();
//			log.trace("Perform rotation if necessary");
//			Orientation orientation = mi.getOrientation();
//			pb = new ParameterBlock();
//			pb.addSource(scaledImage);
//			if(orientation == Orientation.LEFT_BOTTOM){
//				pb.add(TransposeDescriptor.ROTATE_270);
//				PlanarImage pi = (PlanarImage)JAI.create("transpose", pb);
//				thumbnailImage = pi.getAsBufferedImage();
//			} else {
//				thumbnailImage = scaledImage.getAsBufferedImage();
//			}
//			// TODO support other orientations
//			log.debug("rotate: " + (now() - time));
//
//			log.trace("Done preparing thumbnail");
//
//		} catch (IOException e) {
//			log.error("Unable to read image", e);
//			// TODO manage exception
//		} catch (Exception e) {
//			log.error("Unexpected exception", e);
//		}
		return thumbnailImage;
//		return originalImage;
	}

	private int getThumbnailMaxHeight() {
		return 256;
	}

	private int getThumbnailMaxWidth() {
		return 256;
	}
	
	private long now(){
		return System.currentTimeMillis();
	}

}
