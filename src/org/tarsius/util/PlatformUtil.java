package org.tarsius.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PlatformUtil {
	
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(PlatformUtil.class);
	
	public static enum Platform {
		UNKNOWN,
		WINDOWS,
		LINUX}
	
	private static Platform currentPlatform = null;
	
	static {
		String platform = System.getProperty("os.name").toUpperCase();
		if(platform == null || platform.length() == 0){
			currentPlatform = Platform.UNKNOWN;
		} else if(platform.indexOf("LINUX") > -1){
			currentPlatform = Platform.LINUX;
		} else if(platform.indexOf("WINDOWS") > -1){
			currentPlatform = Platform.WINDOWS;
		} else {
			currentPlatform = Platform.UNKNOWN;
		}
	}
	
	public static Platform getPlatform(){
		return currentPlatform;
	}

}
