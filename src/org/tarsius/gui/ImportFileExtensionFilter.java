package org.tarsius.gui;

import java.io.File;

import org.apache.commons.io.filefilter.IOFileFilter;

public class ImportFileExtensionFilter implements IOFileFilter {
		
	// TODO parameterize file extensions
	String[] supportedFileExtensions = new String[]{".jpg"};
	
	public ImportFileExtensionFilter() {
	}
	
	public boolean accept(File file) {
		return accept(file.getName());
	}

	public boolean accept(File dir, String name) {
		return accept(name);
	}
	
	private boolean accept(String name){
		boolean accept = false;
		for(String fileExtension : supportedFileExtensions){
			if(name.toLowerCase().endsWith(fileExtension)){
				accept = true;
				break;
			}
		}
		return accept;
	}
	
}
