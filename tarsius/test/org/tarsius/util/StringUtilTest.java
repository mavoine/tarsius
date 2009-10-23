package org.tarsius.util;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class StringUtilTest extends TestCase {
	
	public void testListToString(){
		List<String> list = new ArrayList<String>();
		list.add("AAA");
		list.add("BBB");
		list.add("CCC");
		assertEquals("AAA;BBB;CCC", StringUtil.listToString(list, ";"));
		assertEquals("", StringUtil.listToString(new ArrayList<String>(), ";"));
		assertEquals("", StringUtil.listToString(null, ";"));
		try {
			StringUtil.listToString(new ArrayList<String>(), "");
			fail("Expected an InvalidParameterException");
		} catch (InvalidParameterException ipe){}
		try {
			StringUtil.listToString(new ArrayList<String>(), null);
			fail("Expected an InvalidParameterException");
		} catch (InvalidParameterException ipe){}
	}
	
	public void testStringToList(){
		String string = "AAA;BBB;CCC";
		List<String> list = StringUtil.stringToList(string, ";");
		assertEquals(3, list.size());
		assertEquals("AAA", list.get(0));
		assertEquals("BBB", list.get(1));
		assertEquals("CCC", list.get(2));
		assertEquals(0, StringUtil.stringToList("", ";").size());
		assertEquals(0, StringUtil.stringToList(null, ";").size());
		try {
			StringUtil.stringToList("", "");
			fail("Expected an InvalidParameterException");
		} catch (InvalidParameterException ipe){}
		try {
			StringUtil.stringToList("", null);
			fail("Expected an InvalidParameterException");
		} catch (InvalidParameterException ipe){}
	}
	
	public void testConcat(){
		String str1 = "";
		str1 = StringUtil.concat(str1, "hello");
		assertEquals("hello", str1);
		str1 = StringUtil.concat(str1, "world");
		assertEquals("hello, world", str1);
		str1 = StringUtil.concat(str1, "hello!");
		assertEquals("hello, world, hello!", str1);
	}
	
	public void testSplitOnLastOccurance(){
		String string = "/some/path/with/file.txt";
		String chunk[] = StringUtil.splitOnLastOccurance(string, ".");
		assertEquals("First chunk", "/some/path/with/file", chunk[0]);
		assertEquals("Second chunk", ".txt", chunk[1]);
	}

}
