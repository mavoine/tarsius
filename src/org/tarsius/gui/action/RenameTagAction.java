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

public class RenameTagAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(RenameTagAction.class);
	
	private TagSelectionProvider tagSelectionProvider = null;
	
	public RenameTagAction(TagSelectionProvider tagSelectionProvider){
		super(I18n.translate("RenameTag"));
		putValue(MNEMONIC_KEY, I18n.mnemonicKey("RenameTag"));
		this.tagSelectionProvider = tagSelectionProvider;
	}
	
	public void actionPerformed(ActionEvent event) {
		log.debug("Rename tag action invoked");
		Tag tag = tagSelectionProvider.getSelectedTag();
		try {
			
			String name = readName(tag);
			// validate name
			while (name != null && name.length() == 0){
				JOptionPane.showMessageDialog(null, I18n
						.translate("InputNewTagName"));
				name = readName(tag);
			}

			// if user clicked Cancel
			if(name == null){
				return;
			}

			tag.setName(name);
			// TODO check that the name does not exist already
			TagDAO.getInstance().updateTag(tag);
		} catch (PersistenceException e) {
			// TODO notify user
			log.error("Failed to delete tag", e);
		} finally {
			EventBelt.getInstance().dispatch(Event.Type.TAGS_CHANGED);
		}
	}
	
	private String readName(Tag tag){
		return (String)JOptionPane.showInputDialog(null, 
				I18n.translate("InputNewTagName"), 
				I18n.translate("RenameTag"), 
				JOptionPane.PLAIN_MESSAGE,
				null,
				null,
				tag.getName());
	}

}
