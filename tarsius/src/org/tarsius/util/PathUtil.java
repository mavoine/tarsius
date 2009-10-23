package org.tarsius.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.twmacinta.util.MD5;

public class PathUtil {
	
	private static Log log = LogFactory.getLog(PathUtil.class);
	
	private static DateTimeFormatter dayTF = DateTimeFormat.forPattern("dd");
	private static DateTimeFormatter monthTF = DateTimeFormat.forPattern("MM");
	private static DateTimeFormatter yearTF = DateTimeFormat.forPattern("yyyy");

	/**
	 * Builds a file path in the form /yyyy/mm/dd from a given date. The path 
	 * contains a leading, but no trailing separator.
	 * @param date
	 * @return path
	 */
	public static String pathFromDate(Date date){
		String path = File.separator + yearTF.print(date.getTime()) +
			File.separator + monthTF.print(date.getTime()) +
			File.separator + dayTF.print(date.getTime());
		return path;
	}
	
	public static String md5Path(String path){
		String urlEncodedPath = null;
		// spaces seem to be a problem in URIs. this solution is patchy 
		// and should be revised some time as there may be other problems hidden...
		urlEncodedPath = path.replaceAll(" ", "%20");
		log.trace("Encoded path: " + urlEncodedPath);
	    MD5 md5 = new MD5();
	    try {
			md5.Update(urlEncodedPath, null);
		} catch (UnsupportedEncodingException e) {
			log.error(e);
		}
	    return md5.asHex();
	}

}
