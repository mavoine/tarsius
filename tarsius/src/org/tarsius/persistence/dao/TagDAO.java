package org.tarsius.persistence.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Photo;
import org.tarsius.bean.Tag;
import org.tarsius.persistence.Database;
import org.tarsius.persistence.PersistenceException;

// FIXME make transactions (question: in Tasks or in DAOs? TBD)
public class TagDAO {
	
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(TagDAO.class);
	private static TagDAO instance = null;
	
	private TagDAO(){
	}
	
	public static TagDAO getInstance(){
		if(instance == null){
			instance = new TagDAO();
		}
		return instance;
	}
	
	public Integer countTags() throws PersistenceException{
		Integer count = null;
		try {
			count = (Integer)
				Database.getInstance().getSqlMap().queryForObject("countTags");
		} catch (SQLException e) {
			throw new PersistenceException("Failed to count tags", e);
		}
		return count;
	}
	
	public Tag getTag(Integer id) throws PersistenceException {
		Tag tag = null;
		try {
			tag = 
				(Tag)Database.getInstance().getSqlMap().queryForObject("getTag", id);
			if(tag == null){
				throw new PersistenceException("No tag exists with id [" + id + "]");
			}
		} catch (SQLException e) {
			throw new PersistenceException("Failed to load tag", e);
		}
		return tag;
	}
	
	public void insertTag(Tag tag) throws PersistenceException {
		if(tag == null){
			throw new NullPointerException("Tag cannot be null");
		}
		if(tag.getName() == null){
			throw new NullPointerException("Tag name cannot be null");
		}
		try {
			Database.getInstance().getSqlMap().insert("insertTag", tag);
			insertTagChild(tag);
		} catch (SQLException e) {
			throw new PersistenceException("Failed to insert tag", e);
		}
	}
	
	private void insertTagChild(Tag tag) throws SQLException {
		Integer parentTagId = tag.getIdParent();
		while(parentTagId != null) {
			HashMap<String,Integer> paramMap = new HashMap<String,Integer>();
			paramMap.put("tagId", parentTagId);
			paramMap.put("childTagId", tag.getId());
			Database.getInstance().getSqlMap().insert("insertTagChild", paramMap);
			Tag parentTag = (Tag)Database.getInstance().getSqlMap().queryForObject(
					"getTag", parentTagId);
			parentTagId = parentTag.getIdParent();
		}
	}

	public void updateTag(Tag tag) throws PersistenceException {
		if(tag == null){
			throw new NullPointerException("Tag cannot be null");
		}
		if(tag.getName() == null){
			throw new NullPointerException("Tag name cannot be null");
		}
		try {
			Database.getInstance().getSqlMap().delete("deleteTagFromChildren", tag);
			Database.getInstance().getSqlMap().update("updateTag", tag);
			insertTagChild(tag);
		} catch (SQLException e) {
			throw new PersistenceException("Failed to update tag", e);
		}
	}

	public void deleteTag(Tag tag) throws PersistenceException {
		if(tag == null){
			throw new NullPointerException("Tag cannot be null");
		}
		try {
			Database.getInstance().getSqlMap().delete("deleteTagFromPhotoTag", tag);
			Database.getInstance().getSqlMap().delete("deleteTagFromChildren", tag);
			Database.getInstance().getSqlMap().delete("deleteTag", tag);
		} catch (SQLException e) {
			throw new PersistenceException("Failed to reparent tag", e);
		}
	}
	
	public void addTag(Tag tag, Photo photo) throws PersistenceException {
		try {
			HashMap<String,Integer> paramMap = new HashMap<String,Integer>();
			paramMap.put("photoId", photo.getId());
			paramMap.put("tagId", tag.getId());
			Database.getInstance().getSqlMap().insert("addTag", paramMap);
		} catch (SQLException e) {
			throw new PersistenceException("Failed to add tag to photos", e);
		}
	}
	
	public void removeTag(Tag tag, Photo photo) throws PersistenceException {
		HashMap<String,Integer> paramMap = new HashMap<String,Integer>();
		paramMap.put("photoId", photo.getId());
		paramMap.put("tagId", tag.getId());
		try {
			Database.getInstance().getSqlMap().delete("removeTag", paramMap);
		} catch (SQLException e) {
			throw new PersistenceException("Failed to remove tag from photos", e);
		}
	}

	public Integer countTaggedPhotos(Tag tag, boolean includeTagChildren)
			throws PersistenceException {
		Integer count = null;
		try {
			if(includeTagChildren){
				count = (Integer)Database.getInstance().getSqlMap().queryForObject(
						"countTaggedPhotosIncludingChildren", tag);
			} else {
				count = (Integer)Database.getInstance().getSqlMap().queryForObject(
						"countTaggedPhotos", tag);
			}
		} catch (SQLException e) {
			throw new PersistenceException("Failed to count tagged photos", e);
		}
		return count;
	}
	
	public Integer countTagChildren(Tag tag) throws PersistenceException {
		Integer count = null;
		try {
			count = (Integer)Database.getInstance().getSqlMap().queryForObject(
					"countTagChildren", tag);
		} catch (SQLException e) {
			throw new PersistenceException("Failed to count tag children", e);
		}
		return count;
	}
	
	@SuppressWarnings("unchecked")
	public List<Tag> getAllTags() throws PersistenceException {
		List<Tag> tags = null;
		try {
			tags = Database.getInstance().getSqlMap().queryForList("getAllTags");
		} catch (SQLException e) {
			throw new PersistenceException("Failed to get all tags", e);
		}
		return tags;
	}
	
}
