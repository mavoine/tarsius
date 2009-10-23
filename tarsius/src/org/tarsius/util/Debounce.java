package org.tarsius.util;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Debounce {
	
	private static Log log = LogFactory.getLog(Debounce.class);
	
	private Timer timer = null;
	private long lastHit = 0;
	private long debounceDelay = 0;
	private long checkDelay = 0;
	
	public abstract void execute();
	
	public Debounce(long debounceDelay, long checkDelay) {
		this.debounceDelay = debounceDelay;
		this.checkDelay = checkDelay;
	}
	
	public void hit(){
		log.trace("hit");
		lastHit = System.currentTimeMillis();
		if(this.timer != null){
			this.timer.cancel();
			this.timer = null;
		}
		this.timer = new Timer("Debounce", true);
		this.timer.schedule(new DebounceTask(this), 0, checkDelay);
	}
	
	private void checkExecute(){
		if((System.currentTimeMillis() - lastHit) > debounceDelay){
			log.trace("Debounce passed, call execute");
			this.timer.cancel();
			this.timer = null;
			execute();
		} else {
			log.trace("Not yet, waiting");
		}
	}
	
	private class DebounceTask extends TimerTask {
		
		private Debounce debounceInstance = null;
		
		public DebounceTask(Debounce debounceInstance) {
			this.debounceInstance = debounceInstance;
		}
		
		@Override
		public void run() {
			debounceInstance.checkExecute();
		}
	}

}
