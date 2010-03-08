package org.tarsius.imaging;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.imaging.MetadataInspector.Orientation;
import org.tarsius.util.ClipUtil;

import com.mortennobel.imagescaling.ThumpnailRescaleOp;


/**
 * A simple wrapper for frequently used image transforms.
 */
public class ImageOperationUtil {
	
	private static Log log = LogFactory.getLog(ImageOperationUtil.class);
	
	/**
	 * Rotates <code>srcImageFile</code> according to its exif data and scales it to fit in the
	 * rectangle defined by <code>maxWidth</code> and <code>maxHeight</code>. This method is
	 * optimized for thumbnails: it values speed over quality and is best suited to scale a large
	 * image into a small one (less than 300px).
	 * @param srcImageFile
	 * @param maxWidth
	 * @param maxHeight
	 * @return BufferedImage
	 * @throws IOException If srcImageFile does not exist or is not readable.
	 */
	public static BufferedImage makeThumbnail(File srcImageFile, int maxWidth,
			int maxHeight) throws IOException {

		BufferedImage resultImage = null;
		BufferedImage srcImage = ImageIO.read(srcImageFile);
		MetadataInspector mi = new MetadataInspector(srcImageFile);
		
		Dimension clip = ClipUtil.clip(new Dimension(srcImage.getWidth(), srcImage.getHeight()), 
				new Dimension(maxWidth, maxHeight));
		
		ThumpnailRescaleOp resampleOp = new ThumpnailRescaleOp(clip.width, clip.height);
		BufferedImage scaledImage = resampleOp.filter(srcImage, null);
		
		log.trace("Perform rotation if necessary");
		Integer thumbWidth = null;
		Integer thumbHeight = null;
		Orientation orientation = mi.getOrientation();
		AffineTransform transform = new AffineTransform();
		if(orientation == Orientation.TOP_LEFT){
			thumbHeight = clip.height;
			thumbWidth = clip.width;
		} else if(orientation == Orientation.TOP_RIGHT){
			thumbHeight = clip.width;
			thumbWidth = clip.height;
			transform.translate(0, thumbHeight);
			transform.rotate(Math.toRadians(-90.0));
		} else if(orientation == Orientation.LEFT_BOTTOM){
			thumbHeight = clip.width;
			thumbWidth = clip.height;
			transform.translate(0, thumbHeight);
			transform.rotate(Math.toRadians(-90.0));
		} else if(orientation == Orientation.RIGHT_BOTTOM){
			thumbHeight = clip.height;
			thumbWidth = clip.width;
			transform.translate(thumbWidth, thumbHeight);
			transform.rotate(Math.toRadians(180.0));
		} else {
			thumbHeight = clip.height;
			thumbWidth = clip.width;
		}
		// TODO add support for "flipped" orientations
		resultImage = new BufferedImage(thumbWidth, thumbHeight, 
				BufferedImage.TYPE_INT_RGB);
		resultImage.createGraphics().drawImage(scaledImage, transform, null);

		
////		try {
////			// rotate image if necessary
////			ParameterBlock pb = null;
////			Orientation orientation = mi.getOrientation();
////			BufferedImage rotatedImage = null;
////			pb = new ParameterBlock();
////			pb.addSource(srcImage);
////			if(orientation == Orientation.LEFT_BOTTOM){
////				pb.add(TransposeDescriptor.ROTATE_270);
//////				RenderableOp op = JAI.createRenderable("transpose", pb);
//////				RenderedImage image = op.createDefaultRendering();
////				PlanarImage pi = (PlanarImage)JAI.create("transpose", pb);
////				rotatedImage = pi.getAsBufferedImage();
////			} else {
////				rotatedImage = srcImage;
////			}
////			// TODO support other orientations
////
////			// scale image
////			Dimension clip = ClipUtil.clip(new Dimension(rotatedImage
////					.getWidth(), rotatedImage.getHeight()), new Dimension(
////					width, height));
////			float heightScale = clip.height / (float)height;
////			float widthScale = clip.width / (float)width;
////			pb = new ParameterBlock();
////			pb.addSource(srcImage);
////			pb.add(widthScale);
////			pb.add(heightScale);
////			pb.add(0.0f);
////			pb.add(0.0f);
////			pb.add(new InterpolationNearest());
////			RenderingHints antialising = new RenderingHints(
////					RenderingHints.KEY_ANTIALIASING,
////					RenderingHints.VALUE_ANTIALIAS_ON);
////			PlanarImage scaledImage = JAI.create("scale", pb, antialising);
////			resultImage = scaledImage.getAsBufferedImage();
//
//		Dimension srcImageSize = new Dimension(srcImage.getWidth(), 
//				srcImage.getHeight());
//		Dimension frameSize = new Dimension(width, height);
//		try {
//			// get orientation
//			Orientation orientation = mi.getOrientation();
//
//			log.trace("Scale image");
//			// scale image
//			// if image is sideways
//			if (orientation == Orientation.LEFT_BOTTOM
//					|| orientation == Orientation.TOP_RIGHT) {
//				// "rotate" frame for the clip
//				frameSize = new Dimension(height, width);
//			}
//			Dimension clip = ClipUtil.clip(srcImageSize, frameSize);
////			float heightScale = clip.height / (float)srcImageSize.height;
////			float widthScale = clip.width / (float)srcImageSize.width;
//			ParameterBlock pb = new ParameterBlock();
//			pb.addSource(srcImage);
//
//// original
////			pb.add(widthScale);
////			pb.add(heightScale);
////			pb.add(0.0f);
////			pb.add(0.0f);
////			pb.add(new InterpolationNearest());
////			RenderingHints antialising = new RenderingHints(
////					RenderingHints.KEY_ANTIALIASING,
////					RenderingHints.VALUE_ANTIALIAS_ON);
////			PlanarImage scaledImage = JAI.create("scale", pb, antialising);
//
//			
//// using subsample
//			pb.add(clip.height / srcImageSize.getHeight());
//			PlanarImage scaledImage = JAI.create("subsampleaverage", pb/*, antialising*/);
//
////			pb.add(4); //x... integer downsample factor
////			pb.add(4); //y... integer downsample factor
//////			pb.add(new float[] {0.5f}); //A quadrant symmetric filter generated from a Gaussian kernel
//////			pb.add(Interpolation.getInstance(Interpolation.INTERP_NEAREST));
//////			or
//////			pb.add(Interpolation.getInstance(Interpolation.INTERP_BILINEAR));
//////			pb.add(Interpolation.getInstance(Interpolation.INTERP_BICUBIC));
//////			pb.add(Interpolation.getInstance(Interpolation.INTERP_BICUBIC_2));
////			PlanarImage scaledImage = JAI.create("filteredsubsample", pb);
//			
//			log.trace("Rotate image if necessary");
//			// rotate image if necessary
//			if(orientation == null || orientation == Orientation.TOP_LEFT){
//				resultImage = scaledImage.getAsBufferedImage();
//			} else {
//				pb = new ParameterBlock();
//				pb.addSource(scaledImage);
//				TransposeType rotation = null;
//				if(orientation == Orientation.LEFT_BOTTOM){
//					rotation = TransposeDescriptor.ROTATE_270;
//				} else if(orientation == Orientation.TOP_RIGHT){
//					rotation = TransposeDescriptor.ROTATE_90;
//				} else if(orientation == Orientation.RIGHT_BOTTOM){
//					rotation = TransposeDescriptor.ROTATE_180;
//				}
//				pb.add(rotation);
//				PlanarImage rotatedImage = (PlanarImage)JAI.create("transpose", pb);
//				resultImage = rotatedImage.getAsBufferedImage();
//			}

//		} catch (Exception e) {
//			log.error("Unexpected exception", e);
//		}

		return resultImage;
	}

}
