package org.tarsius.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Photo;
import org.tarsius.persistence.Database;
import org.tarsius.persistence.PersistenceException;
import org.tarsius.persistence.dao.criteria.PhotoCriteria;

public class PhotoDAO {
	
	private static Log log = LogFactory.getLog(PhotoDAO.class);
	private static PhotoDAO instance = null;
	
	private PhotoDAO() {
	}
	
	public static PhotoDAO getInstance(){
		if(instance == null){
			instance = new PhotoDAO();
		}
		return instance;
	}
	
	/**
	 * Finds photos based on the criteria in parameter.
	 * @param criteria
	 * @return List<Photo>
	 * @throws PersistenceException
	 */
	// TODO rename getPhotos
	public List<Photo> getPhotos(PhotoCriteria criteria) throws PersistenceException {
		List<Photo> photos = null;
		try {
			photos = Database.getInstance().getSqlMap().queryForList("getPhotos", criteria);
		} catch (SQLException sqle){
			throw new PersistenceException("Failed to fetch photo", sqle);
		}
		return photos;
	}
	
	public Photo getPhoto(Integer photoId) throws PersistenceException{
		Photo photo = null;
		try {
			photo = (Photo)Database.getInstance().getSqlMap()
				.queryForObject("getPhoto", photoId);
		} catch (SQLException sqle){
			throw new PersistenceException("Failed to fetch photo", sqle);
		}
		if(photo == null){
			throw new PersistenceException("No photo found with id " + photoId);
		}
		return photo;
	}
	
	public Photo insertPhoto(Photo photo) throws PersistenceException {
		try {
			Integer id = (Integer)Database.getInstance().getSqlMap()
				.insert("insertPhoto", photo);
			log.debug("new id: " + id); // TODO for debug
			photo.setId(id); // TODO this operation may not be necessary
		} catch (SQLException sqle){
			throw new PersistenceException("Failed to insert photo", sqle);
		}
		return photo;
	}
	
	public Integer getCountPhotos() throws PersistenceException{
		Integer nb = 0;
		try {
			nb = (Integer)Database.getInstance().getSqlMap()
				.queryForObject("getCountPhotos");
		} catch (SQLException sqle){
			throw new PersistenceException("Failed to fetch count of photos", sqle);
		}
		return nb;
	}

	public Integer getNextPhotoId() throws PersistenceException {
		Integer photoId = null;
		try {
			photoId = (Integer)Database.getInstance().getSqlMap()
				.queryForObject("getNextPhotoId");
		} catch (SQLException sqle){
			throw new PersistenceException("Failed to fetch photo id", sqle);
		}
		return photoId;
	}
	
	public void deletePhoto(Photo photo) throws PersistenceException {
		try {
			Database.getInstance().getSqlMap().delete("deletePhotoTags", photo);
			Database.getInstance().getSqlMap().delete("deletePhoto", photo);
		} catch (SQLException sqle){
			throw new PersistenceException("Failed to delete photo", sqle);
		}
	}

}
