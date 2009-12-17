package org.tarsius.imaging;

import java.io.File;

import junit.framework.TestCase;

import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.tarsius.imaging.MetadataInspector.Orientation;
import org.tarsius.util.DateUtil;

public class MetadataInspectorTest extends TestCase {
	
	private String rundir = null;
	
	@Override
	protected void setUp() throws Exception {
		rundir = System.getProperty("user.dir");
	}
	
	@Override
	protected void tearDown() throws Exception {
		rundir = null;
	}
	
	public void testGetDateShot() throws Exception {
		MetadataInspector mi = new MetadataInspector(new File(rundir + "/testdata/photo/IMG_3887.jpg"));
		assertEquals(DateUtil.parseDateTime("2008-07-12 18:04:36"), 
				mi.getDateShot());
	}

	public void testPrintTagValue() throws Exception {
		MetadataInspector mi = new MetadataInspector(new File(rundir + "/testdata/photo/IMG_1586.jpg"));
        String str = mi.readTagValue(TiffConstants.EXIF_TAG_ISO);
        assertEquals("ISO: 100", str);
	}
	
	public void testGetOrientation() throws Exception {
		MetadataInspector mi = new MetadataInspector(new File(rundir + "/testdata/photo/IMG_3887.jpg"));
		assertEquals(Orientation.TOP_LEFT, mi.getOrientation());
		mi = new MetadataInspector(new File(rundir + "/testdata/photo/IMG_1450.jpg"));
		assertEquals(Orientation.TOP_RIGHT, mi.getOrientation());
		mi = new MetadataInspector(new File(rundir + "/testdata/photo/IMG_1444.jpg"));
		assertEquals(Orientation.LEFT_BOTTOM, mi.getOrientation());
	}
	
}
