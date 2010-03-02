package org.tarsius;

import java.io.File;
import java.io.FileInputStream;
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
//		List<PhotoTemp> photoTempList = new ArrayList<PhotoTemp>();
		List<String> insertStatements = null;
		
		log.info("Importing photos");
		// import "current version" photos
//		String command[] = new String[]{"sqlite3.exe",
//			dataLoc + "photos.db",
//			"\"select p.id as photo_id, datetime(p.time, 'unixepoch') as date_shot, replace(p.directory_path, '/home/math/Photos', '') || '/' || name as path from photos as p;\""
//		};
//		Process p = Runtime.getRuntime().exec(command, null, new File("c:/Cygwin/bin/"));
//		FileInputStream photoDump = p.getInputStream();
		FileInputStream photoDump = new FileInputStream(new File(location + "photos.dump"));
		Scanner scanner = new Scanner(photoDump, "UTF-8");
		int maxPhotoId = 0;
		insertStatements = new ArrayList<String>();
		while(scanner.hasNextLine()){
			String[] tokens = scanner.nextLine().split("\\|");
			insertStatements.add(
				"insert into photo values (" + tokens[0] + ", '" +
				tokens[1] + "', '" + tokens[2] + "', TRUE);"
			);
			maxPhotoId = Math.max(Integer.parseInt(tokens[0]), maxPhotoId);
		}
		insertStatements.add("alter sequence seq_photo_id restart with " + (maxPhotoId + 1) + ";");
		Database.getInstance().executeBatch(insertStatements.toArray(new String[0]));

//		// import other versions of photos and link them with the "current version"
//		FileInputStream photoVersionsDump = new FileInputStream(new File(dataLoc + "photo_versions.dump"));
//		scanner = new Scanner(photoVersionsDump, "UTF-8");
//		insertStatements = new ArrayList<String>();
//		while(scanner.hasNextLine()){
//			String[] tokens = scanner.nextLine().split("\\|");
//			insertStatements.add(
//				"insert into photo values (NEXT VALUE FOR seq_photo_id, '" +
//				tokens[1] + "', '" + tokens[2] + "', TRUE, " + tokens[0] + ");"
//			);
//		}
//		Database.getInstance().executeBatch(insertStatements.toArray(new String[0]));

		log.info("Importing tags");
		addTagsForParentId(location, null);
	
		// TODO finish implementation of update of tagid sequence
//		insertStatements.add("alter sequence seq_tag_id restart with " + (maxTagId + 1) + ";");
//		Database.getInstance().executeBatch(insertStatements.toArray(new String[0]));
//		Database.getInstance().executeBatch(updateStatements.toArray(new String[0]));
		
		log.info("Importing photo/tag relationships");
//		p = Runtime.getRuntime().exec("sqlite3.exe " + dataLoc + "photos.db \"select pt.photo_id, pt.tag_id from photo_tags pt where photo_id in (select p.id from photos p where p.id = pt.photo_id) and tag_id in (select t.id from tags t where t.id = pt.tag_id);\"");
//		FileInputStream photoTagDump = p.getInputStream();
		FileInputStream photoTagDump = new FileInputStream(new File(location + "photos_tags.dump"));
		scanner = new Scanner(photoTagDump, "UTF-8");
		
		insertStatements = new ArrayList<String>();
		while(scanner.hasNextLine()){
			String[] tokens = scanner.nextLine().split("\\|");
			insertStatements.add("insert into photo_tag (photo_id, tag_id) values (" + tokens[0] + ", " + tokens[1] + ");");
//			insertStatements.add("insert into photo_tag (photo_id, tag_id) select photo_id (" + tokens[0] + ", " + tokens[1] + ");");
		}
		Database.getInstance().executeBatch(insertStatements.toArray(new String[0]));
		
		gallery.close();
	}
	
	private void addTagsForParentId(String dataLoc, Integer parentTagId) throws Exception {
//		Process p = Runtime.getRuntime().exec("sqlite3.exe -nullvalue null " + dataLoc + "photos.db \"select t.id as tag_id, t.name as name, (case when t.category_id = 0 then null else t.category_id end) as parent_tag_id from tags as t where t.category_id = " + parentTagId + ";\"");
//		String command = "sqlite3.exe -nullvalue null " + dataLoc + "photos.db \"select t.id as tag_id, t.name as name, (case when t.category_id = 0 then null else t.category_id end) as parent_tag_id from tags as t where t.category_id ";
//		if(parentTagId == null){
//			command += " = 0";
//		} else {
//			command += " = " + parentTagId;
//		}
//		command += ";\"";
//		Process p = Runtime.getRuntime().exec(command);
//		FileInputStream tagsDump =  p.getInputStream();
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
