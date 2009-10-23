package org.tarsius.config;

import java.util.ResourceBundle;

public class GlobalConfig {
	
	private Double programVersion = null;
	private String databaseDriver = null;
	private String databaseUsername = null;
	private String databasePassword = null;
	
	public GlobalConfig() {
		ResourceBundle rb = ResourceBundle.getBundle("global");
		programVersion = Double.valueOf(rb.getString("program.version"));
		databaseDriver = rb.getString("database.driver");
		databaseUsername = rb.getString("database.username");
		databasePassword = rb.getString("database.password");
	}
	
	public Double getProgramVersion(){
		return programVersion;
	}

	public String getDatabaseDriver(){
		return databaseDriver;
	}
		
	public String getDatabaseUsername(){
		return databaseUsername;
	}
	
	public String getDatabasePassword(){
		return databasePassword;
	}

}
