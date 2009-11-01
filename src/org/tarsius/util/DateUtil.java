package org.tarsius.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.format.DateTimeFormat;

public class DateUtil {
	
	private static final SimpleDateFormat dateFormatter = 
		new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	/**
	 * Parses a date and time string with a pattern like "yyyy-MM-dd hh:mm:ss",
	 * and returns a Date.
	 * @param dateString
	 * @return Date
	 * @throws ParseException If the date string is not in the correct format.
	 */
	public static Date parseDateTime(String dateString) throws ParseException{
		return dateFormatter.parse(dateString);
	}
	
	public static String formatDate(Date date){
		return DateTimeFormat.mediumDate().print(date.getTime());
	}
	
}
