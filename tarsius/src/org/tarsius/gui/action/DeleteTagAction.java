package org.tarsius.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Tag;
import org.tarsius.event.Event;
import org.tarsius.event.EventBelt;
import org.tarsius.i18n.I18n;
import org.tarsius.persistence.PersistenceException;
import org.tarsius.persistence.dao.TagDAO;

public class DeleteTagAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(DeleteTagAction.class);
	
	private TagSelectionProvider tagSelectionProvider = null;

	public DeleteTagAction(TagSelectionProvider tagSelectionProvider){
		super(I18n.translate("DeleteTag"));
		putValue(MNEMONIC_KEY, I18n.mnemonicKey("DeleteTag"));
		this.tagSelectionProvider = tagSelectionProvider;
	}
	
	public void actionPerformed(ActionEvent event) {
		// TODO implement rename tag
		log.debug("Delete tag action invoked");
		Tag tag = tagSelectionProvider.getSelectedTag();
		try {
			TagDAO.getInstance().deleteTag(tag);
		} catch (PersistenceException e) {
			// TODO notify user
			log.error("Failed to delete tag", e);
		} finally {
			EventBelt.getInstance().dispatch(Event.Type.TAGS_CHANGED);
		}
	}

}
