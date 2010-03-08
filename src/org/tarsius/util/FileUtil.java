package org.tarsius.util;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.util.PlatformUtil.Platform;


public class FileUtil {
	
	private static Log log = LogFactory.getLog(FileUtil.class);
	
	public static void makeHidden(File file){
		if(file == null){
			return;
		}
		if(Platform.WINDOWS.equals(PlatformUtil.getPlatform())){
			try {
				Runtime.getRuntime().exec("attrib +h " + file.getAbsolutePath());
			} catch (Exception e){
				log.warn("Failed to hide directory [" + file.getAbsolutePath() + "]");
			}
		}
	}

}
