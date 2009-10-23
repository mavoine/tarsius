package org.tarsius.util;

import java.util.Comparator;

import org.tarsius.bean.Tag;

public class TagNameComparator implements Comparator<Tag> {

	public int compare(Tag t1, Tag t2) {
		return t1.getName().compareToIgnoreCase(t2.getName());
	}
	
}
