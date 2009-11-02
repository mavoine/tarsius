package org.tarsius.gui.action;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.gui.ZoomDescriptor;
import org.tarsius.gui.ZoomLevelChangeListener;
import org.tarsius.i18n.I18n;

public class ChangeZoomLevelAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(ChangeZoomLevelAction.class);

	private ZoomDescriptor zoomDescriptor = null;
	private Set<ZoomLevelChangeListener> zoomLevelChangeListeners = null;

	public ChangeZoomLevelAction(ZoomDescriptor zoomDescriptor) {
		String name = null;
		Integer mnemonicKey = null;
		if(zoomDescriptor.getZoomType() == ZoomDescriptor.ZoomType.FIT_TO_WINDOW){
			name = I18n.translate("ZoomFitToWindow");
			mnemonicKey = I18n.mnemonicKey("ZoomFitToWindow");
		} else {
			name = I18n.translate("ZoomPercent", zoomDescriptor.getValue());
			mnemonicKey = zoomDescriptor.getValue() == 100 ? 
					new Integer((int)'0') : 
					new Integer((int)zoomDescriptor.getValue().toString().charAt(0));
		}
		putValue(NAME, name);
		putValue(MNEMONIC_KEY, mnemonicKey);
		
		this.zoomDescriptor = zoomDescriptor;
		this.zoomLevelChangeListeners = new HashSet<ZoomLevelChangeListener>();
	}
	
	public void actionPerformed(ActionEvent event) {
		log.debug("Change zoom level to " + zoomDescriptor);
		for(ZoomLevelChangeListener listener : this.zoomLevelChangeListeners){
			try {
				listener.changeZoomLevel(this.zoomDescriptor);
			} catch (Exception e){
				log.debug("Failed to notify listener", e);
			}
		}
	}
	
	public void addChangeListener(ZoomLevelChangeListener listener){
		this.zoomLevelChangeListeners.add(listener);
	}
	
	public void removeChangeListener(ZoomLevelChangeListener listener){
		this.zoomLevelChangeListeners.remove(listener);
	}

}
