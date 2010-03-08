package org.tarsius;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tarsius.bean.Tag;
import org.tarsius.persistence.Database;
import org.tarsius.persistence.dao.TagDAO;


public class ImportFSpotDB {
	
	private static Log log = LogFactory.getLog(ImportFSpotDB.class);
	
	public static void main(String[] args) throws Exception {
		
		String location = args[0];
		
		ImportFSpotDB i = new ImportFSpotDB();
		i.run(location);
		
	}
	
	private void run(String location) throws Exception {

		if(!location.endsWith(File.separator)){
			location = location.concat(File.separator);
		}

		log.info("Create gallery");
		File galleryFolder = new File(location + "gallery");
		if(galleryFolder.exists()){
			int answer = JOptionPane.showConfirmDialog(null, "A gallery already exists at the path specified, delete it?");
			if(answer == JOptionPane.YES_OPTION){
				FileUtils.deleteDirectory(galleryFolder);
			} else {
				log.info("Execution cancelled; gallery already exists");
				System.exit(1);
			}
		}
		Gallery gallery = new Gallery();
		gallery.createGallery(galleryFolder.getPath());
		
		log.info("Reading dump files");
		List<String> insertStatements = null;
		
		log.info("Importing photos");
		// import "current version" photos
		FileInputStream photoDump = new FileInputStream(new File(location + "photos.dump"));
		Scanner scanner = new Scanner(photoDump, "UTF-8");
		int maxPhotoId = 0;
		insertStatements = new ArrayList<String>();
		while(scanner.hasNextLine()){
			String[] tokens = scanner.nextLine().split("\\|");
			insertStatements.add(
				"insert into photo (photo_id, date_shot, path, is_path_relative, version_photo_id) values (" + tokens[0] + ", '" +
				tokens[1] + "', '" + tokens[2] + URLDecoder.decode(tokens[3],"UTF-8") + "', TRUE, null);"
			);
			maxPhotoId = Math.max(Integer.parseInt(tokens[0]), maxPhotoId);
		}
		insertStatements.add("alter sequence seq_photo_id restart with " + (maxPhotoId + 1) + ";");
		Database.getInstance().executeBatch(insertStatements.toArray(new String[0]));

		// import other versions of photos and link them with the "current version"
		FileInputStream photoVersionsDump = new FileInputStream(new File(location + "photo_versions.dump"));
		scanner = new Scanner(photoVersionsDump, "UTF-8");
		insertStatements = new ArrayList<String>();
		while(scanner.hasNextLine()){
			String[] tokens = scanner.nextLine().split("\\|");
			insertStatements.add(
				"insert into photo (photo_id, date_shot, path, is_path_relative, version_photo_id) values (NEXT VALUE FOR seq_photo_id, '" +
				tokens[1] + "', '" + tokens[2] + URLDecoder.decode(tokens[3],"UTF-8") + "', TRUE, " + tokens[0] + ");"
			);
		}
		Database.getInstance().executeBatch(insertStatements.toArray(new String[0]));

		log.info("Importing tags");
		addTagsForParentId(location, null);
	
		// TODO finish implementation of update of tagid sequence
//		insertStatements.add("alter sequence seq_tag_id restart with " + (maxTagId + 1) + ";");
//		Database.getInstance().executeBatch(insertStatements.toArray(new String[0]));
//		Database.getInstance().executeBatch(updateStatements.toArray(new String[0]));
		
		log.info("Importing photo/tag relationships");
		FileInputStream photoTagDump = new FileInputStream(new File(location + "photos_tags.dump"));
		scanner = new Scanner(photoTagDump, "UTF-8");
		
		insertStatements = new ArrayList<String>();
		while(scanner.hasNextLine()){
			String[] tokens = scanner.nextLine().split("\\|");
			insertStatements.add("insert into photo_tag (photo_id, tag_id) values (" + tokens[0] + ", " + tokens[1] + ");");
		}
		Database.getInstance().executeBatch(insertStatements.toArray(new String[0]));
		
		gallery.close();
	}
	
	private void addTagsForParentId(String dataLoc, Integer parentTagId) throws Exception {
		FileInputStream tagsDump = new FileInputStream(new File(dataLoc + "tags.dump"));
		Scanner scanner = new Scanner(tagsDump, "UTF-8");
		while(scanner.hasNextLine()){
			String[] tokens = scanner.nextLine().split("\\|");
			
			Integer id = Integer.valueOf(tokens[0]);
			String name = tokens[1];
			Integer parentId = "null".equalsIgnoreCase(tokens[2]) ? null : Integer.valueOf(tokens[2]);
			if ((parentId == null && parentTagId == null)
					|| (parentId != null && parentTagId != null && parentId.equals(parentTagId))) {
				Tag tag = new Tag();
				tag.setId(id);
				tag.setName(name);
				tag.setIdParent(parentId);
				TagDAO.getInstance().insertTag(tag);
				addTagsForParentId(dataLoc, tag.getId());
			}
		}
	}

}
