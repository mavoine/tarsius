package org.tarsius.gui;

import java.awt.Container;

import javax.swing.JMenu;

/**
 * A Perspective defines a set of graphical interface features (i.e. content pane, 
 * menu bar, etc) which together form a UI and is meant to be displayed in the 
 * MainWindow.
 */
public interface Perspective {

	/**
	 * Return a java.awt.Container which represents the contents of the GUI.
	 * @return content pane
	 */
	public Container getContentPane();

	/**
	 * Return an array of javax.swing.JMenu which contains the menus to be added
	 * to the menu bar or null if no menus are to be added.
	 * @return menu bar or null
	 */
	public JMenu[] getMenus();

}
