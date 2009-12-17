package org.tarsius.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.tarsius.bean.Photo;
import org.tarsius.bean.Tag;
import org.tarsius.util.DateUtil;

public class TestData {
	
	public static Photo photo1 = null;
	public static Photo photo2 = null;
	public static Photo photo3 = null;
	public static Photo photo4 = null;
	
	public static Tag tag1 = null;
	public static Tag tag2 = null;
	public static Tag tag3 = null;
	public static Tag tag4 = null;
	public static Tag tag5 = null;
	
	public static void setup() {
		
		tag1 = new Tag(1, "Tag1", null);
		tag2 = new Tag(2, "Tag2", null);
		tag3 = new Tag(3, "Tag3", 2);
		tag4 = new Tag(4, "Tag4", 2);
		tag5 = new Tag(5, "Tag5", 4);
		
		try {
			photo1 = new Photo();
			photo1.setId(1);
			photo1.setDate(DateUtil.parseDateTime("2008-01-01 12:30:46.0"));
			photo1.setPath("/testdata/photos/IMG_3887.jpg");
			photo1.setPathRelToGallery(true);
			List<Tag> tags1 = new ArrayList<Tag>();
			tags1.add(tag1);
			photo1.setTags(tags1);
	
			photo2 = new Photo();
			photo2.setId(2);
			photo2.setDate(DateUtil.parseDateTime("2008-05-06 13:38:00.0"));
			photo2.setPath("/testdata/photos/IMG_1444.jpg");
			photo2.setPathRelToGallery(true);
			List<Tag> tags2 = new ArrayList<Tag>();
			tags2.add(tag2);
			tags2.add(tag3);
			photo2.setTags(tags2);
	
			photo3 = new Photo();
			photo3.setId(3);
			photo3.setDate(DateUtil.parseDateTime("2008-10-14 02:26:53.0"));
			photo3.setPath("/testdata/photos/IMG_1450.jpg");
			photo3.setPathRelToGallery(true);
			List<Tag> tags3 = new ArrayList<Tag>();
			tags3.add(tag3);
			tags3.add(tag4);
			tags3.add(tag5);
			photo3.setTags(tags3);
	
			photo4 = new Photo();
	//		photo4.setId(4);
			photo4.setDate(DateUtil.parseDateTime("2006-05-13 03:02:55.0"));
			photo4.setPath("/testdata/photos/IMG_1586.jpg");
			photo4.setPathRelToGallery(true);
		} catch (ParseException pe){
			throw new RuntimeException("Unable to parse a date", pe);
		}
	}
	
	public static void tearDown(){
		photo1 = null;
		photo2 = null;
		photo3 = null;
		photo4 = null;
		
		tag1 = null;
		tag2 = null;
		tag3 = null;
		tag4 = null;
		tag5 = null;
	}

}
