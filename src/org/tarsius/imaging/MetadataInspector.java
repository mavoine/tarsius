package org.tarsius.imaging;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;

public class MetadataInspector {
	
	private static Log log = LogFactory.getLog(MetadataInspector.class);
	private static SimpleDateFormat dateShotSDF = 
		new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
	
	private IImageMetadata metadata = null;
	
	public static enum Orientation {
		TOP_LEFT,
		TOP_LEFT_INVERTED,
		LEFT_BOTTOM, 
		LEFT_BOTTOM_INVERTED, 
		RIGHT_BOTTOM, 
		RIGHT_BOTTOM_INVERTED, 
		TOP_RIGHT,
		TOP_RIGHT_INVERTED}
	
	private static Map<Integer,Orientation> ORIENTATION_MAP = new HashMap<Integer,Orientation>();
	
	static {
		ORIENTATION_MAP.put(1, Orientation.TOP_LEFT);
		ORIENTATION_MAP.put(2, Orientation.TOP_LEFT_INVERTED);
		ORIENTATION_MAP.put(3, Orientation.RIGHT_BOTTOM);
		ORIENTATION_MAP.put(4, Orientation.RIGHT_BOTTOM_INVERTED);
		ORIENTATION_MAP.put(5, Orientation.TOP_RIGHT_INVERTED);
		ORIENTATION_MAP.put(6, Orientation.LEFT_BOTTOM);
		ORIENTATION_MAP.put(7, Orientation.LEFT_BOTTOM_INVERTED);
		ORIENTATION_MAP.put(8, Orientation.TOP_RIGHT);
	}
	
	public MetadataInspector(File imageFile){
		try {
			metadata = Sanselan.getMetadata(imageFile);
		} catch (ImageReadException e) {
			log.error(e);
			// TODO manage exception
		} catch (IOException e) {
			log.error(e);
			// TODO manage exception
		}
	}

	/**
	 * Return the date at which the photo was shot or null if it cannot be obtained.
	 * @return Date shot or null
	 */
	public Date getDateShot(){
		Date date = null;
		if (metadata instanceof JpegImageMetadata) {
			JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
			TiffField tf = jpegMetadata.findEXIFValue(
					TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
			if(tf == null){
				return null;
			}
			try {
				String dateTimeString = (String)tf.getValue();
				date = dateShotSDF.parse(dateTimeString);
				if(log.isTraceEnabled()){
					log.trace("Date: " + date.toString());
				}
			} catch (ImageReadException e) {
				log.error(e);
			} catch (ParseException e) {
				log.error("Date/time shot: wrong format", e);
			} catch (NullPointerException e){
				log.error("Date/time shot: field value null", e);
			}
		}
		return date;
	}
	
	public String readTagValue(TagInfo tagInfo) {
		String str = "";
		if (metadata instanceof JpegImageMetadata) {
			JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
	        TiffField field = jpegMetadata.findEXIFValue(tagInfo);
	        if (field == null) {
	        	str = str.concat(tagInfo.name + ": " + "Not Found.");
	        } else {
	        	str = str.concat(tagInfo.name + ": " + field.getValueDescription());
	        }
		}
		log.trace("Read: "+ str);
        return str;
	}

	public Orientation getOrientation(){
		// TODO support other orientations
		Orientation orientation = Orientation.TOP_LEFT; // default
		if (metadata instanceof JpegImageMetadata) {
			JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
			TiffField tf = jpegMetadata.findEXIFValue(
					TiffConstants.EXIF_TAG_ORIENTATION);
			
			try {
				Integer integer = (Integer)tf.getValue();
				orientation = ORIENTATION_MAP.get(integer);
				log.trace("Orientation: " + orientation);
			} catch (ImageReadException e) {
				log.error(e);
			}
		}
		return orientation;
	}

}
