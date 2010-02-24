package org.tarsius.persistence.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Photo;
import org.tarsius.bean.Tag;
import org.tarsius.persistence.PersistenceException;
import org.tarsius.test.ExtendedTestCase;

public class TagDAOTest extends ExtendedTestCase {
	
	private static Log log = LogFactory.getLog(TagDAOTest.class);
	
	public void testCountTags() throws Exception {
		Integer countTags = TagDAO.getInstance().countTags();
		assertEquals("Count total", 5, countTags.intValue());
	}

	public void testCountTaggedPhotos() throws Exception {
		// test count only this tag
		Tag tag = TagDAO.getInstance().getTag(2);
		Integer count = TagDAO.getInstance().countTaggedPhotos(tag, false);
		assertEquals("Count tagged photos", 1, count.intValue());
		// test count including children
		count = TagDAO.getInstance().countTaggedPhotos(tag, true);
		assertEquals("Count tagged photos including children", 5, count.intValue());
	}

	public void testGetTag() throws Exception {
		Tag tag = TagDAO.getInstance().getTag(1);
		assertEquals("Get Tag1", "Tag1", tag.getName());
	}

	public void testInsertTag() throws Exception {
		Tag tag = new Tag();
		tag.setName("Some tag");
		tag.setIdParent(4);
		TagDAO.getInstance().insertTag(tag);
		
		Integer countTags = TagDAO.getInstance().countTags();
		assertEquals("Count after insert", 6, countTags.intValue());
	}
	
	public void testUpdateTag() throws Exception {
		Tag tag1 = TagDAO.getInstance().getTag(1);
		tag1.setName("New name");
		TagDAO.getInstance().updateTag(tag1);
		tag1 = TagDAO.getInstance().getTag(1);
		assertEquals("Renamed Tag1", "New name", tag1.getName());
	}
	
	public void testInsertTagChild() throws Exception {
		TagDAO.getInstance().insertTag(new Tag(null, "Tag6", 4));
		Tag tag4 = TagDAO.getInstance().getTag(4);
		Integer count = TagDAO.getInstance().countTagChildren(tag4);
		assertEquals("Children count", 2, count.intValue());
	}
	
	public void testUpdateTagReparenting() throws Exception {
		Tag tag1 = TagDAO.getInstance().getTag(1);
		Tag tag2 = TagDAO.getInstance().getTag(2);
		tag1.setIdParent(tag2.getId());
		TagDAO.getInstance().updateTag(tag1);
		tag1 = TagDAO.getInstance().getTag(1);
		assertEquals("Reparent: parent id", tag2.getId(), tag1.getIdParent());
		tag2 = TagDAO.getInstance().getTag(2);
		Integer childrenCount = TagDAO.getInstance().countTagChildren(tag2);
		assertEquals("Reparent: children count", 4, childrenCount.intValue());
	}
	
	public void testUpdateTagReparentingBranch() throws Exception {
		Tag tag1 = TagDAO.getInstance().getTag(1);
		Tag tag4 = TagDAO.getInstance().getTag(4);
		tag4.setIdParent(tag1.getId());
		TagDAO.getInstance().updateTag(tag4);
		Integer childrenCount = TagDAO.getInstance().countTagChildren(tag1);
		assertEquals("Count children of tag1", 2, childrenCount.intValue());
		childrenCount = TagDAO.getInstance().countTagChildren(tag4);
		assertEquals("Count children of tag4", 1, childrenCount.intValue());
		Tag tag2 = TagDAO.getInstance().getTag(2);
		childrenCount = TagDAO.getInstance().countTagChildren(tag2);
		assertEquals("Count children of tag2", 1, childrenCount.intValue());
	}
	
	public void testDeleteTag() throws Exception {
		Tag tag2 = TagDAO.getInstance().getTag(2);
		Tag tag5 = TagDAO.getInstance().getTag(5);
		TagDAO.getInstance().deleteTag(tag5);
		try {
			TagDAO.getInstance().getTag(5);
			fail("getTag() should have failed; the tag was deleted.");
		} catch (PersistenceException e){}
		Integer childrenCount = TagDAO.getInstance().countTagChildren(tag2);
		assertEquals("Delete tag: children count", 2, childrenCount.intValue());
		try {
			TagDAO.getInstance().deleteTag(tag2);
			fail("Should have failed because it is not yet supported to delete a tag that has children");
		} catch (PersistenceException e){}
	}
	
	public void testAddTag() throws Exception {
		Photo photo = PhotoDAO.getInstance().getPhoto(1);
		Tag tag = TagDAO.getInstance().getTag(2);
		TagDAO.getInstance().addTag(tag, photo);
		Integer count = TagDAO.getInstance().countTaggedPhotos(tag, false);
		assertEquals("Count photos tagged with tag2", 2, count.intValue());
	}
	
	public void testRemoveTag() throws Exception {
		Photo photo = PhotoDAO.getInstance().getPhoto(2);
		Tag tag = TagDAO.getInstance().getTag(2);
		TagDAO.getInstance().removeTag(tag, photo);
		Integer count = TagDAO.getInstance().countTaggedPhotos(tag, false);
		assertEquals("Count photos tagged with tag2", 0, count.intValue());
	}
	
	public void testFindAllTags() throws Exception {
		List<Tag> tags = TagDAO.getInstance().getAllTags();
		assertNotNull(tags);
		assertEquals("Tags list size", 5, tags.size());
	}
	
}
