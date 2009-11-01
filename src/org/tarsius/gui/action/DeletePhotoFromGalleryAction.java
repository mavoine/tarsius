package org.tarsius.gui.action;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Photo;
import org.tarsius.event.Event;
import org.tarsius.event.EventBelt;
import org.tarsius.i18n.I18n;
import org.tarsius.task.DeletePhotoTask;

public class DeletePhotoFromGalleryAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory
			.getLog(DeletePhotoFromGalleryAction.class);

	private PhotoSelectionProvider photoSelectionProvider = null;

	public DeletePhotoFromGalleryAction(
			PhotoSelectionProvider photoSelectionProvider) {
		super(I18n.translate("DeletePhotoFromGallery"));
		putValue(MNEMONIC_KEY, I18n.mnemonicKey("DeletePhotoFromGallery"));
		this.photoSelectionProvider = photoSelectionProvider;
	}

	public void actionPerformed(ActionEvent event) {
		log.debug("Delete photo from gallery action invoked");
		List<Photo> photos = photoSelectionProvider.getSelectedPhotos();
		// if(photos.isEmpty()){
		// JOptionPane.showMessageDialog(null,
		// I18n.translate("SelectPhotoFirst"));
		// return;
		// }
		int answer = JOptionPane.showConfirmDialog(null, I18n
				.translate("ConfirmDeletePhotoFromGallery"), I18n
				.translate("Confirmation"), JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (answer == JOptionPane.OK_OPTION) {
			try {
				DeletePhotoTask task = new DeletePhotoTask(photos, false);
				task.execute();
			} catch (Exception e) {
				log.error("Failed to delete photo", e);
				// TODO notify user
			} finally {
				EventBelt.getInstance().dispatch(Event.Type.GALLERY_CHANGED);
			}
		}
	}

}
