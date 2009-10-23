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
		String platform = System.getProperty("os.name");
		if("Linux".equals(platform)){
			currentPlatform = Platform.LINUX;
		} else if("Windows".equals(platform)){
			currentPlatform = Platform.WINDOWS;
		} else {
			currentPlatform = Platform.UNKNOWN;
		}
	}
	
	public static Platform getPlatform(){
		return currentPlatform;
	}

}
