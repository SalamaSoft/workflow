package com.salama.workflow.engine.persistence.disk;

import java.io.Serializable;

public class DiskFilePersistenceContext implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1459698056182347684L;
	
	private String _baseDir = "";

	public String getBaseDir() {
		return _baseDir;
	}

	public void setBaseDir(String baseDir) {
		_baseDir = baseDir;
	}
	
	
}
