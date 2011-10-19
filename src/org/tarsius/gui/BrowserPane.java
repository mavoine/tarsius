package org.tarsius.gui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.gui.action.CreateTagAction;
import org.tarsius.gui.action.DeletePhotoFromDriveAction;
import org.tarsius.gui.action.DeletePhotoFromGalleryAction;
import org.tarsius.gui.action.DeleteTagAction;
import org.tarsius.gui.action.RenameTagAction;
import org.tarsius.gui.component.AddTagMenu;
import org.tarsius.gui.component.PhotoTable;
import org.tarsius.gui.component.PhotoTableController;
import org.tarsius.gui.component.RemoveTagMenu;
import org.tarsius.gui.component.TagTree;
import org.tarsius.i18n.I18n;

public class BrowserPane extends JPanel implements Perspective {
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(BrowserPane.class);

	// components
	protected MigLayout centerLayout = null;
	protected JPanel centerPanel = null;
	protected PhotoTable photoTable = null;
	protected PhotoTableController photoTableControler = null;
//	protected JScrollPane photoTableScrollPane = null;
	protected TagTree tagTree = null;
	protected JTextField filterTextField = null;
	protected JButton clearFilterButton = null;
	protected JPanel filterPanel = null;
	protected JPanel infoPanel = null;
	protected JLabel infoLabel = null;

	// menus
	//   photo
	protected JMenu photoMenu = null;
	protected AddTagMenu addTagMenu = null;
	protected RemoveTagMenu removeTagMenu = null;
	//   tag
	protected JMenu tagMenu = null;

	// contextual menus
	protected JPopupMenu tagPopupMenu = null;
	protected JPopupMenu photoPopupMenu = null;
	protected AddTagMenu addTagMenuForPopup = null;
	protected RemoveTagMenu removeTagMenuForPopup = null;
			
	// actions
	protected CreateTagAction createTagAction = null;
	protected RenameTagAction renameTagAction = null;
	protected DeleteTagAction deleteTagAction = null;
	protected DeletePhotoFromDriveAction deletePhotoFromDriveAction = null;
	protected DeletePhotoFromGalleryAction deletePhotoFromGalleryAction = null;

	public BrowserPane() {
		
		log.debug("Building browser");
		MigLayout mainLayout = new MigLayout(
				"fill",              // layout constraints
				"[]",        // column constraints
				"[][grow][]"); // row constraints
		this.setLayout(mainLayout);
		
		// create the photo table
		photoTable = new PhotoTable();
		photoTable.setEnabled(false);
//		photoTableScrollPane = new JScrollPane(photoTable);
//		photoTableScrollPane.setVerticalScrollBarPolicy(
//				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		photoTableScrollPane.setHorizontalScrollBarPolicy(
//				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//		photoTableScrollPane.setMinimumSize(new Dimension(300, 300));

		photoTableControler = new PhotoTableController();
		photoTableControler.setPhotoTable(photoTable);
		
		// create the tag panel
		MigLayout tagPanelLayout = new MigLayout(
				"fill",          // layout constraints
				"[]",    // column constraints
				"[][]"); // row constraints
		JPanel tagPanel = new JPanel(tagPanelLayout);
		JLabel tagsLabel = new JLabel(I18n.translate("Tags"));
		tagTree = new TagTree();
		tagTree.setEnabled(false);
		JScrollPane tagTreeScroll = new JScrollPane(tagTree);
		tagTreeScroll.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tagTreeScroll.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tagPanel.add(tagsLabel, "wrap");
		tagPanel.add(tagTreeScroll, "dock south, grow, wmin 75px");
		
		// create the center panel
		centerPanel = new JPanel(new BorderLayout());
//		centerPanel.add(photoTableScrollPane, BorderLayout.CENTER);
		centerPanel.add(photoTable, BorderLayout.CENTER);
		
		// create the filter input field
		filterPanel = new JPanel(new MigLayout("", "0[grow]0", "0[]0"));
		JLabel filterLabel = new JLabel(I18n.translate("Search"));
		filterTextField = new JTextField();
		filterTextField.setEditable(false);
		clearFilterButton = new JButton(I18n.translate("Clear"));
		clearFilterButton.setEnabled(false);
		filterPanel.add(filterLabel, "split 3");
		filterPanel.add(filterTextField, "grow");
		filterPanel.add(clearFilterButton, "w button, align right");
		this.add(filterPanel, "grow, wrap");

		// create the split pane
		JSplitPane splitPane = new JSplitPane();
		splitPane.setLeftComponent(tagPanel);
		splitPane.setRightComponent(centerPanel);
		splitPane.setContinuousLayout(false);
		this.add(splitPane, "grow, wrap, wmin 100px, hmin 100px");
		
		// create the bottom info panel
		infoPanel = new JPanel();
		infoLabel = new JLabel();
		infoPanel.add(infoLabel);
		this.add(infoPanel, "");
		
		// layout the components to get the available space for the table
		// TODO is this still required?
		this.validate();
		this.doLayout();

		// create actions
		createTagAction = new CreateTagAction(tagTree);
		renameTagAction = new RenameTagAction(tagTree);
		deleteTagAction = new DeleteTagAction(tagTree);
		deletePhotoFromDriveAction = new DeletePhotoFromDriveAction(photoTable);
		deletePhotoFromGalleryAction = new DeletePhotoFromGalleryAction(
				photoTable);
		
		// create menus
		// photo
		photoMenu = new JMenu(I18n.translate("Photo"));
		photoMenu.setMnemonic(I18n.mnemonic("Photo"));
		addTagMenu = new AddTagMenu(photoTable);
		photoMenu.add(addTagMenu);
		removeTagMenu = new RemoveTagMenu(photoTable);
		photoMenu.add(removeTagMenu);
		photoMenu.addSeparator();
		photoMenu.add(deletePhotoFromGalleryAction);
		photoMenu.add(deletePhotoFromDriveAction);
		// tag
		tagMenu = new JMenu(I18n.translate("Tag"));
		tagMenu.setMnemonic(I18n.mnemonic("Tag"));
		tagMenu.add(createTagAction);
		tagMenu.add(renameTagAction);
		tagMenu.add(deleteTagAction);
		
		// create the contextual menus
		tagPopupMenu = new JPopupMenu();
		tagPopupMenu.add(createTagAction);
		tagPopupMenu.add(renameTagAction);
		tagPopupMenu.add(deleteTagAction);
		photoPopupMenu = new JPopupMenu();
		addTagMenuForPopup = new AddTagMenu(photoTable);
		photoPopupMenu.add(addTagMenuForPopup);
		removeTagMenuForPopup = new RemoveTagMenu(photoTable);
		photoPopupMenu.add(removeTagMenuForPopup);
		photoPopupMenu.addSeparator();
		photoPopupMenu.add(deletePhotoFromGalleryAction);
		photoPopupMenu.add(deletePhotoFromDriveAction);
	}
	
	public Container getContentPane() {
		return this;
	}
	
	public JMenu[] getMenus() {
		JMenu[] menus = new JMenu[2];
		menus[0] = photoMenu;
		menus[1] = tagMenu;
		return menus;
	}

}
