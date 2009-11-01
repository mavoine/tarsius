package org.tarsius.persistence;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.Context;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class Database {
	
	private static Database instance = null;
	private static Log log = LogFactory.getLog(Database.class);
	private static SqlMapClient sqlMap = null;
	
	static {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException cnfe) {
			throw new RuntimeException("Cannot load hsqldb driver", cnfe);
		}
	}

	private Database() {
	}
	
	public static Database getInstance(){
		if(instance == null){
			instance = new Database();
		}
		return instance;
	}
	
	public SqlMapClient getSqlMap(){
		return sqlMap;
	}
	
	public boolean isDatabaseOpen(){
		return sqlMap != null;
	}

	/**
	 * Opens a database.
	 */
	public void open(String path, String username, String password){

		String dbLocation = "jdbc:hsqldb:" + path;
		Properties properties = new Properties();
		properties.put("driver", Context.getGlobalConfig().getDatabaseDriver());
		properties.put("url", dbLocation);
		properties.put("username", Context.getGlobalConfig().getDatabaseUsername());
		properties.put("password", Context.getGlobalConfig().getDatabasePassword());

		log.debug("Opening database: " + dbLocation);

		Reader reader;
		try {
			reader = Resources.getResourceAsReader("iBatisConfig.xml");
		} catch (IOException e) {
			throw new RuntimeException("Cannot load iBatis config file", e);
		}
		sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader, properties);
	}
	
	/**
	 * Closes the connection and shuts down the database. In the context of tests,
	 * this also destroys the database instance so it can be built anew for the
	 * next test case. 
	 */
	public void shutdown(){
		log.debug("Shutting down database");
		try {
			execute("shutdown");
			log.debug("Database connection closed successfully");
		} catch (Exception e){
			log.fatal("Database connection close failed", e);
		} finally {
			sqlMap = null;
		}
	}

	public void upgrade() throws Exception {
		Double dbVersion = null;
		Double progVersion= null;
		ResultSet rs = null;
		try {
			rs = execute("select version from info;");
			if(rs.next()){
				dbVersion = rs.getDouble("version");
			} else {
				throw new Exception("Info table empty");
			}
		} catch (SQLException e) {
			throw new Exception("Cannot get version of database", e);
		} finally {
			if(rs != null){
				rs.close();
			}
		}

		log.info("Database version is " + dbVersion);
		progVersion = Double.valueOf(Context.getGlobalConfig().getProgramVersion());
		log.info("Program version is " + progVersion);

		if(dbVersion < progVersion){
			log.info("Upgrading database");
			// TODO complete code for database upgrade
		}

	}

	/**
	 * Executes an sql query.
	 * @param sql
	 * @return The ResultSet or null is there is none.
	 * @throws Exception
	 * @Deprecated There seems to be an issue with the usage of this method
	 * for processing sql queries other than select. Use executeBatch instead.
	 */
	public ResultSet execute(String sql) throws Exception {
		log.debug("Execute sql: " + sql);
		Connection connection = sqlMap.getDataSource().getConnection();
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			stmt.execute(sql);
			rs = stmt.getResultSet();
		} catch (SQLException sqle1){
			log.error("Statement execution failed", sqle1);
			throw new Exception("Statement execution failed", sqle1);
		} finally {
			if(stmt != null){
				try {
					stmt.close();
				} catch (SQLException sqle2){
					throw new Exception("Statement close failed", sqle2);
				}
			}
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException sqle3){
					throw new Exception("Connection release failed", sqle3);
				}
			}
		}
		return rs;
	}
	
	/**
	 * Executes a batch of sql statements. The batch is executed as a single
	 * transaction; if an exception is thrown, the whole batch is rolled back.
	 * @param sql
	 * @throws SQLException
	 * @throws Exception
	 */
	public void executeBatch(String[] sql) throws SQLException, Exception {
		log.debug("SQL batch");
		Connection connection = sqlMap.getDataSource().getConnection();
		boolean autoCommit = connection.getAutoCommit();
		Statement stmt = null;
		try {
			connection.setAutoCommit(false);
			stmt = connection.createStatement();
			for(int i = 0; i < sql.length; i++){
				log.debug("Add sql to batch: " + sql[i]);
				stmt.addBatch(sql[i]);
			}
			log.debug("Execute batch");
			stmt.executeBatch();
			connection.commit();
		} catch (Exception ex){
			log.error("Failed to execute batch", ex);
			Throwable t = null;
			do{
				t = stmt.getWarnings();
				if(t != null){
					log.error("Batch failure detail", t.getCause());
				}
			} while(t != null);
			try {
				connection.rollback();
			} catch (Exception ex1){
				log.error("Transaction rollback failed", ex1);
			}
			throw new Exception("Error while executing batch update", ex);
		} finally {
			try {
				connection.setAutoCommit(autoCommit);
			} catch (Exception ex2){
				log.error("Reset auto-commit to previous value failed", ex2);
			}
			if(stmt != null){
				try {
					stmt.close();
				} catch (Exception ex3){
					log.error("Statement close failed", ex3);
				}
			}
			if(connection != null){
				try {
					connection.close();
				} catch (Exception ex3){
					log.error("Connection release failed", ex3);
				}
			}
		}
	}

}
