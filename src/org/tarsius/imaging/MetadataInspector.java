package org.tarsius.imaging;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	
	public static enum Orientation {TOP_LEFT, LEFT_BOTTOM, RIGHT_BOTTOM, TOP_RIGHT}
	
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
				switch(integer){
				case 1:
					orientation = Orientation.TOP_LEFT;
					break;
				case 8:
					orientation = Orientation.LEFT_BOTTOM;
					break;
				default:
					orientation = Orientation.TOP_LEFT;
					break;
				}
				log.trace("Orientation: " + orientation);
			} catch (ImageReadException e) {
				log.error(e);
			}
		}
		return orientation;
	}

}
