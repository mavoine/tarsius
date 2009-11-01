package org.tarsius;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImageDisplayTest {
	
	private static Log log = LogFactory.getLog(ThumbnailTest.class);

	JFrame frame = null;
	JButton button = new JButton("Go");
	JLabel label = new JLabel();
	ImageIcon icon = new ImageIcon();
	JLabel labelJAI = new JLabel();
	ImageIcon iconJAI = new ImageIcon();

	public static void main(String[] args) {
		ImageDisplayTest t = new ImageDisplayTest();
		t.run();
	}
	
	public void run(){
		BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		icon.setImage(image);
		label.setIcon(icon);
		iconJAI.setImage(image);
		labelJAI.setIcon(iconJAI);
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		frame.add(button);
		frame.add(label);
		frame.add(labelJAI);
		frame.pack();
		
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				go();
			}
		});
		
		frame.setVisible(true);
	}
	
	public void go(){
		log.debug("go");
		File imageFile = new File("/home/user/MMC-SD/CANON_DC/dev/test/IMG_1810.JPG");
		
		// TODO use an ImageObserver and graphics2D  to control the painting into a JPanel or similar component
		
		ImageObserver io = new ImageObserver(){
			public boolean imageUpdate(Image img, int infoflags, int x, int y,
					int width, int height) {
				if((infoflags | ImageObserver.ALLBITS) > 0){
					log.debug("image finished loading");
					return true;
				}
				log.debug(infoflags);
				return false;
			}
		};
		
		try {
			log.debug("read image");
			BufferedImage image = ImageIO.read(imageFile);
			log.debug("begin prepare");
			frame.prepareImage(image, 50, 50, io);
			log.debug("done prepare");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private int getThumbnailMaxHeight() {
		return 256;
	}

	private int getThumbnailMaxWidth() {
		return 256;
	}
	
	private long now(){
		return System.currentTimeMillis();
	}

}
