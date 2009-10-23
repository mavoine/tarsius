package org.tarsius.util;

import java.awt.Dimension;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClipUtil {
	
	private static Log log = LogFactory.getLog(ClipUtil.class);
	
	/**
	 * Calculates and returns the dimension to which frame1 must be clipped
	 * to fit inside frame2.
	 * @param frame1
	 * @param frame2
	 * @return Dimension of the clipped frame.
	 */
	public static Dimension clip (Dimension frame1, Dimension frame2){

		double frame1Height = frame1.getHeight();
		double frame1Width = frame1.getWidth();
		double frame2Height = frame2.getHeight();
		double frame2Width = frame2.getWidth();
		double clipHeight = 0;
		double clipWidth = 0;
		
		log.trace("x: " + frame1Width + ", y:" + frame1Height);
		
		if((frame1Height / frame2Height) > (frame1Width / frame2Width)){
			// width is the limitation 
			clipHeight = frame2Height;
			clipWidth = frame1Width / (frame1Height / frame2Height);
		} else {
			// height is the limitation 
			clipWidth = frame2Width;
			clipHeight = frame1Height / (frame1Width / frame2Width);
		}
		
		return new Dimension((int)clipWidth, (int)clipHeight);
	}

}
