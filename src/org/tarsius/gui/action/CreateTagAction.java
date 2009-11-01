package org.tarsius.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Tag;
import org.tarsius.event.Event;
import org.tarsius.event.EventBelt;
import org.tarsius.i18n.I18n;
import org.tarsius.persistence.PersistenceException;
import org.tarsius.persistence.dao.TagDAO;

public class CreateTagAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(CreateTagAction.class);
	
	private TagSelectionProvider tagSelectionProvider = null;
	
	public CreateTagAction(TagSelectionProvider tagSelectionProvider){
		super(I18n.translate("CreateTag"));
		putValue(MNEMONIC_KEY, I18n.mnemonicKey("CreateTag"));
		this.tagSelectionProvider = tagSelectionProvider;
	}
	
	public void actionPerformed(ActionEvent event) {
		log.debug("Create tag action invoked");
		Tag parent = tagSelectionProvider.getSelectedTag();

		String name = readName();
		// validate name
		while (name != null && name.length() == 0){
			JOptionPane.showMessageDialog(null, I18n.translate("InputTagName"));
			name = readName();
		}
		// if user clicked Cancel
		if(name == null){
			return;
		}
		
		Tag tag = new Tag();
		tag.setIdParent(parent != null ? parent.getId() : null);
		tag.setName(name);
		try {
			TagDAO.getInstance().insertTag(tag);
		} catch (PersistenceException e) {
			// TODO notify user
			log.error("Failed to create new tag", e);
		}
		EventBelt.getInstance().dispatch(Event.Type.TAGS_CHANGED);
	}
	
	private String readName(){
		return JOptionPane.showInputDialog(null, 
				I18n.translate("InputTagName"), 
				I18n.translate("CreateTag"), 
				JOptionPane.PLAIN_MESSAGE);
	}
	
}
