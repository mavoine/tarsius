package org.tarsius.gui;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.gui.ZoomDescriptor.ZoomType;
import org.tarsius.gui.action.ChangeZoomLevelAction;
import org.tarsius.gui.action.GoToNextAction;
import org.tarsius.gui.action.GoToPreviousAction;
import org.tarsius.gui.action.OpenBrowserAction;
import org.tarsius.gui.component.PhotoFrame;
import org.tarsius.gui.component.ZoomMenuItem;
import org.tarsius.i18n.I18n;

public class ViewerPane extends JPanel implements Perspective {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(ViewerPane.class);
	
	// components
	protected JPanel photoPanel = null;
	protected JPanel infoPanel = null;
	protected JLabel fileNameLabel = null;
	protected JLabel dateLabel = null;
	protected PhotoFrame photoFrame = null;
	protected JButton previousButton = null;
	protected JButton nextButton = null;
	
	// menus
	protected JMenu viewMenu = null;
	protected JMenu goMenu = null;
	
	// actions
	protected ChangeZoomLevelAction zoomFitToWindowAction = null;
	protected List<ChangeZoomLevelAction> zoomPercentActions = null;
	protected OpenBrowserAction openBrowserAction = null;
	protected GoToNextAction goToNextAction = null;
	protected GoToPreviousAction goToPreviousAction = null;
	
	// defaults
	ZoomDescriptor defaultZoomLevel = new ZoomDescriptor(ZoomType.FIT_TO_WINDOW,
			null);
	
	public ViewerPane() {
		
		log.debug("Building Viewer");
		
		// set layout
		MigLayout frameLayout = new MigLayout(
				"",              // layout constraints
				"[][grow]",      // column constraints
				"[grow]");             // row constraints
		this.setLayout(frameLayout);
		
		// create actions
		zoomFitToWindowAction = new ChangeZoomLevelAction(
				new ZoomDescriptor(
				ZoomType.FIT_TO_WINDOW, null));
		zoomPercentActions = new ArrayList<ChangeZoomLevelAction>();
		for (int i = 10; i <= 100; i += 10) {
			zoomPercentActions.add(new ChangeZoomLevelAction(
					new ZoomDescriptor(ZoomType.PERCENTAGE, i)));
		}
		openBrowserAction = new OpenBrowserAction();
		goToNextAction = new GoToNextAction();
		goToPreviousAction = new GoToPreviousAction();

		// create components
		//
		// info panel
		MigLayout infoPanelLayout = new MigLayout();
		infoPanelLayout.setLayoutConstraints("wrap 1");
		infoPanelLayout.setColumnConstraints("");
		infoPanelLayout.setRowConstraints("");
		JLabel infoTitle = new JLabel();
		infoTitle.setText(I18n.translate("Info"));
		infoPanel = new JPanel(infoPanelLayout);
		fileNameLabel = new JLabel("");
		dateLabel = new JLabel("");
		infoPanel.add(infoTitle, "");
		infoPanel.add(fileNameLabel, "");
		infoPanel.add(dateLabel, "");
		
		previousButton = new JButton(goToPreviousAction);
		previousButton.setText("<<");
		nextButton = new JButton(goToNextAction);
		nextButton.setText(">>");
		infoPanel.add(previousButton);
		infoPanel.add(nextButton);
		
		// photo display area
		photoFrame = new PhotoFrame(defaultZoomLevel);
		zoomFitToWindowAction.addChangeListener(photoFrame);
		for(ChangeZoomLevelAction action : zoomPercentActions){
			action.addChangeListener(photoFrame);
		}

		// assemble the dialog
		JScrollPane scrollPane = new JScrollPane(infoPanel);
		this.add(scrollPane, "grow");
		this.add(photoFrame, "grow, w 250:250:, h 250:250:");

		this.validate();
		this.doLayout();
		
		// create menu
		// view
		viewMenu = new JMenu(I18n.translate("View"));
		viewMenu.setMnemonic(I18n.mnemonic("View"));
		ButtonGroup group = new ButtonGroup();
		JMenu zoomMenu = new JMenu(I18n.translate("Zoom"));
		zoomMenu.setMnemonic(I18n.mnemonic("Zoom"));
		ZoomMenuItem zoomFitTowindowMenuItem = new ZoomMenuItem(group,
				zoomFitToWindowAction);
		zoomFitTowindowMenuItem.setSelected(true);
		zoomMenu.add(zoomFitTowindowMenuItem);
		for(ChangeZoomLevelAction zoomPercentAction : zoomPercentActions){
			zoomMenu.add(new ZoomMenuItem(group, zoomPercentAction));
		}
		viewMenu.add(zoomMenu);
		goMenu = new JMenu(I18n.translate("Go"));
		goMenu.setMnemonic(I18n.mnemonic("Go"));
		goMenu.add(openBrowserAction);
		
	}

	public Container getContentPane() {
		return this;
	}
	
	public JMenu[] getMenus() {
		return new JMenu[]{viewMenu,goMenu};
	}
	
//	private class ImageCanvas extends JComponent {
//		
//		private static final long serialVersionUID = 1L;
//
////		private Image image = null;
//		private File photoFile = null;
////		private VolatileImage backBuffer = null;
////		private BufferStrategy strategy = null;
//		
//		public ImageCanvas() {
//			super();
//			createBufferStrategy(2);
//		}
//		
//		@Override
//		public void paint(Graphics g) {
//			if(photoFile == null){
//				super.paint(g);
//				return;
//			}
//						
////			if (backBuffer == null || bufferSizeDiffer()) {
////				backBuffer = this.createVolatileImage(this.getWidth(), this
////						.getHeight());
////				Painter painter = new Painter();
////				painter.setImage(photoFile);
////				painter.setBackBuffer(backBuffer);
////				painter.execute();
////			} else {
////				this.getGraphics().drawImage(backBuffer, 0, 0, null);
////			}
//			
//			// TODO explore usage of DisplayJAI
//			
////			Dimension imageDimension = new Dimension(image.getWidth(null),
////					image.getHeight(null));
////			Dimension canvasDim = new Dimension(this.getWidth(), this
////					.getHeight());
////			Dimension clip = ClipUtil.clip(imageDimension, canvasDim);
////			int xOffset = (int)(this.getWidth() - clip.getWidth()) / 2;
////			int yOffset = (int)(this.getHeight() - clip.getHeight()) / 2;
////			g.drawImage(image, xOffset, yOffset, (int) clip.getWidth(),
////					(int) clip.getHeight(), null);
//
//			
////			BufferedImage image = null;
////			try {
////				image = ImageOperationUtil.rotateAndScale(
////						this.photoFile, this.getWidth(), this.getHeight());
////			} catch (IOException e) {
////				// FIXME render default "not found" image
////				return;
////			}
////
////			int xOffset = (int)(this.getWidth() - image.getWidth()) / 2;
////			int yOffset = (int)(this.getHeight() - image.getHeight()) / 2;
////			g.drawImage(image, xOffset, yOffset, image.getWidth(),
////					image.getHeight(), null);
//			
//			
//			
//			
//		}
//		
////		private boolean bufferSizeDiffer() {
////			return backBuffer != null
////					&& backBuffer.getWidth() == this.getWidth()
////					&& backBuffer.getHeight() == this.getHeight();
////		}
//		
//	}
//	
//	private class Painter extends SwingWorker {
//		private File imageFile = null;
////		private VolatileImage backBuffer = null;
//		private BufferStrategy strategy = null;
//		private int width = 0;
//		private int height = 0;
//		@Override
//		protected Object doInBackground() throws Exception {
//			log.debug("Painter execution");
////			BufferedImage image = ImageOperationUtil.rotateAndScale(imageFile,
////					backBuffer.getWidth(), backBuffer.getHeight());
//			BufferedImage image = ImageOperationUtil.rotateAndScale(imageFile,
//					width, height);
//
//			Dimension imageDimension = new Dimension(image.getWidth(null),
//			image.getHeight(null));
//			Dimension canvasDim = new Dimension(width, height);
//			Dimension clip = ClipUtil.clip(imageDimension, canvasDim);
//			int xOffset = (int)(width - clip.getWidth()) / 2;
//			int yOffset = (int)(height - clip.getHeight()) / 2;
//			
//			strategy.getDrawGraphics().drawImage(image, xOffset, yOffset, (int) clip.getWidth(),
//					(int) clip.getHeight(), null);
//			strategy.show();
//			
//			return null;
//		}
//		public void setImage(File imageFile){
//			this.imageFile = imageFile;
//		}
//		public void setStrategy(BufferStrategy strategy){
//			this.strategy = strategy;
//		}
//		public void setSize(int width, int height){
//			this.width = width;
//			this.height = height;
//		}
////		public void setBackBuffer(VolatileImage backBuffer){
////			this.backBuffer = backBuffer;
////		}
//	};
//


}
