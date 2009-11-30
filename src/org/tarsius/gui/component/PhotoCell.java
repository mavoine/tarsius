package org.tarsius.gui.component;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.Context;
import org.tarsius.bean.Photo;
import org.tarsius.bean.Tag;
import org.tarsius.imaging.ThumbnailsCallback;
import org.tarsius.imaging.ThumbnailsFactory;
import org.tarsius.util.DateUtil;
import org.tarsius.util.StringUtil;
import org.tarsius.util.TagNameComparator;

public class PhotoCell extends JPanel {
	
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(PhotoCell.class);
	
	// TODO get the default "selected" color for the L&F
	private static final Color selectedColor = new Color(100,160,245);
	private static final Color unselectedColor = Color.WHITE;
	private static final Border focusBorder =
		BorderFactory.createLineBorder(Color.ORANGE, 2);
	private static final Border blurBorder = 
		BorderFactory.createLineBorder(Color.WHITE, 2);

	// components
	private JLabel photoLabel = null;
	private JLabel tagsLabel = null;
	private JLabel dateLabel = null;
	private JPanel borderPanel = null;
	
	// properties
	private boolean hasFocus = false;
	private boolean isSelected = false;
	
	public PhotoCell(Photo photo) {
		log.trace("Constructor called");
		MigLayout cellLayout = new MigLayout(
				"",  // layout constraints
				"4[grow,center]4",  // column constraints
				"4[grow][][]4"); // row constraints
		this.setLayout(cellLayout);

		this.setBackground(Color.WHITE);

		MigLayout borderPanelLayout = new MigLayout(
				"",                 // layout constraints
				"2[grow,center]2",  // column constraints
				"2[grow,center]2"   // row constraints
		);
		borderPanel = new JPanel(borderPanelLayout);
		borderPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

		photoLabel = new JLabel();
		photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		photoLabel.setVerticalAlignment(SwingConstants.CENTER);
		photoLabel.setSize(
				ThumbnailsFactory.getInstance().getThumbnailMaxWidth(),
				ThumbnailsFactory.getInstance().getThumbnailMaxHeight());
//		photoLabel.setText("loading..."); // TODO add default icon meaning "loading..."
		
		borderPanel.add(photoLabel);
		
		dateLabel = new JLabel();
		dateLabel.setText(DateUtil.formatDate(photo.getDate()));
		
		// TODO fix width problem with the tags label
		tagsLabel = new JLabel();
		tagsLabel.setText(buildTagList(photo.getTags()));

		this.add(borderPanel, "grow, wrap");
		this.add(dateLabel, "wrap");
		this.add(tagsLabel, "");
		
		setSelected(false);
		setHasFocus(false);
		
		// load the thumbnail
		ThumbnailsFactory.getInstance().getThumbnail(Context.getGallery().getPhotosPath(), photo.getPath(),
			new ThumbnailsCallback() {
				public void execute(Image image) {
					photoLabel.setIcon(new ImageIcon(image));
					borderPanel.revalidate();
				}
		});
	}
	
	private String buildTagList(List<Tag> tags){
		String tagsList = "";
		if(tags != null) {
			// copy the list of tags and sort it
			List<Tag> sortedTags = new ArrayList<Tag>();
			sortedTags.addAll(tags);
			Collections.sort(sortedTags, new TagNameComparator());
			// make a list
			for(Tag tag : sortedTags){
				tagsList = StringUtil.concat(tagsList, tag.getName());
			}
		}
		return tagsList.length() > 0 ? tagsList : " ";
	}
	
	public void setSelected(boolean isSelected){
		this.isSelected = isSelected;
		if(isSelected){
			borderPanel.setBackground(selectedColor);
		} else {
			borderPanel.setBackground(unselectedColor);
		}
	}
	
	public boolean isSelected(){
		return this.isSelected;
	}
	
	public void setHasFocus(boolean hasFocus){
		this.hasFocus = hasFocus;
		if(hasFocus){
			this.setBorder(focusBorder);
		} else {
			this.setBorder(blurBorder);
		}
	}
	
	public boolean hasFocus(){
		return this.hasFocus;
	}
	
}
