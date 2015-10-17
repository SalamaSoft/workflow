package com.salama.workflow.core.config;

import java.io.Serializable;

public class ClassSetting implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5723540251631474721L;
	
	private String className = "";

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
}
