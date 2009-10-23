package org.tarsius.task;

import javax.swing.SwingWorker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class BackgroundTask extends SwingWorker {
	
	private static Log log = LogFactory.getLog(BackgroundTask.class);
	
	protected abstract void executeTask() throws TaskException;
	
	@Override
	final protected Object doInBackground() throws Exception {
		try {
			executeTask();
		} catch (Exception ex){
			log.error("Task failed", ex);
		}
		return null;
	}

}
