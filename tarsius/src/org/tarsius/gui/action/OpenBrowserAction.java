package org.tarsius.gui.action;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;

import org.tarsius.bean.Photo;
import org.tarsius.event.EventBelt;
import org.tarsius.gui.OpenBrowserEvent;
import org.tarsius.i18n.I18n;

public class OpenBrowserAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	private PhotoSelectionProvider photoSelectionProvider = null;
	
	public OpenBrowserAction() {
		putValue(NAME, I18n.translate("Browser"));
		putValue(MNEMONIC_KEY, I18n.mnemonicKey("Browser"));
	}

	public void actionPerformed(ActionEvent e) {
		Photo photo = null;
		if(photoSelectionProvider != null){
			List<Photo> photos = photoSelectionProvider.getSelectedPhotos();
			photo = !photos.isEmpty() ? photos.get(0) : null;
		}
		OpenBrowserEvent event = new OpenBrowserEvent(photo);
		EventBelt.getInstance().dispatch(event);
	}
	
	public void setPhotoSelectionProvider(
			PhotoSelectionProvider photoSelectionProvider) {
		this.photoSelectionProvider = photoSelectionProvider;
	}
	
}
