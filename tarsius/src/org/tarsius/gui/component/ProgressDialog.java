package org.tarsius.gui.component;

import java.awt.Dialog;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;

import org.tarsius.gui.ProgressObserver;
import org.tarsius.i18n.I18n;

public class ProgressDialog extends JDialog implements ProgressObserver {
	
	private static final long serialVersionUID = 1L;
	public static enum TextStatusType {Percentage, Steps}
	
	// components
	private JLabel infoLabel = null;
	private JProgressBar progressBar = null;
	private JLabel statusLabel = null;
//	private JLabel progressLabel = null;
	
	// behavior
	private int min = 0;
	private int max = 100;
	private int progress = 0;
	private TextStatusType textStatusType = TextStatusType.Percentage; 

	public ProgressDialog(String title, String info, boolean useStatus,
			TextStatusType textStatusType, int min, int max, int initial) {
		this.min = min;
		this.max = max;
		this.progress = initial;
		this.textStatusType = textStatusType;
		
		this.setTitle(title);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		
		MigLayout layout = new MigLayout(
				"",  // layout constraints
				"[grow]",  // column constraints
				""); // row constraints
		this.setLayout(layout);

		infoLabel = new JLabel(info);
		progressBar = new JProgressBar(this.min, this.max);
		progressBar.setValue(progress);
		progressBar.setStringPainted(true);
		updateProgressLabel();
		
		statusLabel = new JLabel(" ");
		
		this.add(infoLabel, "wrap");
		if(useStatus){
			this.add(statusLabel, "width 300, grow, wrap");
		}
		this.add(progressBar, "grow, wrap");
		this.add(new JLabel(" "));

		this.pack();
	}

	public void setMaximum(int max) {
		this.max = max;
	}

	public void setMinimum(int min) {
		this.min = min;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		this.progressBar.setValue(progress);
		updateProgressLabel();
		// if the dialog is visible and the task is done
		if(this.isVisible() && this.progress >= this.max){
			this.dispose();
			return;
		}
		// if the dialog is still hidden and the task has started
		if(!this.isVisible() && this.progress > this.min){
			// pop-up
			makeVisible();
		}
		this.repaint();
	}
	
	private void makeVisible(){
		Thread t = new Thread(new MakeVisible(this));
		t.start();
	}

	public void setStatus(String statusText) {
		this.statusLabel.setText(statusText);
	}

	public void finished() {
		this.dispose();
	}
	
	private void updateProgressLabel(){
		if (this.textStatusType == TextStatusType.Percentage) {
			int percent = (this.progress * 100 / (this.max - this.min));
			progressBar.setString(I18n.translate("ProgressPercent", percent));
		} else if (this.textStatusType == TextStatusType.Steps) {
			progressBar.setString(I18n.translate("ProgressSteps",
					this.progress, this.max));
		}
	}
	
	private class MakeVisible implements Runnable {
		private JDialog dialog = null;
		public MakeVisible(JDialog dialog) {
			this.dialog = dialog;
		}
		public void run() {
			this.dialog.setVisible(true);
		}
	}
	
}
