package org.tarsius.gui.component;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Tag;
import org.tarsius.gui.action.AddTagAction;
import org.tarsius.gui.action.PhotoSelectionProvider;
import org.tarsius.gui.event.MouseLeftClickListener;
import org.tarsius.i18n.I18n;
import org.tarsius.util.TagNameComparator;

public class AddTagMenu extends JMenu {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(AddTagMenu.class);
	
	private PhotoSelectionProvider photoSelectionProvider = null;
	
	public AddTagMenu(PhotoSelectionProvider photoSelectionProvider) {
		super();
		setText(I18n.translate("AddTag"));
		setMnemonic(I18n.mnemonicKey("AddTag"));
		this.photoSelectionProvider = photoSelectionProvider;
	}
	
	public void setData(List<Tag> tags){

		this.removeAll();
		if(tags == null){
			return;
		}

		List<Tag> tagsTmp = new ArrayList<Tag>();
		tagsTmp.addAll(tags);
		
		Map<Integer,JMenuItem> alreadyPresent = new HashMap<Integer,JMenuItem>();
		
		List<Tag> tagSet = new ArrayList<Tag>();
		tagSet.addAll(tagsTmp);
		Collections.sort(tagSet, new TagNameComparator());
		while(!tagsTmp.isEmpty()){
			Iterator<Tag> it = tagSet.iterator();
			while (it.hasNext()) {
				Tag tag = (Tag) it.next();
				JMenuItem menuItem = null;
				// if the tag has any children
				if (hasChildren(tags, tag)) {
					// make a JMenu to be able to add children
					final AddTagAction action = new AddTagAction(tag,
							photoSelectionProvider);
					final JMenu menu = this;
					menuItem = new JMenu(action);
					// TODO fix the key listener to react on ENTER
//					menuItem.addMenuKeyListener(new MenuKeyListener(){
//						public void menuKeyPressed(MenuKeyEvent e) {}
//						public void menuKeyReleased(MenuKeyEvent e) {}
//						public void menuKeyTyped(MenuKeyEvent e) {
//							if(e.getKeyCode() == KeyEvent.VK_ENTER){
//								menu.setPopupMenuVisible(false);
//								action.actionPerformed(null);
//							}
//						}
//					});
					menuItem.addMouseListener(new MouseLeftClickListener(){
						@Override
						public void leftClicked(MouseEvent event) {
							// TODO fix close menu
							menu.setPopupMenuVisible(false);
							action.actionPerformed(null);
						}
					});
				} else {
					// otherwise, make it a JMenuItem
					menuItem = new JMenuItem(new AddTagAction(tag,
							photoSelectionProvider));
				}
				// if the tag has no parent (is a top-level tag)
				if (tag.getIdParent() == null) {
					this.add(menuItem);
					alreadyPresent.put(tag.getId(), menuItem);
					tagsTmp.remove(tag);
				} else {
					// if the parent of the tag is already in the tree
					if (alreadyPresent.containsKey(tag.getIdParent())) {
						JMenu parent = (JMenu) alreadyPresent.get(tag
								.getIdParent());
						parent.add(menuItem);

						alreadyPresent.put(tag.getId(), menuItem);
						tagsTmp.remove(tag);
					}
				}
			}
			tagSet.clear();
			tagSet.addAll(tagsTmp);
		}

	}
	
	private boolean hasChildren(List<Tag> tags, Tag tag){
		Iterator<Tag> it = tags.iterator();
		Tag compareTag = null;
		while(it.hasNext()){
			 compareTag = it.next();
			 if(tag.getId().equals(compareTag.getIdParent())){
				 return true;
			 }
		}
		return false;
	}
	
}
