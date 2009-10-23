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
import javax.swing.border.Border;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Photo;
import org.tarsius.bean.Tag;
import org.tarsius.imaging.ThumbnailsFactory;
import org.tarsius.util.DateUtil;
import org.tarsius.util.StringUtil;
import org.tarsius.util.TagNameComparator;

public class PhotoCell extends JPanel {
	
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(PhotoCell.class);
	
	// TODO get the default "selected" color for the L&F
	private static Color selectedColor = new Color(100,160,245);
	private static Border focusBorder =
		BorderFactory.createLineBorder(Color.ORANGE, 2);
	private static Border blurBorder = 
		BorderFactory.createLineBorder(Color.WHITE, 2);

	private JLabel photoLabel = null;
	private JLabel tagsLabel = null;
	private JLabel dateLabel = null;
	
	public PhotoCell(Photo photo, boolean hasFocus, boolean isSelected) {
		log.trace("Constructor called");
		MigLayout cellLayout = new MigLayout(
				"",  // layout constraints
				"4[grow,center]4",  // column constraints
				"4[grow][][]4"); // row constraints
		this.setLayout(cellLayout);

		this.setBackground(Color.WHITE);

		// load the thumbnail
		Image thumbnail = ThumbnailsFactory.getInstance().getThumbnail(
				photo.getAbsolutePath());

		MigLayout borderPanelLayout = new MigLayout(
				"",  // layout constraints
				"2[grow,center]2",  // column constraints
				"2[grow,center]2"); // row constraints
		JPanel borderPanel = new JPanel(borderPanelLayout);
		borderPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

		photoLabel = new JLabel();
		photoLabel.setVerticalTextPosition(JLabel.BOTTOM);
		photoLabel.setHorizontalTextPosition(JLabel.CENTER);
		photoLabel.setIcon(new ImageIcon(thumbnail));
		
		borderPanel.add(photoLabel, "center");
		
		dateLabel = new JLabel();
		dateLabel.setText(DateUtil.formatDate(photo.getDate()));
		
		// TODO fix width problem with the tags label
		tagsLabel = new JLabel();
		tagsLabel.setText(buildTagList(photo.getTags()));

		this.add(borderPanel, "grow, wrap");
		this.add(dateLabel, "wrap");
		this.add(tagsLabel, "");
		
		if(hasFocus){
			this.setBorder(focusBorder);
		} else {
			this.setBorder(blurBorder);
		}

		if(isSelected){
			borderPanel.setBackground(selectedColor);
		} else {
			borderPanel.setBackground(Color.WHITE);
		}
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
	
}
