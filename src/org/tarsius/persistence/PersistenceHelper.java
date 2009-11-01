package org.tarsius.persistence;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PersistenceHelper {
	
	private static Log log = LogFactory.getLog(PersistenceHelper.class);
	private static PersistenceHelper instance = null;
	
	private PersistenceHelper(){
	}
	
	public static PersistenceHelper getInstance(){
		if(instance == null){
			instance = new PersistenceHelper();
		}
		return instance;
	}
	
	public void beginTransaction() throws RuntimeException {
		try {
			log.debug("Begin transaction");
			Database.getInstance().getSqlMap().startTransaction();
		} catch (SQLException e) {
			log.error("Failed to begin transaction", e);
			throw new RuntimeException("Failed to begin transaction, e");
		}
	}
	
	public void commitTransaction() throws RuntimeException{
		try {
			log.debug("Commit transaction");
			Database.getInstance().getSqlMap().commitTransaction();
		} catch (SQLException e) {
			log.error("Failed to commit transaction", e);
			throw new RuntimeException("Failed to commit transaction", e);
		}
	}
	
	public void endTransaction() throws RuntimeException{
		try {
			log.debug("End transaction");
			Database.getInstance().getSqlMap().endTransaction();
		} catch (SQLException e) {
			log.error("Failed to end transaction", e);
			throw new RuntimeException("Failed to end transaction, e");
		}
	}

}
