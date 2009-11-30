package org.tarsius.imaging;

import java.io.File;
import java.text.ParseException;

import org.tarsius.Context;
import org.tarsius.bean.Photo;
import org.tarsius.test.ExtendedTestCase;
import org.tarsius.util.DateUtil;

public class PhotoLoaderTest extends ExtendedTestCase {

	public void testLoad() throws ParseException{
		File file = new File(Context.getGallery().getRootPath() + "/testdata/photo/rc0005.jpg");
		PhotoLoader pl = new PhotoLoader();
		Photo photo = pl.load(file);
		assertNotNull(photo);
		assertEquals(DateUtil.parseDateTime("2006-05-10 09:59:33"), photo.getDate());
	}
	
}
