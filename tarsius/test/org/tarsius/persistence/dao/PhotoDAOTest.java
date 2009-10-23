package org.tarsius.persistence.dao;

import java.util.List;

import org.tarsius.bean.Photo;
import org.tarsius.persistence.PersistenceException;
import org.tarsius.persistence.dao.criteria.PhotoCriteria;
import org.tarsius.test.ExtendedTestCase;
import org.tarsius.test.TestData;
import org.tarsius.util.DateUtil;

public class PhotoDAOTest extends ExtendedTestCase {
	
	public void testGetNextPhotoId() throws Exception {
		Integer photoId = PhotoDAO.getInstance().getNextPhotoId();
		assertNotNull(photoId);
	}

	public void testGetPhoto() throws Exception {
		Photo photo2 = PhotoDAO.getInstance().getPhoto(TestData.photo2.getId());
		assertNotNull(photo2);
		assertNotNull("Tag list not null", photo2.getTags());
		assertEquals("Tag list contains 2", 2, photo2.getTags().size());
		assertTrue("List contains tag2", photo2.getTags().contains(TestData.tag2));
		assertTrue("List contains tag3", photo2.getTags().contains(TestData.tag3));
	}
	
	public void testInsertPhoto() throws Exception{
		
		int nbBefore = PhotoDAO.getInstance().getCountPhotos();
		assertEquals("nb of photos before insert", 3, nbBefore);

		Photo photo = PhotoDAO.getInstance().insertPhoto(TestData.photo4);
		assertEquals("photo id", 100, photo.getId().intValue());
		
		int nbAfter = PhotoDAO.getInstance().getCountPhotos();
		assertEquals("nb of photos after insert", 4, nbAfter);
	}
	
	public void testGetPhotos() throws Exception {
		
		// test no criteria
		PhotoCriteria criteria = new PhotoCriteria();
		List<Photo> photos = PhotoDAO.getInstance().getPhotos(criteria);
		assertNotNull(photos);
		assertEquals("nb photos fetched", 3, photos.size());
		
		// test date restriction
		criteria = new PhotoCriteria();
		criteria.addTimeRestriction(
				DateUtil.parseDateTime("2008-05-06 00:00:00.0"), 
				DateUtil.parseDateTime("2008-10-15 00:00:00.0"));
		photos = PhotoDAO.getInstance().getPhotos(criteria);
		assertNotNull(photos);
		assertEquals("nb photos with date restriction", 2, photos.size());
		
		// test tag restriction
		criteria = new PhotoCriteria();
		criteria.addTagRestriction(TestData.tag3);
		photos = PhotoDAO.getInstance().getPhotos(criteria);
		assertNotNull(photos);
		assertEquals("nb photos with tag restriction", 2, photos.size());
	}
	
	public void testDeletePhoto() throws Exception {
		PhotoDAO.getInstance().deletePhoto(TestData.photo1);
		try {
			PhotoDAO.getInstance().getPhoto(new Integer(1));
			fail("Photo with id 1 has just been deleted");
		} catch (PersistenceException e){
			// this exception is expected
		}
	}
	
}
