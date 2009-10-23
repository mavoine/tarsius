package org.tarsius.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImportDirectoryScanner extends DirectoryWalker {

	private Log log = LogFactory.getLog(ImportDirectoryScanner.class);
	
	private boolean cancelled = false;
	private List<String> filePaths = null;
	private int numDirScanned = 0;
	private int numPhotosFound = 0;
	
	public ImportDirectoryScanner() {
		super(null, new ImportFileExtensionFilter(), -1);
	}
	
	public List<String> scan(File rootDirectory){
		try {
			log.debug("Begin scanning");
			filePaths = new ArrayList<String>();
			numDirScanned = 0;
			numPhotosFound = 0;
			walk(rootDirectory, null);
			log.debug("Done scanning");
		} catch (IOException e) {
			log.error("Error while scanning directory", e);
		}
		return filePaths;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleFile(File file, int depth, Collection results)
			throws IOException {
		if(log.isTraceEnabled()){
			log.debug("Match found: " + file.getAbsolutePath());
		}
		filePaths.add(file.getAbsolutePath());
		numPhotosFound++;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void handleDirectoryStart(File directory, int depth,
			Collection results) throws IOException {
		numDirScanned++;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected boolean handleIsCancelled(File file, int depth, 
			Collection results) throws IOException {
		return cancelled;
	}
	
	public void cancel(){
		cancelled = true;
	}

}
