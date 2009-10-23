package org.tarsius.gui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.plaf.BorderUIResource;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Photo;
import org.tarsius.config.Resources;
import org.tarsius.gui.event.ComponentResizeListener;
import org.tarsius.util.ClipUtil;
import org.tarsius.util.Debounce;

public class PhotoFrame_bk extends JPanel {
	
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(PhotoFrame_bk.class);
	
	private Photo photo = null;
	private JLabel photoLabel = null;
	
	public PhotoFrame_bk() {
		MigLayout frameLayout = new MigLayout(
				"",  // layout constraints
				"0[grow]0",  // column constraints
				"0[grow]0"); // row constraints
		
		this.setLayout(frameLayout);

		photoLabel = new JLabel();
		photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		photoLabel.setVerticalAlignment(SwingConstants.CENTER);
		// TODO figure how to set the border color to "system"
		photoLabel.setBorder(BorderUIResource.getBlackLineBorderUIResource());
		this.add(photoLabel, "grow, w 250:250:, h 250:250:, center");
		this.setForeground(Color.WHITE);
		this.setBackground(Color.WHITE);
		
		this.addComponentListener(new ComponentResizeListener(){
			Debounce resizeDebounce = new Debounce(500, 100){
				@Override
				public void execute() {
					update();
				}
			};
			public void resized() {
				resizeDebounce.hit();
			}
		});
	}
		
	public void setPhoto(Photo photo){
		this.photo = photo;
		update();
	}
			
	private void update(){
		// load the image and display
		BufferedImage bufferedImage = null;
		Image image = null;
		try {
			File file = new File(photo.getAbsolutePath());
			bufferedImage = ImageIO.read(file);
			Dimension frameSize = new Dimension(photoLabel.getWidth(), 
					photoLabel.getHeight());
			Dimension photoSize = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
			Dimension clipSize = ClipUtil.clip(photoSize, frameSize);
			image = bufferedImage.getScaledInstance(clipSize.width, 
					clipSize.height, Image.SCALE_SMOOTH);
		} catch (Exception ex){
			image = Resources.getInstance().getNotFoundIcon();
		} finally {
			ImageIcon icon = new ImageIcon(image);
			photoLabel.setIcon(icon);
		}
	}

}
