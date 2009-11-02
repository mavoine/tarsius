package org.tarsius.imaging;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.tarsius.Context;
import org.tarsius.imaging.MetadataInspector;
import org.tarsius.imaging.MetadataInspector.Orientation;
import org.tarsius.test.ExtendedTestCaseNoDb;
import org.tarsius.util.DateUtil;

public class MetadataInspectorTest extends ExtendedTestCaseNoDb {
	
	public void testGetDateShot() throws ParseException{
		File file = new File(Context.getGallery().getGalleryPath() 
				+ "/testdata/photos/rc0005.jpg");
		MetadataInspector mi = new MetadataInspector(file);
		assertEquals(DateUtil.parseDateTime("2006-05-10 09:59:33"), 
				mi.getDateShot());
	}

	public void testPrintTagValue() throws ImageReadException, IOException {
		File file = new File(Context.getGallery().getGalleryPath() + "/testdata/photos/rc0005.jpg");
		MetadataInspector mi = new MetadataInspector(file);
        String str = mi.readTagValue(TiffConstants.EXIF_TAG_ISO);
        assertEquals("ISO: 200", str);
	}
	
	public void testGetOrientation(){
		File file = new File(Context.getGallery().getGalleryPath() + "/testdata/photos/rc0005.jpg");
		MetadataInspector mi = new MetadataInspector(file);
		assertEquals(Orientation.TOP_LEFT, mi.getOrientation());
	}
	
}
