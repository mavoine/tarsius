package org.tarsius.util;

import java.io.File;
import java.text.ParseException;
import java.util.Date;

import junit.framework.TestCase;

public class PathUtilTest extends TestCase {

	public void testPathFromDate() throws ParseException{
		Date date = DateUtil.parseDateTime("2008-05-12 00:00:00");
		String path = PathUtil.pathFromDate(date);
		String expectedPath = File.separator + "2008" + 
			File.separator + "05" +
			File.separator + "12";
		assertEquals(expectedPath, path);
	}
	
	public void testMd5Path(){
		String md5 = "c6ee772d9e49320e97ec29a7eb5b1697";
		String path = "file:///home/jens/photos/me.png";
		String calcMd5 = PathUtil.md5Path(path);
		assertEquals("Normal case", md5, calcMd5);
		
		md5 = "e74e71c0f654227cf9f60084ab275a3a";
		path = "file:///home/user/My Documents/My Pictures/Webcam/Image_00001.jpg";
		calcMd5 = PathUtil.md5Path(path);
		assertEquals("Border case: URI containing white spaces", md5, calcMd5);
	}
	
}
