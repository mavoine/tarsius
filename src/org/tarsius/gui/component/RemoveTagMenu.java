package org.tarsius.gui.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Photo;
import org.tarsius.bean.Tag;
import org.tarsius.gui.action.PhotoSelectionProvider;
import org.tarsius.gui.action.RemoveTagAction;
import org.tarsius.i18n.I18n;
import org.tarsius.util.RemoveTagActionComparator;

public class RemoveTagMenu extends JMenu {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(RemoveTagMenu.class);
	
	private PhotoSelectionProvider photoSelectionProvider = null;
	
	public RemoveTagMenu(PhotoSelectionProvider photoSelectionProvider) {
		super();
		setText(I18n.translate("RemoveTag"));
		setMnemonic(I18n.mnemonicKey("RemoveTag"));
		
		this.photoSelectionProvider = photoSelectionProvider;
		
		addMenuListener(new MenuListener(){
			public void menuSelected(MenuEvent e) {
				populateMenu();
			}
			public void menuCanceled(MenuEvent e) {}
			public void menuDeselected(MenuEvent e) {}
		});
	}
	
	private void populateMenu(){
		log.debug("Populating RemoveTag menu");
		this.removeAll();

		List<Photo> selectedPhotos = new ArrayList<Photo>();
		if (photoSelectionProvider != null) {
			selectedPhotos = photoSelectionProvider.getSelectedPhotos();
		}
		Hashtable<Tag, RemoveTagAction> tagActions = new Hashtable<Tag, RemoveTagAction>();
		for (Photo photo : selectedPhotos) {
			for (Tag tag : photo.getTags()) {
				if (!tagActions.containsKey(tag)) {
					RemoveTagAction action = new RemoveTagAction(tag,
							photoSelectionProvider);
					tagActions.put(tag, action);
				}
			}
		}
		// order by tag name
		List<RemoveTagAction> orderedActions = new ArrayList<RemoveTagAction>();
		orderedActions.addAll(tagActions.values());
		Collections.sort(orderedActions, new RemoveTagActionComparator());
		for(RemoveTagAction action : orderedActions){
			this.add(action);
		}
	}
	
}
