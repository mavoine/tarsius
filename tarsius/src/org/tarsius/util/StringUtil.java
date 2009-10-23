package org.tarsius.util;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class StringUtil {
	
	public static String listToString(List<String> list, String separator){
		if(separator == null || separator.length() == 0){
			throw new InvalidParameterException("separator cannot be null or empty");
		}
		String result = "";
		if(list != null && list.size() > 0){
			for(String str : list){
				result += result.length() > 0 ? separator + str : str;
			}
		}
		return result;
	}
	
	public static List<String> stringToList(String string, String separator){
		if(separator == null || separator.length() == 0){
			throw new InvalidParameterException("separator cannot be null or empty");
		}
		List<String> list = new ArrayList<String>();
		if(string != null && string.length() > 0){
			for(String str : string.split(separator)){
				list.add(str);
			}
		}
		return list;
	}
	
	public static String concat(String str1, String str2){
		if(str1 == null){
			str1 = "";
		}
		if(str1.isEmpty()){
			str1 = str1.concat(str2);
		} else {
			str1 += ", " + str2;
		}
		return str1;
	}
	
	public static String[] splitOnLastOccurance(String string, String separator){
		String[] chunks = new String[2];
		int index = string.lastIndexOf(separator);
		chunks[0] = string.substring(0, index);
		chunks[1] = string.substring(index, string.length());
		return chunks;
	}

}
