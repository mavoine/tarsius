package org.tarsius.util;

import java.util.Comparator;

import org.tarsius.gui.action.RemoveTagAction;

public class RemoveTagActionComparator implements Comparator<RemoveTagAction> {

	public int compare(RemoveTagAction t1, RemoveTagAction t2) {
		return t1.getTag().getName().compareToIgnoreCase(t2.getTag().getName());
	}

}
